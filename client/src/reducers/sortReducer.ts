import ReferencedItem from 'model/intersection/referencedItem';
import DataStorageService from "service/dataStorageService";

export interface SortAction {
  type: string;
  sortFunc: (a: ReferencedItem, b: ReferencedItem) => number;
}

const SORT_ALPHABETICAL_ASC = "SORT_ALPHABETICAL_ASC";
const SORT_ALPHABETICAL_DESC = "SORT_ALPHABETICAL_DESC";
const SORT_DEPENDENCIES_COUNT_ASC = "SORT_DEPENDENCIES_COUNT_ASC";
const SORT_DEPENDENCIES_COUNT_DESC = "SORT_DEPENDENCIES_COUNT_DESC";
const SORT_ERROR_COUNT_ASC = "SORT_ERROR_COUNT_ASC";
const SORT_ERROR_COUNT_DESC = "SORT_ERROR_COUNT_DESC";
const SORT_MIN_CVSS_ASC = "SORT_MIN_CVSS_ASC";
const SORT_MIN_CVSS_DESC = "SORT_MIN_CVSS_DESC";
const SORT_MAX_CVSS_ASC = "SORT_MAX_CVSS_ASC";
const SORT_MAX_CVSS_DESC = "SORT_MAX_CVSS_DESC";

export const applyAscendingAlphabeticalOrdering = (): SortAction => {
  return {
    type: SORT_ALPHABETICAL_ASC,
    // @ts-ignore
    sortFunc: (a, b) => a.data.name.localeCompare(b.data.name),
  };
};

export const applyDescendingAlphabeticalOrdering = (): SortAction => {
  return {
    type: SORT_ALPHABETICAL_DESC,
    // @ts-ignore
    sortFunc: (a, b) => b.data.name.localeCompare(a.data.name),
  };
};

export const applyAscendingDependenciesCountSorting = (): SortAction => {
  return {
    type: SORT_DEPENDENCIES_COUNT_ASC,
    sortFunc: (a, b) => {
        //console.log('Change function once data is available!');
        return errorFunc(a) - errorFunc(b);
    }
  };
};

export const applyDescendingDependenciesCountSorting = (): SortAction => {
  return {
    type: SORT_DEPENDENCIES_COUNT_DESC,
    sortFunc: (a, b) => {
        return errorFunc(b) - errorFunc(a);
    }
  };
};

const errorFunc = (r: ReferencedItem) =>
    DataStorageService.getInstance().getBugValuesWithIds(r.refs.bugIds, (b) => b.cvssScore).filter(e => e !== -1).length;

export const applyAscendingErrorCountSorting = (): SortAction => {
  return {
    type: SORT_ERROR_COUNT_ASC,
    sortFunc: (a, b) => errorFunc(a) - errorFunc(b),
  };
};

export const applyDescendingErrorCountSorting = (): SortAction => {
  return {
    type: SORT_ERROR_COUNT_DESC,
    sortFunc: (a, b) => errorFunc(b) - errorFunc(a),
  };
};

const minScoreFunc = (r: ReferencedItem) =>
  Math.min(
    ...DataStorageService.getInstance()
      .getBugValuesWithIds(r.refs.bugIds, (b) => b.cvssScore)
      .filter((e) => e !== -1)
  );

export const applyAscendingMinCvssSorting = (): SortAction => {
  return {
    type: SORT_MIN_CVSS_ASC,
    sortFunc: (a, b) => minScoreFunc(a) - minScoreFunc(b),
  };
};

export const applyDescendingMinCvssSorting = (): SortAction => {
  return {
    type: SORT_MIN_CVSS_DESC,
    sortFunc: (a, b) => minScoreFunc(b) - minScoreFunc(a),
  };
};

const maxScoreFunc = (r: ReferencedItem) =>
  Math.max(
    ...DataStorageService.getInstance()
      .getBugValuesWithIds(r.refs.bugIds, (b) => b.cvssScore)
      .filter((e) => e !== -1)
  );

export const applyAscendingMaxCvssSorting = (): SortAction => {
  return {
    type: SORT_MAX_CVSS_ASC,
    sortFunc: (a, b) => maxScoreFunc(a) - maxScoreFunc(b),
  };
};

export const applyDescendingMaxCvssSorting = (): SortAction => {
  return {
    type: SORT_MAX_CVSS_DESC,
    sortFunc: (a, b) => maxScoreFunc(b) - maxScoreFunc(a),
  };
};

export const sortReducer = (
  state = applyDescendingMaxCvssSorting(),
  action: SortAction
) => {
  switch (action.type) {
    case SORT_ALPHABETICAL_ASC:
        return applyAscendingAlphabeticalOrdering();
    case SORT_ALPHABETICAL_DESC:
        return applyDescendingAlphabeticalOrdering();
    case SORT_DEPENDENCIES_COUNT_ASC:
        return applyAscendingDependenciesCountSorting();
    case SORT_DEPENDENCIES_COUNT_DESC:
        return applyDescendingDependenciesCountSorting();
    case SORT_ERROR_COUNT_ASC:
        return applyAscendingErrorCountSorting();
    case SORT_ERROR_COUNT_DESC:
        return applyDescendingErrorCountSorting();
    case SORT_MIN_CVSS_ASC:
        return applyAscendingMinCvssSorting();
    case SORT_MIN_CVSS_DESC:
        return applyDescendingMinCvssSorting();
    case SORT_MAX_CVSS_ASC:
        return applyAscendingMaxCvssSorting();
    case SORT_MAX_CVSS_DESC:
        return applyDescendingMaxCvssSorting();
    default:
        return state;
  }
};
