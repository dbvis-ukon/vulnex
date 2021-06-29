import * as React from 'react';
import './bugTreeElement.css';
import TableRowEntry from 'components/treeView/tableRowEntry';
import { IoIosArrowDown, IoIosArrowForward, IoIosCloseCircle, IoIosWarning } from 'react-icons/io';
import ItemType from 'model/itemType';
import { BUG_COLOR } from '../../../constants';
import { filterChildren, lastChild, TreeElementProps } from 'model/treeElementProps';
import BugData from 'model/data/bugData';
import TreeElement from '../treeElement';
import { connect } from 'react-redux';
import { INTERVALS } from 'reducers/bugDisplayReducer';

interface State {
    expanded: boolean;
}

class BugTreeElement extends React.Component<TreeElementProps, State> {

    constructor(props: TreeElementProps) {
        super(props);
        this.state = { expanded: false };
    }

    public render() {
        const item = lastChild(this.props);
        const data = item.data as BugData;

        const element = () => {
            const filtered = filterChildren(this.props);

            const arrowClickEvent = () => {
                this.setState({ expanded: !this.state.expanded })
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

            const icon = data.cvssScore === -1 ? <IoIosWarning /> : <IoIosCloseCircle />;
    
            const elements = filtered.map((e, i) => <TreeElement key={i} tableState={this.props.tableState} dataItems={this.props.dataItems.concat([e])} />);
            
            const indent = { margin: '0px 0px 0px ' + (this.props.dataItems.length === 1 ? 0 : 25) + 'px' }
                
            return (
                <div style={indent}>
                    {expandSymbol}
                    <div style={{ display: 'inline', color: BUG_COLOR }}>
                        {icon} <a className='BugLink' href={'https://nvd.nist.gov/vuln/detail/' + data.name}>{data.name}</a>
                    </div>
                    <TableRowEntry itemType={ItemType.Bug} itemId={item.id} numChildren={elements.length} bugIds={[item.id]} showAllBugs={false} bugDisplay={INTERVALS} />
                    {this.state.expanded ? elements : null}
                </div>
            );
        };

        // @ts-ignore
        if (this.props.showAllBugs && data.cvssScore === -1) {
            return element();
        }
        // @ts-ignore
        if (data.cvssScore >= this.props.minCvss && data.cvssScore <= this.props.maxCvss) {
            return element();
        }
        return null;
    }
}

const mapStateToProps = (state: any, ownProps: TreeElementProps) => {
    return {
        sorting: state.sort,
        showAllBugs: state.filter.showAllBugs,
        minCvss: state.filter.minCvss,
        maxCvss: state.filter.maxCvss,
    };
}

export default connect(mapStateToProps)(BugTreeElement);
