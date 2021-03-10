import GraphNode from './graphNode';

export default interface ModuleGraphNode extends GraphNode {
    moduleId: number;
    groupId: string;
    artifactId: string;
    version: string;
}
