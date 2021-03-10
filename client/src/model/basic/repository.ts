import RepositoryData from 'model/data/repositoryData';
import DataItem from 'model/dataItem';
import Module from './module';

export default interface Repository extends DataItem {
    parentModules: Module[];
    data: RepositoryData;
}