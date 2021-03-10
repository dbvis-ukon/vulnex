import * as React from 'react';
import RepositoryTreeElement from '../repositoryTreeElement';
import ModuleTreeElement from '../moduleTreeElement';
import FileTreeElement from '../fileTreeElement';
import BugTreeElement from '../bugTreeElement';
import { determineTableConfig, TreeElementProps } from 'model/treeElementProps';
import ItemType from 'model/itemType';

interface State {

}

class TreeElement extends React.Component<TreeElementProps, State>  {

    public render() {
        const treeElementType = determineTableConfig(this.props.tableState)[this.props.dataItems.length - 1];
        switch (treeElementType) {
            case ItemType.Repository:
                return <RepositoryTreeElement tableState={this.props.tableState} dataItems={this.props.dataItems} />;
            case ItemType.Module:
                return <ModuleTreeElement tableState={this.props.tableState} dataItems={this.props.dataItems} />;
            case ItemType.Library:
                return <FileTreeElement tableState={this.props.tableState} dataItems={this.props.dataItems} />;
            case ItemType.Bug:
                return <BugTreeElement tableState={this.props.tableState} dataItems={this.props.dataItems} />
            default:
                throw Error('No such tree element type!');
        }
    }

}

export default TreeElement;
