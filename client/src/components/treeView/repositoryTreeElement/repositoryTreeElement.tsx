import * as React from 'react';
import './repositoryTreeElement.css';
import { IoIosArrowDown, IoIosArrowForward, IoIosApps, IoIosGitNetwork } from 'react-icons/io';
import TableRowEntry from 'components/treeView/tableRowEntry';
import { connect } from 'react-redux';
import { changeGraphToRespository } from 'reducers/graphReducer';
import { chooseGraphView } from 'reducers/viewChoiceReducer';
import ItemType from 'model/itemType';
import DataStorageService from 'service/dataStorageService';
import { REPOSITORY_COLOR } from '../../../constants';
import { TreeElementProps, lastChild, filterChildren } from 'model/treeElementProps';
import TreeElement from '../treeElement';
import RepositoryData from 'model/data/repositoryData';
import { INTERVALS } from 'reducers/bugDisplayReducer';
import { max, min } from 'd3';

interface State {
    expanded: boolean;
}

class RepositoryTreeElement extends React.Component<TreeElementProps, State>  {

    public constructor(props: TreeElementProps) {
        super(props);
        this.state = { expanded: false };
    }

    public render() {
        const item = lastChild(this.props);
        const data = item.data as RepositoryData;

        const element = () => {
            const filtered = filterChildren(this.props);

            const arrowClickEvent = () => {
                this.setState({ expanded: !this.state.expanded })
            };
        
            const handleGraphSymbolClick = () => {
                // @ts-ignore
                this.props.changeGraphToRepository(item.id)
                // @ts-ignore
                this.props.chooseGraphView();
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
    
            const graphSymbol = <IoIosGitNetwork onClick={handleGraphSymbolClick} />;

            let numChildren = filtered.filter(e => e.refs.bugIds.length > 0).length;
            //@ts-ignore
            if (this.props.filter.showAllModules) {
                numChildren = filtered.length;
            }

            const elements = filtered.map((e, i) => <TreeElement key={i} tableState={this.props.tableState} dataItems={this.props.dataItems.concat([e])} />);
            
            const indent = { margin: '0px 0px 0px ' + (this.props.dataItems.length === 1 ? 0 : 25) + 'px' }   

            return (
                <div style={indent}>
                    {expandSymbol} {graphSymbol}&nbsp;
                    <div style={{ display: 'inline', color: REPOSITORY_COLOR }}>
                        <IoIosApps /> {data.name}
                    </div>
                    <TableRowEntry itemType={ItemType.Repository} itemId={item.id} numChildren={numChildren} bugIds={item.refs.bugIds} showAllBugs={false} bugDisplay={INTERVALS} />
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
        if (this.props.filter.showAllModules) {
            return element();
        }        

        if (this.props.tableState !== 'REPOSITORY_TABLE') {
            return element();
        }

        if (numErrors === 0
            // @ts-ignore
            || numDependencies < this.props.filter.numDependencies
            // @ts-ignore
            || numErrors < this.props.filter.numErrors
            // @ts-ignore
            || minCvss < this.props.filter.minCvss
            // @ts-ignore
            || maxCvss > this.props.filter.maxCvss
            ) {
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

const mapDispatchToProps = (dispatch: any, ownProps: TreeElementProps) => {
    return {
        changeGraphToRepository: (id: number) => dispatch(changeGraphToRespository(id)),
        chooseGraphView: () => dispatch(chooseGraphView()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(RepositoryTreeElement);
