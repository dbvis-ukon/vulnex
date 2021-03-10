import * as React from 'react';
import { Button, Navbar as Bar } from 'react-bootstrap';
import { connect } from 'react-redux';
import { chooseRepositoryTable, chooseLibraryTable, chooseBugTable, REPOSITORY_TABLE, LIBRARY_TABLE, BUG_TABLE, GRAPH_VIEW } from 'reducers/viewChoiceReducer';

interface Props {
    viewChoice: string
}

interface State {

}

class Navbar extends React.Component<Props, State>  {

    public constructor(props: Props) {
        super(props);
    }

    public render(): JSX.Element {

        const handleRepositoryTableButtonClick = () => {
            //@ts-ignore
            this.props.updateToRepositoryTable();
        }

        const handleLibraryTableButtonClick = () => {
            //@ts-ignore
            this.props.updateToLibraryTable();
        }

        const handleBugTableButtonClick = () => {
            //@ts-ignore
            this.props.updateToBugTable();
        }

        return (
            <Bar bg='dark' variant='dark'>
                <Bar.Brand href='#home'>
                    <img
                        alt=''
                        src='/logo-rectangle-light.png'
                        width='125'
                        height='30'
                        className='d-inline-block align-top'
                    />&nbsp;
        Vulnerability Explorer
        </Bar.Brand>
                <Button variant={this.props.viewChoice === REPOSITORY_TABLE ? 'danger' : 'secondary'} onClick={handleRepositoryTableButtonClick}>Repository Table</Button>
                <div>&nbsp;&nbsp;&nbsp;&nbsp;</div>
                <Button variant={this.props.viewChoice === LIBRARY_TABLE ? 'danger' : 'secondary'} onClick={handleLibraryTableButtonClick}>Library Table</Button>
                <div>&nbsp;&nbsp;&nbsp;&nbsp;</div>
                <Button variant={this.props.viewChoice === BUG_TABLE ? 'danger' : 'secondary'} onClick={handleBugTableButtonClick}>Bug Table</Button>
        </Bar>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return { viewChoice: state.viewChoice.current === GRAPH_VIEW ? state.viewChoice.last :  state.viewChoice.current };
}

const mapDispatchToProps = (dispatch: any, ownProps: Props) => {
    return {
        updateToRepositoryTable: () => {
            dispatch(chooseRepositoryTable());
        },
        updateToLibraryTable: () => {
            dispatch(chooseLibraryTable());
        },
        updateToBugTable: () => {
            dispatch(chooseBugTable());
        },
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Navbar);
