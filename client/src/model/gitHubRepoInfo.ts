export default interface GitHubRepoInfo {
    id: number;
    node_id: string;
    name: string;
    full_name: string;
    private: boolean;

    html_url: string;
    description: string;
    fork: boolean;

    homepage: string;
    size: number;
    stargazers_count: number;
    watchers_count: number;
    language: string;
    has_issues: boolean;
    has_projects: boolean;
    has_downloads: boolean;
    has_wiki: boolean;
    has_pages: boolean;
    forks_count: number;

    archived: false;
    disabled: false;
    open_issues_count: number;

    forks: number,
    open_issues: number,
    watchers: number,
    network_count: number,
    subscribers_count: number
}