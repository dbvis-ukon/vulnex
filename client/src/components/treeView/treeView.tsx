import * as React from 'react';
import { connect } from 'react-redux';
import './treeView.css'
import { applyDescendingMaxCvssSorting } from 'reducers/sortReducer';
import TableRowHeader from 'components/treeView/tableRowHeader';
import DataStorageService from 'service/dataStorageService';
import TreeElement from './treeElement';
import ReferencedItem from 'model/intersection/referencedItem';
import ItemType from 'model/itemType';
import { determineTableConfig } from 'model/treeElementProps';
import { INTERVALS } from 'reducers/bugDisplayReducer';
import { BUG_TABLE } from 'reducers/viewChoiceReducer';

interface Props {
    tableState: string;
}

interface State {
    dataAvailable: boolean;
}

class TreeView extends React.Component<Props, State>  {

    constructor(props: Props) {
        super(props);
        this.state = { dataAvailable: false };
    }

    public componentDidMount() {
        const dataPromise = DataStorageService.getInstance().loadData();
        dataPromise.then(r => {
            this.setState({ dataAvailable: true });
        });
    }

    private determineRootDataItems(): ReferencedItem[] {
        switch (determineTableConfig(this.props.tableState)[0]) {
            case ItemType.Bug:
                return DataStorageService.getInstance().getReferencedBugs();
            case ItemType.Library:
                return DataStorageService.getInstance().getReferencedFiles();
            case ItemType.Module:
                return DataStorageService.getInstance().getReferencedModules();
            case ItemType.Repository:
                return DataStorageService.getInstance().getReferencedRepositories();
            default:
                throw Error('No such tree element type!');
        }
    }

    public render() {
        if (!this.state.dataAvailable) {
            return null;
        }
        // @ts-ignore
        const items = this.determineRootDataItems().filter(e => e.data.name.includes(this.props.search));
        if (this.props.tableState === BUG_TABLE) {
            // @ts-ignore
            items.sort((a, b) => b.data.cvssScore - a.data.cvssScore);
        } else {
            // @ts-ignore
            items.sort(this.props.sorting.sortFunc);
        }
        const elements = items.map((e, i) => <TreeElement key={i} tableState={this.props.tableState} dataItems={[e]} />)
        return (
            <div>
                <TableRowHeader topBugs={[]} bugDisplay={INTERVALS}/>
                <div className='TreeView'>
                    {elements}
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return {
        search: state.filter.search,
        sorting: state.sort,
    };
}

const mapDispatchToProps = (dispatch: any, ownProps: Props) => {
    return {
        updateSorting: () => dispatch(applyDescendingMaxCvssSorting())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(TreeView);
