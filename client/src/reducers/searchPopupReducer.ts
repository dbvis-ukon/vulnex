interface SearchPopupAction {
  type: string;
}

const SEARCH_POPUP_SHOW = "SEARCH_POPUP_SHOW";
const SEARCH_POPUP_HIDE = "SEARCH_POPUP_HIDE";
const SEARCH_POPUP_TOGGLE = "SEARCH_POPUP_TOGGLE";

export const showSearchPopup = () => {
  return {
    type: SEARCH_POPUP_SHOW,
  };
};

export const hideSearchPopup = () => {
  return {
    type: SEARCH_POPUP_HIDE,
  };
};

export const toggleSearchPopup = () => {
  return {
    type: SEARCH_POPUP_TOGGLE,
  };
};

export const searchPopupReducer = (
  state = false,
  action: SearchPopupAction
) => {
  switch (action.type) {
    case SEARCH_POPUP_SHOW:
      return true;
    case SEARCH_POPUP_HIDE:
      return false;
    case SEARCH_POPUP_TOGGLE:
      return !state;
    default:
      return state;
  }
};
