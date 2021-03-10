import * as React from 'react';
import './repositoryTreeElement.css';
import { IoIosArrowDown, IoIosArrowForward, IoIosCloseCircle, IoIosApps, IoIosGitNetwork } from 'react-icons/io';
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
    
            const isErrorOn = () => {
                if (item.refs.bugIds === null) {
                    return false;
                }
                if (item.refs.bugIds.length <= 0) {
                    return false;
                }
                return true;
            };
    
            const handleGraphSymbolClick = () => {
                //@ts-ignore
                this.props.changeGraphToRepository(item.id)
                //@ts-ignore
                this.props.chooseGraphView();
            };
    
            let expandSymbol = <span>•</span>;
            if (filtered.length > 0) {
                expandSymbol = this.state.expanded ? <IoIosArrowDown onClick={arrowClickEvent} /> : <IoIosArrowForward onClick={arrowClickEvent} />;
            }
    
            const graphSymbol = <IoIosGitNetwork onClick={handleGraphSymbolClick} />;
    
            const errorIcon = isErrorOn() ? <IoIosCloseCircle /> : <span className='EmptySymbol'></span>;
    
            const elements = filtered.map((e, i) => <TreeElement key={i} tableState={this.props.tableState} dataItems={this.props.dataItems.concat([e])} />);
    
            const indent = { margin: '0px 0px 0px ' + (this.props.dataItems.length === 1 ? 0 : 25) + 'px' }
    
            return (
                <div style={indent}>
                    {expandSymbol} {graphSymbol}&nbsp;
                    <div style={{ display: 'inline', color: REPOSITORY_COLOR }}>
                        {errorIcon} <IoIosApps />  {data.name}
                    </div>
                    <TableRowEntry itemType={ItemType.Repository} itemId={item.id} bugIds={item.refs.bugIds} showAllBugs={false} bugDisplay={INTERVALS} />
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
        if (this.props.filter.showAllModules && numErrors === 0) {
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
    };
}

const mapDispatchToProps = (dispatch: any, ownProps: TreeElementProps) => {
    return {
        changeGraphToRepository: (id: number) => dispatch(changeGraphToRespository(id)),
        chooseGraphView: () => dispatch(chooseGraphView()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(RepositoryTreeElement);
