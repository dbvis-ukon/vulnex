import * as React from 'react';
import './graphView.css';
import * as d3Base from "d3";
import * as d3Dag from "d3-dag";
import DependencyGraph from 'model/graph/dependencyGraph';
import DataLoaderService from 'service/dataLoaderService';
import RepositoryGraphNode from 'model/graph/repositoryGraphNode';
import ModuleGraphNode from 'model/graph/moduleGraphNode';
import { connect } from 'react-redux';
import ItemType from 'model/itemType';
import LibraryGraphNode from 'model/graph/libraryGraphNode';
import BugGraphNode from 'model/graph/bugGraphNode';
import { REPOSITORY_COLOR } from '../../constants';
import { MODULE_COLOR } from '../../constants';
import { LIBRARY_COLOR } from '../../constants';
import { BUG_COLOR } from '../../constants';
import { hideTooltip, showTooltip } from 'reducers/tooltipReducer';
import { IoIosCloseCircle } from 'react-icons/io';
import { BUG_TABLE, chooseBugTable, chooseLibraryTable, chooseRepositoryTable, LIBRARY_TABLE, REPOSITORY_TABLE } from 'reducers/viewChoiceReducer';

interface Props {
    itemType: string,
    itemId: number,
}

interface State {

}

class GraphView extends React.Component<Props, State>  {
    private width: number;
    private height: number;

    constructor(props: Props) {
        super(props);
        this.width = window.innerWidth - 300;
        this.height = window.innerHeight - 100;
    }

    private createGraph(graphData: DependencyGraph): void {
        const dag = d3Dag.dagStratify()(graphData.nodes);
        let dagLayout = this.arquintLayoutVerySimple(dag, 1.0);
        const newHeightRatio = (dagLayout.children[0].x1 - dagLayout.children[0].x0) / (dagLayout.children[0].y1 - dagLayout.children[0].y0);
        dagLayout = this.arquintLayoutVerySimple(dag, newHeightRatio / 2);
        this.drawGraph(dagLayout);
    }

    private arquintLayoutVerySimple(dag: any, heightRatio: number): any {
        // Determine and store heightRatio
        dag.each((node: any) => node.heightRatio = heightRatio);
        // Layout configuration
        const layout = d3Dag.arquint()
            .size([this.width - 50, this.height - 50])
            .layering(d3Dag.layeringSimplex())
            .decross(d3Dag.decrossTwoLayer())
            .columnAssignment(d3Dag.columnAdjacent().center(true))
            .column2Coord(d3Dag.column2CoordRect())
            .interLayerSeparation(() => 1)
            .columnWidth(() => 1)
            .columnSeparation(() => 1);
        // Apply layout
        layout(dag);
        return dag;
    }

    private drawGraph(dag: any) {
        // Retrieve the DOM element
        const svgSelection = d3Base.select("#dependency-graph")
            .append("svg")
            .attr("width", this.width)
            .attr("height", this.height);

        const graphGroup = svgSelection.append('g')
            .attr('transform', 'translate(25, 25)');

        // Defined edges
        const line = d3Base.line()
            .curve(d3Base.curveCatmullRom)
            // @ts-ignore
            .x(d => d.x)
            // @ts-ignore
            .y(d => d.y);

        // Plot edges
        graphGroup.append('g')
            .selectAll('path')
            .data(dag.links())
            .enter()
            .append('path')
            // @ts-ignore
            .attr('d', ({ data }) => line(data.points))
            .attr('fill', 'none')
            .attr('stroke-width', 1)
            .attr('stroke', 'darkgrey');

        // Select nodes
        const nodes = graphGroup.append('g')
            .selectAll('g')
            .data(dag.descendants())
            .enter()
            .append('g')
            // @ts-ignore
            .attr('transform', ({ x0, x1, y0, y1 }) => `translate(${x0}, ${y0})`);
        // Plot node rectangles
        nodes.append('rect')
            // @ts-ignore
            .attr('width', (d) => d.x1 - d.x0)
            // @ts-ignore
            .attr('height', (d) => d.y1 - d.y0)
            // @ts-ignore
            .attr('fill', (d: any) => {
                if (d.data.type === ItemType.Repository) {
                    return REPOSITORY_COLOR;
                }
                if (d.data.type === ItemType.Module) {
                    return MODULE_COLOR;
                }
                if (d.data.type === ItemType.Library) {
                    return LIBRARY_COLOR;
                }
                if (d.data.type === ItemType.Bug) {
                    return BUG_COLOR;
                }
                return 'black';
            })
            // @ts-ignore
            .on('mousemove', (d) => this.props.showTooltip(window.event, this.createMouseOverText(d)))
            // @ts-ignore
            .on('mouseleave', () => this.props.hideTooltip());
    }

    private createMouseOverText(d: any) {
        if (d.data.type === ItemType.Repository) {
            const rgn = d.data as RepositoryGraphNode;
            return 'Name: ' + rgn.name;
        }
        if (d.data.type === ItemType.Module) {
            const mgn = d.data as ModuleGraphNode;
            return 'Artifact ID: ' + mgn.artifactId + '<br>' +
                'Group ID: ' + mgn.groupId + '<br>' +
                'Version: ' + mgn.version;
        }
        if (d.data.type === ItemType.Library) {
            const lgn = d.data as LibraryGraphNode;
            return 'Name: ' + lgn.name;
        }
        if (d.data.type === ItemType.Bug) {
            const bgn = d.data as BugGraphNode;
            return 'Name: ' + bgn.name + '<br>' +
                'CVSS score: ' + bgn.cvssScore + '<br>' +
                'CVSS version: ' + bgn.cvssVersion;
        }
        return '';
    }

    public componentDidMount() {
        if (this.props.itemId < 0) {
            return;
        }
        if (this.props.itemType === ItemType.Repository) {
            DataLoaderService.getInstance().loadRepositoryGraphData(this.props.itemId).then(e => this.createGraph(e));
        } else if (this.props.itemType === ItemType.Module) {
            DataLoaderService.getInstance().loadModuleGraphData(this.props.itemId).then(e => this.createGraph(e));
        }
    }

    public render() {
        const closeButtonClick = () => {
            // @ts-ignore
            if (this.props.viewChoice.last === REPOSITORY_TABLE) {
                // @ts-ignore
                this.props.updateToRepositoryTable();
                return;
            }
            // @ts-ignore
            if (this.props.viewChoice.last === LIBRARY_TABLE) {
                // @ts-ignore
                this.props.updateToLibraryTable();
                return;
            }
            // @ts-ignore
            if (this.props.viewChoice.last === BUG_TABLE) {
                // @ts-ignore
                this.props.updateToBugTable();
                return;
            }
        };

        return (
            <div>
                <IoIosCloseCircle style={{margin: '0px 0px 0px 5px', fontSize: '22px'}} onClick={closeButtonClick} />
                <div className='GraphView'>
                    <div id="dependency-graph"></div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return {
        viewChoice: state.viewChoice,
        itemType: state.graph.itemType,
        itemId: state.graph.itemId,
    };
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

export default connect(mapStateToProps, mapDispatchToProps)(GraphView);
