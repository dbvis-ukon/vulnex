import LibraryFile from './basic/libraryFile';

export default interface ModuleFileIntersection {
    id: number;
    file: LibraryFile;
    intersection: number[];
}