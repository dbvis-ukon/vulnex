import GraphNode from './graphNode';

export default interface LibraryGraphNode extends GraphNode {
    libraryId: number;
    name: string;
    sha1: string;
}