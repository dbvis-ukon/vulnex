export interface TooltipInfo {
  xPos: number;
  yPos: number;
  content: string;
}

interface TooltipState {
  visible: boolean;
  xPos: number;
  yPos: number;
  content: string;
}

export interface TooltipAction {
  type: string;
  tooltip: TooltipState;
}

const SHOW_TOOLTIP = "SHOW_TOOLTIP";
const HIDE_TOOLTIP = "HIDE_TOOLTIP";

const HIDDEN_STATE: TooltipState = {
  visible: false,
  xPos: 0,
  yPos: 0,
  content: '',
};

export const showTooltip = (info: TooltipInfo): TooltipAction => {
  return {
    type: SHOW_TOOLTIP,
    tooltip: {
      visible: true,
      xPos: info.xPos,
      yPos: info.yPos,
      content: info.content,
    },
  };
};

export const hideTooltip = (): TooltipAction => {
  return {
    type: HIDE_TOOLTIP,
    tooltip: HIDDEN_STATE,
  };
};

export const tooltipReducer = (state = HIDDEN_STATE, action: TooltipAction) => {
  switch (action.type) {
    case SHOW_TOOLTIP:
      return action.tooltip;
    case HIDE_TOOLTIP:
      return HIDDEN_STATE;
    default:
      return state;
  }
};
