import * as React from 'react';
import { IoIosArrowDown, IoIosArrowForward, IoIosFolder, IoIosGitNetwork } from 'react-icons/io';
import './moduleTreeElement.css'
import TableRowEntry from 'components/treeView/tableRowEntry';
import { connect } from 'react-redux';
import { changeGraphToModule } from 'reducers/graphReducer';
import { chooseGraphView } from 'reducers/viewChoiceReducer';
import ItemType from 'model/itemType';
import DataStorageService from 'service/dataStorageService';
import { MODULE_COLOR } from '../../../constants';
import { filterChildren, lastChild, TreeElementProps } from 'model/treeElementProps';
import ModuleData from 'model/data/moduleData';
import TreeElement from '../treeElement';
import { INTERVALS } from 'reducers/bugDisplayReducer';
import { max, min } from 'd3';

interface State {
    expanded: boolean;
}

class ModuleTreeElement extends React.Component<TreeElementProps, State>  {

    constructor(props: TreeElementProps) {
        super(props);
        this.state = { expanded: false };
    }

    public render() {
        const item = lastChild(this.props);
        const data = item.data as ModuleData;

        const element = () => {
            const filtered = filterChildren(this.props);

            const arrowClickEvent = () => {
                this.setState({ expanded: !this.state.expanded })
            };

            const handleGraphSymbolClick = () => {
                //@ts-ignore
                this.props.changeGraphToModule(this.props.item.id)
                //@ts-ignore
                this.props.chooseGraphView();
            };

            let expandSymbol = <span>•</span>;
            if (filtered.length > 0) {
                expandSymbol = this.state.expanded ? <IoIosArrowDown onClick={arrowClickEvent} /> : <IoIosArrowForward onClick={arrowClickEvent} />;
            }

            const graphSymbol = <IoIosGitNetwork onClick={handleGraphSymbolClick} />;

            const text = data.artifactId; // + ' (' + this.props.module.subModules.length + ')';

            const elements = filtered.map((e, i) => <TreeElement key={i} tableState={this.props.tableState} dataItems={this.props.dataItems.concat([e])} />)

            const indent = { margin: '0px 0px 0px ' + (this.props.dataItems.length === 1 ? 0 : 25) + 'px' }

            return (
                <div style={indent}>
                    {expandSymbol} {graphSymbol}&nbsp;
                    <div style={{ display: 'inline', color: MODULE_COLOR }}>
                        <IoIosFolder /> {text}
                    </div>
                    <TableRowEntry itemType={ItemType.Module} itemId={item.id} bugIds={item.refs.bugIds} showAllBugs={false} bugDisplay={INTERVALS} />
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
        changeGraphToModule: (id: number) => dispatch(changeGraphToModule(id)),
        chooseGraphView: () => dispatch(chooseGraphView()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(ModuleTreeElement);
