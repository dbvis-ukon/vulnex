import * as React from 'react';
import './tooltip.css';
import { connect } from 'react-redux';

interface Props {
    visible: boolean;
    xPos: number;
    yPos: number;
    content: string;
}

interface State {

}

class Tooltip extends React.Component<Props, State>  {

    public render() {
        if (!this.props.visible) {
            return null;
        }
        const position = {left: this.props.xPos + 14, top: this.props.yPos + 7};
        return (
            <div className='Tooltip' style={position} dangerouslySetInnerHTML={{ __html: this.props.content}}>
            </div>
        );
    }
}

const mapStateToProps = (state: any) => {
    return state.tooltip;
}
  
export default connect(mapStateToProps)(Tooltip);
  