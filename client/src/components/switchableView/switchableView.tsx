import * as React from 'react';
import TreeView from 'components/treeView';
import GraphView from 'components/graphView';
import { connect } from 'react-redux';
import { LIBRARY_TABLE, REPOSITORY_TABLE, BUG_TABLE } from 'reducers/viewChoiceReducer';

interface Props {
    viewChoice: string
}

interface State {

}

class SwitchableView extends React.Component<Props, State>  {

    constructor(props: Props) {
        super(props);
        this.state = { isGraphView: true };
    }   

    public render() {
        let element: JSX.Element;
        if (this.props.viewChoice === REPOSITORY_TABLE) {
            element = <TreeView tableState={REPOSITORY_TABLE} />;
        } else if (this.props.viewChoice === LIBRARY_TABLE) {
            element = <TreeView tableState={LIBRARY_TABLE} />;
        } else if (this.props.viewChoice === BUG_TABLE) {
            element = <TreeView tableState={BUG_TABLE} />;
        } else {
            element = <GraphView itemType={'REPOSITORY'} itemId={-1} />;
        }
        return (
            <div>
                {element}
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return { viewChoice: state.viewChoice.current };
}

export default connect(mapStateToProps)(SwitchableView);