interface BugDisplayAction {
  type: string;
}

export const HEATMAP = "HEATMAP";
export const INTERVALS = "INTERVALS";

export const selectHeatmap = (): BugDisplayAction => {
  return {
    type: HEATMAP,
  };
};

export const selectIntervals = (): BugDisplayAction => {
  return {
    type: INTERVALS,
  };
};

export const bugDisplayReducer = (
  state = INTERVALS,
  action: BugDisplayAction
) => {
  switch (action.type) {
    case HEATMAP:
      return HEATMAP;
    case INTERVALS:
      return INTERVALS;
    default:
      return state;
  }
};
