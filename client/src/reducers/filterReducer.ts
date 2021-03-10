import Filter from "model/filter";

interface FilterAction {
  type: string;
  value: string | number | boolean;
}

const FILTER_NUM_DEPENDENCIES = "FILTER_NUM_DEPENDENCIES";
const FILTER_NUM_ERRORS = "FILTER_NUM_ERRORS";
const FILTER_NUM_WARNINGS = "FILTER_NUM_WARNINGS";
const FILTER_MIN_CVSS = "FILTER_MIN_CVSS";
const FILTER_MAX_CVSS = "FILTER_MAX_CVSS";
const FILTER_SEARCH = "FILTER_SEARCH";
const FILTER_MODULES = "FILTER_MODULES";
const FILTER_BUGS = "FILTER_BUGS";

const HIDDEN_STATE: Filter = {
  numDependencies: 0,
  numErrors: 0,
  numWarnings: 0,
  minCvss: 0.0,
  maxCvss: 10.0,
  search: "",
  showAllModules: false,
  showAllBugs: false,
};

export const applyNumDependenciesFilter = (
  numDepencencies: number
): FilterAction => {
  return {
    type: FILTER_NUM_DEPENDENCIES,
    value: numDepencencies,
  };
};

export const applyNumWarningsFilter = (numWarnings: number): FilterAction => {
  return {
    type: FILTER_NUM_WARNINGS,
    value: numWarnings,
  };
};

export const applyNumErrorsFilter = (numErrors: number): FilterAction => {
  return {
    type: FILTER_NUM_ERRORS,
    value: numErrors,
  };
};

export const applyMinCvssFilter = (minCvss: number): FilterAction => {
  return {
    type: FILTER_MIN_CVSS,
    value: minCvss,
  };
};

export const applyMaxCvssFilter = (maxCvss: number): FilterAction => {
  return {
    type: FILTER_MAX_CVSS,
    value: maxCvss,
  };
};

export const applySearchFilter = (search: string): FilterAction => {
  return {
    type: FILTER_SEARCH,
    value: search,
  };
};

export const applyModuleFilter = (active: boolean): FilterAction => {
  return {
    type: FILTER_MODULES,
    value: active,
  };
};

export const applyBugFilter = (active: boolean): FilterAction => {
  return {
    type: FILTER_BUGS,
    value: active,
  };
};

export const filterReducer = (state = HIDDEN_STATE, action: FilterAction) => {
  switch (action.type) {
    case FILTER_NUM_DEPENDENCIES:
      state.numDependencies = action.value as number;
      return JSON.parse(JSON.stringify(state));
    case FILTER_NUM_ERRORS:
      state.numErrors = action.value as number;
      return JSON.parse(JSON.stringify(state));
    case FILTER_NUM_WARNINGS:
      state.numWarnings = action.value as number;
      return JSON.parse(JSON.stringify(state));
    case FILTER_MIN_CVSS:
      state.minCvss = action.value as number;
      return JSON.parse(JSON.stringify(state));
    case FILTER_MAX_CVSS:
      state.maxCvss = action.value as number;
      return JSON.parse(JSON.stringify(state));
    case FILTER_SEARCH:
      state.search = action.value as string;
      return JSON.parse(JSON.stringify(state));
    case FILTER_MODULES:
      state.showAllModules = action.value as boolean;
      return JSON.parse(JSON.stringify(state));
    case FILTER_BUGS:
      state.showAllBugs = action.value as boolean;
      return JSON.parse(JSON.stringify(state));
    default:
      return state;
  }
};
