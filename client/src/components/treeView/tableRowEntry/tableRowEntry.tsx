import * as React from 'react';
import './tableRowEntry.css';
import DataStorageService from 'service/dataStorageService';
import ActiveBugsEntry from 'components/treeView/tableRowEntry/activeBugsEntry';
import ItemType from 'model/itemType';
import { connect } from 'react-redux';
import { INTERVALS } from 'reducers/bugDisplayReducer';

interface Props {
    itemType: ItemType;
    itemId: number;
    numChildren: number;
    bugIds: number[] | null;
    showAllBugs: boolean;
    bugDisplay: string;
}

interface State {

}

class TableRowEntry extends React.Component<Props, State>  {

    public render() {

        if (this.props.bugIds === null) {
            return (
                <div></div>
            )
        }

        const isBug = () => this.props.itemType === ItemType.Bug;

        const bugs = DataStorageService.getInstance().getBugsWithIds(this.props.bugIds);
        const scores = bugs.map(e => e.cvssScore).filter(e => e !== -1);

        const drawHeatmap = () => {
            let scoreDisplay = <div className='ScoreEmptyBackground'>
                <div className='ScoreEmptyForeground'></div>
            </div>;

            if (scores.length !== 0) {
                const positions = scores.map(e => e * 20);
                const minPos = Math.min(...positions);
                const maxPos = Math.max(...positions);

                const rangeStyle = { width: maxPos - minPos, marginLeft: minPos };

                const scoreLines = positions.map((e, i) => {
                    const lineStyle = { marginLeft: e };
                    return (<div key={i} className='ScoreLine' style={lineStyle}></div>);
                });

                scoreDisplay =
                    <div style={{ display: "inline" }}>
                        <div className='ScoreBackground'>
                            <div className='ScoreRange' style={rangeStyle}></div>
                            {scoreLines}
                        </div>
                    </div>
                return scoreDisplay;
            }
        };

        const drawIntervals = () => {
            const lowBugs = scores.filter(score => score >= 0.1 && score <= 3.9).length;
            const mediumBugs = scores.filter(score => score >= 4.0 && score <= 6.9).length;
            const highBugs = scores.filter(score => score >= 7.0 && score <= 8.9).length;
            const criticalBugs = scores.filter(score => score >= 9.0 && score <= 10.0).length;
            return (
                <div className="ScoreContainer">
                    {lowBugs === 0 ? <div className="ScoreEmpty"></div> : <div className="ScoreLow">{isBug() ? '' : lowBugs}</div>}
                    {mediumBugs === 0 ? <div className="ScoreEmpty"></div> : <div className="ScoreMedium">{isBug() ? '' : mediumBugs}</div>}
                    {highBugs === 0 ? <div className="ScoreEmpty"></div> : <div className="ScoreHigh">{isBug() ? '' : highBugs}</div>}
                    {criticalBugs === 0 ? <div className="ScoreEmpty"></div> : <div className="ScoreCritical">{isBug() ? '' : criticalBugs}</div>}
                </div>
            );
        };

        /*
        const formatBigNumber = (n: number) => {
            if (n < 1000) {
                return n;
            }
            return Math.round(n / 1000) + 'k';
        };

        const lgtmGradeColor = (grade: string) => {
            switch (grade) {
                case 'A+':
                    return 'rgb(0, 189, 0)';
                case 'A':
                    return 'rgb(110, 189, 0)';
                case 'B':
                    return 'rgb(167, 189, 0)';
                case 'C':
                    return 'rgb(214, 193, 0)';
                case 'D':
                    return 'rgb(189, 132, 0)';
                case 'E':
                    return 'rgb(189, 79, 0)';
                default:
                    return 'rgb(212, 212, 212)';
            }
        }

        const drawRepoMetaInfo = () => {
            const repo = DataStorageService.getInstance().getSingleRepository(this.props.itemId) as Repository;
            if (!repo.data.metaInfo) {
                return (
                    <div className="MetaInfoContainer">
                        <div className="ScoreEmpty"></div>
                        <div className="ScoreEmpty" style={{ marginRight: '13px' }}></div>
                        <div className="ScoreEmpty"></div>
                        <div className="ScoreEmpty"></div>
                        <div className="ScoreEmpty"></div>
                    </div>
                );
            }
            return (
                <div className="MetaInfoContainer">
                    {repo.data.metaInfo.lgtmGrade === '-' ? <div className="ScoreEmpty"></div> : <div className='MetaInfoEntry' style={{backgroundColor: lgtmGradeColor(repo.data.metaInfo.lgtmGrade)}}>{repo.data.metaInfo.lgtmGrade}</div>}
                    {repo.data.metaInfo.lgtmAlerts === -1 ? <div className="ScoreEmpty" style={{marginRight: '13px'}}></div> : <div className='MetaInfoEntry' style={{marginRight: '8px'}}>{repo.data.metaInfo.lgtmAlerts}</div>}
                    {repo.data.metaInfo.githubIssues === -1 ? <div className="ScoreEmpty" style={{marginRight: '13px'}}></div> : <div className='MetaInfoEntry'>{formatBigNumber(repo.data.metaInfo.githubIssues)}</div>}
                    {repo.data.metaInfo.githubStars === -1 ? <div className="ScoreEmpty" style={{marginRight: '13px'}}></div> : <div className='MetaInfoEntry'>{formatBigNumber(repo.data.metaInfo.githubStars)}</div>}
                    {repo.data.metaInfo.githubWatchers === -1 ? <div className="ScoreEmpty" style={{marginRight: '13px'}}></div> : <div className='MetaInfoEntry'>{formatBigNumber(repo.data.metaInfo.githubWatchers)}</div>}
                </div>
            );
        }
        */

        const dependeciesPosition = { left: '768px' };
        const errorOnlyPosition = { left: '830px' };

        const errorPosition = { left: '815px' };
        const warningPosition = { left: '840px' };

        let dependeciesEntry = null;
        if (this.props.numChildren > 0) {
            dependeciesEntry = <div className='DependenciesEntry' style={dependeciesPosition}>{this.props.numChildren}</div>
        }
        
        let errorEntry = null;
        let warningEntry = null;

        const numErrors = bugs.filter(e => e.cvssScore !== -1).length; 
        const numWarnings = bugs.length - numErrors;

        if (!isBug()) {

            /* Errors */

            if (this.props.showAllBugs) {
                errorEntry = numErrors === 0 ?
                    <div className='DashEntry' style={errorPosition}>-</div>
                    :
                    <div className='ErrorEntry' style={errorPosition}>
                        {this.props.showAllBugs ? bugs.length : numErrors}
                    </div>
                warningEntry = numWarnings === 0 ?
                    <div className='DashEntry' style={warningPosition}>-</div>
                    :
                    <div className='WarningEntry' style={warningPosition}>
                        {numWarnings}
                    </div>
            } else {
                errorEntry = numErrors === 0 ?
                    <div className='DashEntry' style={errorOnlyPosition}>-</div>
                    :
                    <div className='ErrorEntry' style={errorOnlyPosition}>
                        {numErrors}
                    </div>
            }

        } else {
                errorEntry = numErrors === 0 ?
                    <div className='DashEntry' style={errorOnlyPosition}>-</div>
                    :
                    <div className='ErrorEntry' style={errorOnlyPosition}>
                        {
                        //@ts-ignore
                        DataStorageService.getInstance().getReferencedBugsWithIds([this.props.itemId])[0].refs.repositoryIds.length
                        }
                    </div>
        }

        return (
            <div className='TableRowEntry'>
                {dependeciesEntry}
                {errorEntry}
                {warningEntry}
                {this.props.bugDisplay === INTERVALS ? drawIntervals() : drawHeatmap()}
                <div className='TopBugs'>
                    <ActiveBugsEntry bugIds={this.props.bugIds} topBugIds={[]} />
                </div>
                {/*isRepository() ? drawRepoMetaInfo() : null*/}
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return {
        showAllBugs: state.filter.showAllBugs,
        bugDisplay: state.bugDisplay,
    };
}

export default connect(mapStateToProps)(TableRowEntry);
