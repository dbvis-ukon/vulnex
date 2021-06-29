import * as React from 'react';
import './fileTreeElement.css'
import { IoIosArrowDown, IoIosArrowForward, IoIosDocument } from 'react-icons/io';
import TableRowEntry from 'components/treeView/tableRowEntry';
import ItemType from 'model/itemType';
import { LIBRARY_COLOR } from '../../../constants';
import { filterChildren, lastChild, TreeElementProps } from 'model/treeElementProps';
import TreeElement from '../treeElement';
import LibraryFileData from 'model/data/libraryFileData';
import DataStorageService from 'service/dataStorageService';
import { INTERVALS } from 'reducers/bugDisplayReducer';
import { connect } from 'react-redux';
import {max, min} from 'd3';

interface State {
    expanded: boolean;
}

class FileTreeElement extends React.Component<TreeElementProps, State>  {

    constructor(props: TreeElementProps) {
        super(props);
        this.state = { expanded: false };
    }

    public render() {
        const item = lastChild(this.props);
        const data = item.data as LibraryFileData;

        const element = () => {
            const filtered = filterChildren(this.props);

            const arrowClickEvent = () => {
                this.setState({ expanded: !this.state.expanded })
            };
    
            let expandSymbol = <span>•</span>;
            if (filtered.length > 0) {

                expandSymbol = this.state.expanded ? <IoIosArrowDown onClick={arrowClickEvent} /> : <IoIosArrowForward onClick={arrowClickEvent} />;
            
                if (filtered[0].type !== ItemType.Bug) {
                    // @ts-ignore
                    filtered.sort(this.props.sorting.sortFunc);
                } else {
                    // @ts-ignore
                    filtered.sort((a, b) => b.data.cvssScore - a.data.cvssScore);
                }
            }
    
            const text = data.name;
    
            const applicableBugIds = item.refs.bugIds;
    
            const elements = filtered.map((e, i) => <TreeElement key={i} tableState={this.props.tableState} dataItems={this.props.dataItems.concat([e])} />);
            const indent = { margin: '0px 0px 0px ' + (this.props.dataItems.length === 1 ? 0 : 25) + 'px' }
    
            return (
                <div style={indent}>
                    {expandSymbol}
                    <div style={{ display: 'inline', color: LIBRARY_COLOR }}>
                        <IoIosDocument /> {text}
                    </div>
                    <TableRowEntry itemType={ItemType.Module} itemId={item.id} numChildren={elements.length} bugIds={applicableBugIds} showAllBugs={false} bugDisplay={INTERVALS} />
                    {this.state.expanded ? elements : null}
                </div>
            );
        };

        const numDependencies = item.refs.bugIds.length;

        const scores = DataStorageService.getInstance()
            .getBugValuesWithIds(item.refs.bugIds, e => e.cvssScore);
    
        const filteredScores = scores.filter(e => e !== -1);
        const numErrors = filteredScores.length;
        const minCvss = min(filteredScores);
        const maxCvss = max(filteredScores);

        // @ts-ignore
        if (this.props.filter.showAllModule) {
            return element();
        }        

        //@ts-ignore
        if (numErrors === 0 || numDependencies < this.props.filter.numDependencies || numErrors < this.props.filter.numErrors || minCvss < this.props.filter.minCvss || maxCvss > this.props.filter.maxCvss) {
            return null;
        }
        
        return element();
    }
}

const mapStateToProps = (state: any, ownProps: TreeElementProps) => {
    return {
        filter: state.filter,
        sorting: state.sort,
    };
}

export default connect(mapStateToProps)(FileTreeElement);
