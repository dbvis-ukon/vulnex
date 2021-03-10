export interface TopBugsAction {
  type: string;
  bugId: number;
}

// Bugs by apprearance
// const START_STATE = [50, 51, 15, 14, 13];

// Bugs by severity
const START_STATE = [68, 70, 83, 98, 241];

const ADD_BUG = "ADD_BUG";
const REMOVE_BUG = "REMOVE_BUG";

export const addBug = (bugId: number): TopBugsAction => {
  return {
    type: ADD_BUG,
    bugId,
  };
};

export const removeBug = (bugId: number): TopBugsAction => {
  return {
    type: REMOVE_BUG,
    bugId,
  };
};

export const topBugsReducer = (state = START_STATE, action: TopBugsAction) => {
  switch (action.type) {
    case ADD_BUG:
      state.push(action.bugId);
      return [...state];
    case REMOVE_BUG:
      return [...state.filter((e) => e !== action.bugId)];
    default:
      return state;
  }
};
