import Bug from 'model/basic/bug';
import BugCount from 'model/basic/bugCount';
import Report from 'model/basic/repository';
import LibraryFile from 'model/basic/libraryFile';
import GitHubRepoInfo from 'model/gitHubRepoInfo';
import DependencyGraph from 'model/graph/dependencyGraph';
import Module from 'model/basic/module';
import ReferencedItem from 'model/intersection/referencedItem';

const HOST = 'localhost';
const PORT = '3000';

export default class DataLoaderService {

    private static instance: DataLoaderService | undefined;

    private constructor() {
        
    }

    public static getInstance(): DataLoaderService {
        if (DataLoaderService.instance === undefined) {
            DataLoaderService.instance = new DataLoaderService();
        }
        return DataLoaderService.instance;
    }

    public async loadBugs(): Promise<Bug[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/bugs');
        const data = await response.json();
        return data;
    }

    public async loadBugCounts(): Promise<BugCount[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/bugs/count');
        const data = await response.json();
        return data;
    }

    public async loadTop5BugCount(): Promise<BugCount[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/bugs/count/5');
        const data = await response.json();
        return data;
    }

    public async loadTop5BugSeverity(): Promise<BugCount[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/bugs/severity/5');
        const data = await response.json();
        return data;
    }

    public async loadLibraryFiles(): Promise<LibraryFile[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/files');
        const data = await response.json();
        return data;
    }

    public async loadModules(): Promise<Module[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/modules');
        const data = await response.json();
        return data;
    } 

    public async loadRepositories(): Promise<Report[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/repositories');
        const data = await response.json();
        return data;
    }

    public async loadReferencedBugs(): Promise<ReferencedItem[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/referenced/bugs');
        const data = await response.json();
        return data;
    }

    public async loadReferencedFiles(): Promise<ReferencedItem[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/referenced/files');
        const data = await response.json();
        return data;
    }

    public async loadReferencedModules(): Promise<ReferencedItem[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/referenced/modules');
        const data = await response.json();
        return data;
    }

    public async loadReferencedRepositories(): Promise<ReferencedItem[]> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/referenced/repositories');
        const data = await response.json();
        return data;
    }

    public async loadGitHubRepoInfo(user: string, repo: string): Promise<GitHubRepoInfo> {
        const url = 'https://api.github.com/repos/' + user + '/' + repo;
        const response = await fetch(url);
        const data = await response.json();
        return data; 
    }

    public async loadRepositoryGraphData(id: number): Promise<DependencyGraph> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/graph/repository/' + id);
        const data = await response.json();
        return data;
    }

    public async loadModuleGraphData(id: number): Promise<DependencyGraph> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/graph/module/' + id);
        const data = await response.json();
        return data;
    }

    public async reloadDataset(): Promise<any> {
        const response = await fetch('http://' + HOST + ':' + PORT + '/api/dataset/reload');
        const data = await response.json();
        return data;
    }

}