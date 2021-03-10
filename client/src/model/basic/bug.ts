import DataItem from '../dataItem';
import LibraryFile from './libraryFile';

export default interface Bug extends DataItem {
    file: LibraryFile;
    name: string;
    cvssScore: number;
    cvssVersion: number;
}