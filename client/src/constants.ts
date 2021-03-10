import ItemType from "model/itemType";

// Colors

export const REPOSITORY_COLOR = "#000000";
export const MODULE_COLOR = "#1f77b4";
export const LIBRARY_COLOR = "#8c564b";
export const BUG_COLOR = "#d62728";

// Table config

export const REPOSITORY_TABLE_CONFIG = [
  ItemType.Repository,
  ItemType.Module,
  ItemType.Library,
  ItemType.Bug,
];

export const LIBRARY_TABLE_CONFIG = [
  ItemType.Library,
  ItemType.Bug,
  ItemType.Repository,
  ItemType.Module,
];

export const BUG_TABLE_CONFIG = [
  ItemType.Bug,
  ItemType.Library,
  ItemType.Repository,
  ItemType.Module,
];
