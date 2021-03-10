import GraphNode from './graphNode';

export default interface BugGraphNode extends GraphNode {
    bugId: number;
    name: string;
    cvssScore: number;
    cvssVersion: number;
}