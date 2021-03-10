import * as React from 'react';
import { connect } from 'react-redux';
import './sidebar.css';
import { IoIosCloseCircle, IoIosLink } from 'react-icons/io';
import { applyNumDependenciesFilter, applyNumErrorsFilter, applyNumWarningsFilter, applyMinCvssFilter, applyMaxCvssFilter, applySearchFilter, applyModuleFilter, applyBugFilter } from 'reducers/filterReducer';
import { Form } from 'react-bootstrap';
import Switch from 'react-switch';
import { HEATMAP, selectHeatmap, selectIntervals } from 'reducers/bugDisplayReducer';
import { hideTooltip, showTooltip } from 'reducers/tooltipReducer';
import { MAX_CVSS_COLUMN_DESC, MIN_CVSS_COLUMN_DESC, NUM_DEPENDENCIES_SLIDER_DESC, NUM_ERRORS_SLIDER_DESC } from 'service/tooltipService';

interface Props {
    numDependencies: number;
    numErrors: number;
    numWarnings: number;
    minCvss: number;
    maxCvss: number;
    showAllModules: boolean;
    showAllBugs: boolean;
    bugDisplay: boolean;
}

interface State {

}

class Sidebar extends React.Component<Props, State>  {

    public render() {

        const handleNumDependeciesSliderChange = (event: any) => {
            // @ts-ignore
            this.props.updateNumDependeciesFilter(parseInt(event.target.value));
        }

        const handleNumErrorsSliderChange = (event: any) => {
            // @ts-ignore
            this.props.updateNumErrorsFilter(parseInt(event.target.value));
        }

        const handleMinCvssSliderChange = (event: any) => {
            // @ts-ignore
            this.props.updateMinCvssFilter(parseFloat(event.target.value));
        }

        const handleMaxCvssSliderChange = (event: any) => {
            // @ts-ignore
            this.props.updateMaxCvssFilter(parseFloat(event.target.value));
        }

        const handleSearchFieldChange = (event: any) => {
            // @ts-ignore
            this.props.updateSearchFilter(event.target.value);
        }

        const handleShowModulesSwitch = () => {
            // @ts-ignore
            this.props.updateModuleFilter(!this.props.showAllModules);
        }

        const handleShowBugsSwitch = () => {
            // @ts-ignore
            this.props.updateBugFilter(!this.props.showAllBugs);
        }

        const handleShowHeatmapSwitch = () => {
            if (this.props.bugDisplay) {
                // @ts-ignore
                this.props.updateToIntervals();
            } else {
                // @ts-ignore
                this.props.updateToHeatmap();
            }    
        }
        return (
            <div className='Sidebar bg-light border-right'>
                <div className="SidebarLabel">Search</div>
                <Form.Control className="SearchField" type="text" placeholder="e.g., eclipse" onChange={handleSearchFieldChange} />
                <div className="SidebarLabel">Filter</div>
                <div className="Filter"
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, NUM_DEPENDENCIES_SLIDER_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    <div className="FilterLabel">Num. <IoIosLink />:</div><div className="FilterValue">&#8805; {this.props.numDependencies}</div>
                    <div>
                        <input type='range' className='RangeSlider' id='numDependenciesSlider' name='numDependencies' defaultValue={0.0} min='0' max='100' step='1' onChange={handleNumDependeciesSliderChange}></input>
                    </div>
                </div>
                <div className="Filter"
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, NUM_ERRORS_SLIDER_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    <div className="FilterLabel">Num. <IoIosCloseCircle />:</div><div className="FilterValue">&#8805; {this.props.numErrors}</div>
                    <div>
                        <input type='range' className='RangeSlider' id='numErrorsSlider' name='numErrors' defaultValue={0.0} min='0' max='100' step='1' onChange={handleNumErrorsSliderChange}></input>
                    </div>
                </div>
                <div className="Filter"
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, MIN_CVSS_COLUMN_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    <div className="FilterLabel">min. CVSS:</div><div className="FilterValue">&#8805; {this.props.minCvss}</div>
                    <div>
                        <input type='range' className='RangeSlider' id='minCvssSlider' name='minCvss' defaultValue={0.0} min='0.0' max='10.0' step='0.1' onChange={handleMinCvssSliderChange}></input>
                    </div>
                </div>
                <div className="Filter"
                    onMouseMove={
                        //@ts-ignore
                        (event) => this.props.showTooltip(event, MAX_CVSS_COLUMN_DESC)
                    }
                    onMouseLeave={
                        //@ts-ignore
                        () => this.props.hideTooltip()
                    }
                >
                    <div className="FilterLabel">max. CVSS:</div><div className="FilterValue">&#8804; {this.props.maxCvss}</div>
                    <div>
                        <input type='range' className='RangeSlider' id='maxCvssSlider' name='maxCvss' defaultValue={10.0} min='0.0' max='10.0' step='0.1' onChange={handleMaxCvssSliderChange}></input>
                    </div>
                </div>
                <div className="SidebarLabel">Options</div>
                <div className="OptionsLabel">Only Show Vulnerable Modules:</div>
                <Switch className="OptionsSwitch" onChange={handleShowModulesSwitch} checked={!this.props.showAllModules} />
                <div className="OptionsLabel">Show Bugs without CVSS score:</div>
                <Switch className="OptionsSwitch" onChange={handleShowBugsSwitch} checked={this.props.showAllBugs} />
                <div className="OptionsLabel">Show Heatmap of CVSS scores:</div>
                <Switch className="OptionsSwitch" onChange={handleShowHeatmapSwitch} checked={this.props.bugDisplay} />
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return {
        numDependencies: state.filter.numDependencies,
        numErrors: state.filter.numErrors,
        numWarnings: state.filter.numWarnings,
        minCvss: state.filter.minCvss,
        maxCvss: state.filter.maxCvss,
        showAllModules: state.filter.showAllModules,
        showAllBugs: state.filter.showAllBugs,
        bugDisplay: (state.bugDisplay === HEATMAP),
    };
}

const mapDispatchToProps = (dispatch: any, ownProps: Props) => {
    return {
        updateNumDependeciesFilter: (n: number) => {
            dispatch(applyNumDependenciesFilter(n));
        },
        updateNumErrorsFilter: (n: number) => {
            dispatch(applyNumErrorsFilter(n));
        },
        updateNumWarningsFilter: (n: number) => {
            dispatch(applyNumWarningsFilter(n));
        },
        updateMinCvssFilter: (n: number) => {
            dispatch(applyMinCvssFilter(n));
        },
        updateMaxCvssFilter: (n: number) => {
            dispatch(applyMaxCvssFilter(n));
        },
        updateSearchFilter: (s: string) => {
            dispatch(applySearchFilter(s));
        },
        updateModuleFilter: (b: boolean) => {
            dispatch(applyModuleFilter(b));
        },
        updateBugFilter: (b: boolean) => {
            dispatch(applyBugFilter(b));
        },
        updateToHeatmap: () => {
            dispatch(selectHeatmap());
        },
        updateToIntervals: () => {
            dispatch(selectIntervals());
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
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Sidebar);
