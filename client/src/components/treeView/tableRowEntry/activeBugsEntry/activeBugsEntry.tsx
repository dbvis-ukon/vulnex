import * as React from 'react';
import './activeBugsEntry.css';
import { connect } from 'react-redux';

interface Props {
    bugIds: number[];
    topBugIds: number[];
}

interface State {

}

class ActiveBugsEntry extends React.Component<Props, State>  {
    public render() {
        const activeBugs = this.props.topBugIds.map((e, i) => this.props.bugIds.includes(e));
        let activeBugElements;
        // Entry has active bugs
        if (activeBugs.some(e => e)) {
            activeBugElements = activeBugs.map((e, i) => e ? <div key={i} className='ActiveEntry'></div> : <div key={i} className='InactiveEntry'></div>);
        // Show empty line, if there are no empty bugs
        } else {
            const emptyWidth = 22 * this.props.topBugIds.length;
            activeBugElements = <div className='TopBugsEmptyBackground' style={{width: emptyWidth}}>
                <div className='TopBugsEmptyForeground' style={{width: emptyWidth - 10}}></div>
            </div>;
        }
        return (
            <div>
                {activeBugElements}
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return {
        topBugIds: state.topBugs,
    };
}

export default connect(mapStateToProps)(ActiveBugsEntry);
