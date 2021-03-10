interface ViewChoiceAction {
  type: string;
}

interface ViewChoiceState {
  last: string;
  current: string;
}

export const REPOSITORY_TABLE = "REPOSITORY_TABLE";
export const LIBRARY_TABLE = "LIBRARY_TABLE";
export const BUG_TABLE = "BUG_TABLE";
export const GRAPH_VIEW = "GRAPH_VIEW";

const START_STATE: ViewChoiceState = {
  last: REPOSITORY_TABLE,
  current: REPOSITORY_TABLE,
};

export const chooseRepositoryTable = (): ViewChoiceAction => {
  return {
    type: REPOSITORY_TABLE,
  };
};

export const chooseLibraryTable = (): ViewChoiceAction => {
  return {
    type: LIBRARY_TABLE,
  };
};

export const chooseBugTable = (): ViewChoiceAction => {
  return {
    type: BUG_TABLE,
  };
};

export const chooseGraphView = (): ViewChoiceAction => {
  return {
    type: GRAPH_VIEW,
  };
};

export const viewChoiceReducer = (
  state = START_STATE,
  action: ViewChoiceAction
) => {
  switch (action.type) {
    case REPOSITORY_TABLE:
      return {
        last: state.current,
        current: REPOSITORY_TABLE,
      };
    case LIBRARY_TABLE:
      return {
        last: state.current,
        current: LIBRARY_TABLE,
      };
    case BUG_TABLE:
      return {
        last: state.current,
        current: BUG_TABLE,
      };
    case GRAPH_VIEW:
      return {
        last: state.current,
        current: GRAPH_VIEW,
      };
    default:
      return state;
  }
};
