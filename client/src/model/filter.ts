export default interface Filter {
  numDependencies: number;
  numErrors: number;
  numWarnings: number;
  minCvss: number;
  maxCvss: number;
  search: string;
  showAllModules: boolean;
  showAllBugs: boolean;
}
