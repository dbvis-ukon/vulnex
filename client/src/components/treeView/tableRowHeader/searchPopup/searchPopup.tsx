import * as React from 'react';
import './searchPopup.css';
import { Form } from 'react-bootstrap';
import { connect } from 'react-redux';
import DataStorageService from 'service/dataStorageService';
import { addBug, removeBug } from 'reducers/topBugsReducer';
import { IoIosAddCircle, IoIosCloseCircle } from "react-icons/io";

interface Props {
    visible: boolean;
    topBugs: number[];
}

interface State {
    searchTerm: string;
}

class SearchPopup extends React.Component<Props, State>  {

    constructor(props: Props) {
        super(props);
        this.state = { searchTerm: '' };
    }

    public render() {
        const handleAddBugClick = (id: number) => {
            //@ts-ignore
            this.props.addBug(id);
            this.setState({ searchTerm: this.state.searchTerm });
        }
        const handleRemoveBugClick = (id: number) => {
            //@ts-ignore
            this.props.removeBug(id);
            this.setState({ searchTerm: this.state.searchTerm });
        }
        const handleSearchFieldChange = (event: any) => {
            this.setState({ searchTerm: event.target.value });
        };

        const topBugsObjects = DataStorageService.getInstance().getBugsWithIds(this.props.topBugs);
        
        // @ts-ignore
        if (topBugsObjects.includes(undefined)) {
            return null;
        }
        
        const topBugNames = topBugsObjects.map(e => <div className="CveItem" key={e.id}><IoIosCloseCircle onClick={() => handleRemoveBugClick(e.id)} /> {e.name}</div>);

        let resultItems: JSX.Element[] = [];
        if (this.state.searchTerm.length > 5) {
            resultItems = DataStorageService.getInstance().getBugs()
                .filter(e => e.name.includes(this.state.searchTerm) && !this.props.topBugs.includes(e.id))
                .map(e => <div className="CveItem" key={e.id}><IoIosAddCircle onClick={() => handleAddBugClick(e.id)}/> {e.name}</div>);
        }
        
        const display = { display: this.props.visible ? 'inline-block' : 'none' };
        return (
            <div className="Box" style={display}>
                <div>
                    {topBugNames}
                </div>
                <div className="AddBugLabel">Add Bug:</div>
                <Form.Control className="Search" type="text" placeholder="CVE name" onChange={handleSearchFieldChange} />
                <div>{resultItems}</div>
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return {
        visible: state.searchPopup,
        topBugs: state.topBugs,
    };
}

const mapDispatchToProps = (dispatch: any, ownProps: Props) => {
    return {
        addBug: (id: number) => dispatch(addBug(id)),
        removeBug: (id: number) => dispatch(removeBug(id)),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(SearchPopup);
