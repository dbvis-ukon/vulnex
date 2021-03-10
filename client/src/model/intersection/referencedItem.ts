import BugData from 'model/data/bugData';
import LibraryFileData from 'model/data/libraryFileData';
import ModuleData from 'model/data/moduleData';
import RepositoryData from 'model/data/repositoryData';
import DataItem from 'model/dataItem';
import ItemType from 'model/itemType'
import IdReferences from './idReferences';

export default interface ReferencedItem extends DataItem {
    type: ItemType;
    data: BugData | LibraryFileData | ModuleData | RepositoryData;
    refs: IdReferences;
}