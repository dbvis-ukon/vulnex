import Bug from 'model/basic/bug';
import DataLoaderService from './dataLoaderService';
import LibraryFile from 'model/basic/libraryFile';
import Module from 'model/basic/module';
import Repository from 'model/basic/repository';
import ReferencedItem from 'model/intersection/referencedItem';

export default class DataStorageService {

    private static instance: DataStorageService | undefined;

    private static dataPromise: Promise<void> | undefined;

    private bugs: Bug[];
    private files: LibraryFile[];
    private modules: Module[];
    private repositories: Repository[];

    private referencedBugs: ReferencedItem[];
    private referencedFiles: ReferencedItem[];
    private referencedModules: ReferencedItem[];
    private referencedRepositories: ReferencedItem[];

    private constructor() {
        this.bugs = [];
        this.files = [];
        this.modules = [];
        this.repositories = [];

        this.referencedBugs = [];
        this.referencedFiles = [];
        this.referencedModules = [];
        this.referencedRepositories = [];
    }

    public static getInstance(): DataStorageService {
        if (DataStorageService.instance === undefined) {
            DataStorageService.instance = new DataStorageService();
        }
        return DataStorageService.instance;
    }

    public getBugs(): Bug[] {
        return this.bugs;
    }

    public getSingleBug(id: number): Bug {
        return this.bugs.find(e => e.id === id) as Bug;
    }

    public getBugsWithIds(ids: number[]): Bug[] {
        return ids.map(id => this.getSingleBug(id));
    }

    public getBugValuesWithIds<T>(ids: number[], func: (bug: Bug) => T): T[] {
        return ids.map(id => this.getSingleBug(id)).map(func);
    }

    public getFiles(): LibraryFile[] {
        return this.files;
    }

    public getSingleFile(id: number): LibraryFile | undefined {
        return this.files.find(e => e.id === id);
    }

    public getModules(): Module[] {
        return this.modules;
    }

    public getSingleModule(id: number): Module | undefined {
        return this.modules.find(e => e.id === id);
    }

    public getRepositories(): Repository[] {
        return this.repositories;
    }

    public getSingleRepository(id: number): Repository | undefined {
        return this.repositories.find(e => e.id === id);
    }

    public getReferencedBugs(): ReferencedItem[] {
        return this.referencedBugs;
    }

    public getReferencedBugsWithIds(ids: number[]): ReferencedItem[] {
        return this.referencedBugs.filter(e => ids.includes(e.id));
    }

    public getReferencedFiles(): ReferencedItem[] {
        return this.referencedFiles;
    }

    public getReferencedFilesWithIds(ids: number[]): ReferencedItem[] {
        return this.referencedFiles.filter(e => ids.includes(e.id));
    }

    public getReferencedModules(): ReferencedItem[] {
        return this.referencedModules;
    }

    public getReferencedModulesWithIds(ids: number[]): ReferencedItem[] {
        return this.referencedModules.filter(e => ids.includes(e.id));
    }

    public getReferencedRepositories(): ReferencedItem[] {
        return this.referencedRepositories;
    }

    public getReferencedRepositoriesWithIds(ids: number[]): ReferencedItem[] {
        return this.referencedRepositories.filter(e => ids.includes(e.id));
    }

    public async loadData(): Promise<void> {
        if (DataStorageService.dataPromise !== undefined) {
            return DataStorageService.dataPromise;
        }

        const bugsPromise = DataLoaderService.getInstance().loadBugs();
        const filesPromise = DataLoaderService.getInstance().loadLibraryFiles();
        const modulesPromise = DataLoaderService.getInstance().loadModules();
        const repositoryPromise = DataLoaderService.getInstance().loadRepositories();

        const referencedBugsPromise = DataLoaderService.getInstance().loadReferencedBugs();
        const referencedFilesPromise = DataLoaderService.getInstance().loadReferencedFiles();
        const referencedModulesPromise = DataLoaderService.getInstance().loadReferencedModules();
        const referencedRepositoriesPromise = DataLoaderService.getInstance().loadReferencedRepositories();

        DataStorageService.dataPromise = Promise.all([
                bugsPromise,
                filesPromise,
                modulesPromise,
                repositoryPromise,
                referencedBugsPromise,
                referencedFilesPromise,
                referencedModulesPromise,
                referencedRepositoriesPromise,
        ]).then(r => {
            this.bugs = r[0];
            this.files = r[1];
            this.modules = r[2];
            this.repositories = r[3];

            this.referencedBugs = r[4];
            this.referencedFiles = r[5];
            this.referencedModules = r[6];
            this.referencedRepositories = r[7];
        });
        return DataStorageService.dataPromise;
    }

}
