interface GraphDisplayAction {
  type: string;
  itemType: string;
  itemId: number;
}

interface GraphState {
    itemType: string;
    itemId: number;
}

const UPDATE_GRAPH = "UPDATE_GRAPH"

const REPOSITORY_ITEM = "REPOSITORY";
const MODULE_ITEM = "MODULE";

const START_STATE: GraphState = { itemType: REPOSITORY_ITEM, itemId: 0 }

export const changeGraphToRespository = (id: number): GraphDisplayAction => {
  return {
    type: UPDATE_GRAPH,
    itemType: REPOSITORY_ITEM,
    itemId: id,
  };
};

export const changeGraphToModule = (id: number): GraphDisplayAction => {
  return {
    type: UPDATE_GRAPH,
    itemType: MODULE_ITEM,
    itemId: id,
  };
};

export const graphReducer = (state = START_STATE, action: GraphDisplayAction) => {
    switch (action.type) {
        case UPDATE_GRAPH:
          return { itemType: action.itemType, itemId: action.itemId };
        default:
          return state;
      }
};
