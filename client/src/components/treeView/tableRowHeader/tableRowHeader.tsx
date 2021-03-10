import * as React from 'react';
import './tableRowHeader.css';
import { IoIosCloseCircle, IoIosArrowDropdown, IoIosArrowDropdownCircle, IoIosArrowDropup, IoIosArrowDropupCircle, IoIosLink, IoIosCog } from 'react-icons/io';
import { applyAscendingAlphabeticalOrdering, applyDescendingAlphabeticalOrdering, applyAscendingDependenciesCountSorting, applyDescendingDependenciesCountSorting, applyAscendingErrorCountSorting, applyDescendingErrorCountSorting, applyAscendingMinCvssSorting, applyDescendingMinCvssSorting, applyAscendingMaxCvssSorting, applyDescendingMaxCvssSorting } from 'reducers/sortReducer';
import { connect } from 'react-redux';
import { showTooltip, hideTooltip } from 'reducers/tooltipReducer';
import { TREE_COLUMN_DESC, DEPENDENCIES_COLUMN_DESC, ERROR_COLUMN_DESC, CVSS_COLUMN_DESC, METAINFO_DESC, TOP_BUGS_DESC } from 'service/tooltipService';
import DataStorageService from 'service/dataStorageService';
import { toggleSearchPopup } from 'reducers/searchPopupReducer';
import { INTERVALS } from 'reducers/bugDisplayReducer';

const ALPHABETICAL_ASC = 'ALPHABETICAL_ASC';
const ALPHABETICAL_DESC = 'ALPHABETICAL_DESC';
const DEPENDENCIES_COUNT_ASC = 'DEPENDENCIES_COUNT_ASC';
const DEPENDENCIES_COUNT_DESC = 'DEPENDENCIES_COUNT_DESC';
const ERROR_COUNT_ASC = 'ERROR_COUNT_ASC';
const ERROR_COUNT_DESC = 'ERROR_COUNT_DESC';
const MIN_CVSS_COUNT_ASC = 'MIN_CVSS_COUNT_ASC';
const MIN_CVSS_COUNT_DESC = 'MIN_CVSS_COUNT_DESC';
const MAX_CVSS_COUNT_ASC = 'MAX_CVSS_COUNT_ASC';
const MAX_CVSS_COUNT_DESC = 'MAX_CVSS_COUNT_DESC';

interface Props {
    topBugs: number[];
    bugDisplay: string;
}

interface State {
    sortingLabel: string,
}

class TableRowHeader extends React.Component<Props, State>  {

    public constructor(props: Props) {
        super(props);
        this.state = { sortingLabel: MAX_CVSS_COUNT_DESC }
    }

    public render() {

        const diagonalLabels = (list: string[], offset: number = 0) => {
            return list.map((e, i) => <div key={i} className='DiagonalLabel' style={{ left: (offset + 7 + (i * 22)) + 'px' }}>{e}</div>)
        };

        const handleClick = (opt: string) => {
            this.setState({ sortingLabel: opt });
            // @ts-ignore
            this.props.updateSorting(opt);
        };

        const createSortButton = (opt: string) => {
            if (opt.endsWith('_ASC')) {
                if (this.state.sortingLabel === opt) {
                    return <IoIosArrowDropupCircle onClick={() => handleClick(opt)} />;
                }
                return <IoIosArrowDropup onClick={() => handleClick(opt)} />
            } else {
                if (this.state.sortingLabel === opt) {
                    return <IoIosArrowDropdownCircle onClick={() => handleClick(opt)} />;
                }
                return <IoIosArrowDropdown onClick={() => handleClick(opt)} />
            }
        };

        const scaleCvssHeader = <div className='CvssHeader'
            onMouseMove={
                //@ts-ignore
                (event) => this.props.showTooltip(event, CVSS_COLUMN_DESC)
            }
            onMouseLeave={
                //@ts-ignore
                () => this.props.hideTooltip()
            }
        >
            CVSS min. / max.
        <div>
            {createSortButton(MIN_CVSS_COUNT_ASC)} {createSortButton(MIN_CVSS_COUNT_DESC)}
            &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
            {createSortButton(MAX_CVSS_COUNT_ASC)} {createSortButton(MAX_CVSS_COUNT_DESC)}
            </div>
        </div>

        const scoreScale = <div className='ScoreScale'>
            <div className='ScaleText' style={{ left: '5px', top: '5px' }}>0.0</div>
            <div className='ScaleText' style={{ left: '105px', top: '5px' }}>5.0</div>
            <div className='ScaleText' style={{ left: '195px', top: '5px' }}>10.0</div>
            <div className='ScaleLineSmall' style={{ left: '11px' }}></div>
            <div className='ScaleLineSmall' style={{ left: '111px' }}></div>
            <div className='ScaleLineSmall' style={{ left: '210px' }}></div>
            <div className='ScaleLineBase'></div>
        </div>;

        const cvssLabelHeader = this.props.bugDisplay === INTERVALS ? <div className='CvssLabelHeader'>{diagonalLabels(['Low', 'Medium', 'High', 'Critical'])}</div> : scoreScale;

        const topBugNames = DataStorageService.getInstance().getBugValuesWithIds(this.props.topBugs, e => e.name);
        const topBugElements = diagonalLabels(topBugNames);
        const lgtmMetaInfoLabelElements = diagonalLabels(['LGTM Grade', 'LGTM Alerts']);
        const gitHubMetaInfoLabelElements = diagonalLabels(['GitHub Issues', 'GitHub Stars', 'GitHub Watchers'], 53);

        return (
            <div className='TableRowHeader'>
                <span className='TreeHeader'
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, TREE_COLUMN_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    Tree
            <div>
                        {createSortButton(ALPHABETICAL_ASC)} {createSortButton(ALPHABETICAL_DESC)}
                    </div>
                </span>
                <span className='DependenciesHeader'
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, DEPENDENCIES_COLUMN_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    <IoIosLink />
                    <div>
                        {createSortButton(DEPENDENCIES_COUNT_ASC)} {createSortButton(DEPENDENCIES_COUNT_DESC)}
                    </div>
                </span>
                <span className='ErrorHeader'
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, ERROR_COLUMN_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    <IoIosCloseCircle />
                    <div>
                        {createSortButton(ERROR_COUNT_ASC)} {createSortButton(ERROR_COUNT_DESC)}
                    </div>
                </span>

                {scaleCvssHeader}
                {cvssLabelHeader}

                <div className='TopBugsHeader'
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, TOP_BUGS_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    {topBugElements}
                    <IoIosCog onClick={
                        //@ts-ignore
                        () => this.props.toggleSearchPopup()
                    } />
                </div>

                <div className='MetaInfoLabelHeader'
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, METAINFO_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    {lgtmMetaInfoLabelElements}
                    {gitHubMetaInfoLabelElements}
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return {
        topBugs: state.topBugs,
        bugDisplay: state.bugDisplay,
    };
}

const mapDispatchToProps = (dispatch: any, ownProps: Props) => {
    return {
        updateSorting: (opt: string) => {
            switch (opt) {
                case ALPHABETICAL_ASC:
                    dispatch(applyAscendingAlphabeticalOrdering());
                    break;
                case ALPHABETICAL_DESC:
                    dispatch(applyDescendingAlphabeticalOrdering());
                    break;
                case DEPENDENCIES_COUNT_ASC:
                    dispatch(applyAscendingDependenciesCountSorting());
                    break;
                case DEPENDENCIES_COUNT_DESC:
                    dispatch(applyDescendingDependenciesCountSorting());
                    break;
                case ERROR_COUNT_ASC:
                    dispatch(applyAscendingErrorCountSorting());
                    break;
                case ERROR_COUNT_DESC:
                    dispatch(applyDescendingErrorCountSorting());
                    break;
                case MIN_CVSS_COUNT_ASC:
                    dispatch(applyAscendingMinCvssSorting());
                    break;
                case MIN_CVSS_COUNT_DESC:
                    dispatch(applyDescendingMinCvssSorting());
                    break;
                case MAX_CVSS_COUNT_ASC:
                    dispatch(applyAscendingMaxCvssSorting());
                    break;
                case MAX_CVSS_COUNT_DESC:
                    dispatch(applyDescendingMaxCvssSorting());
                    break;
                default:
                    dispatch(applyAscendingErrorCountSorting());
            }
        },
        showTooltip: (event: any, content: string) => {
            const info = {
                xPos: event.clientX,
                yPos: event.clientY,
                content
            }
            dispatch(showTooltip(info));
        },
        hideTooltip: () => dispatch(hideTooltip()),
        toggleSearchPopup: () => dispatch(toggleSearchPopup()),
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(TableRowHeader);
