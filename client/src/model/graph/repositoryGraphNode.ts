import GraphNode from './graphNode';

export default interface RepositoryGraphNode extends GraphNode {
    repoId: number;
    name: string;
}
