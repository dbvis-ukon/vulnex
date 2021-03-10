import { combineReducers } from 'redux';
import { filterReducer } from './filterReducer';
import { sortReducer } from './sortReducer';
import { tooltipReducer } from './tooltipReducer';
import { topBugsReducer } from './topBugsReducer';
import { viewChoiceReducer } from './viewChoiceReducer';
import { searchPopupReducer } from './searchPopupReducer';
import { graphReducer } from './graphReducer';
import { bugDisplayReducer } from './bugDisplayReducer';

const allReducers = combineReducers({
  filter: filterReducer,
  sort: sortReducer,
  tooltip: tooltipReducer,
  topBugs: topBugsReducer,
  viewChoice: viewChoiceReducer,
  searchPopup: searchPopupReducer,
  graph: graphReducer,
  bugDisplay: bugDisplayReducer,
});

export default allReducers;