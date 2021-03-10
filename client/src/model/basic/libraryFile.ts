import DataItem from 'model/dataItem';

export default interface LibraryFile extends DataItem {
    name: string;
    sha1: string;
    bugIds: number[];
}