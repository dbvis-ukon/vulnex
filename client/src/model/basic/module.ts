import DataItem from 'model/dataItem';
import VulnerableItem from './vulnerableItem';

export default interface Module extends DataItem {
    subModules: Module[];
    groupId: string;
    artifactId: string;
    version: string;
    bugIds: number[];
}