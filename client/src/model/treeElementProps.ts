import {
  REPOSITORY_TABLE_CONFIG,
  LIBRARY_TABLE_CONFIG,
  BUG_TABLE_CONFIG,
} from "../constants";
import {
  LIBRARY_TABLE,
  REPOSITORY_TABLE,
  BUG_TABLE,
} from "reducers/viewChoiceReducer";
import DataStorageService from "service/dataStorageService";
import ReferencedItem from "./intersection/referencedItem";
import ItemType from "./itemType";

export interface TreeElementProps {
  tableState: string;
  dataItems: ReferencedItem[];
}

export function determineTableConfig(config: string) {
  switch (config) {
    case REPOSITORY_TABLE:
      return REPOSITORY_TABLE_CONFIG;
    case LIBRARY_TABLE:
      return LIBRARY_TABLE_CONFIG;
    case BUG_TABLE:
      return BUG_TABLE_CONFIG;
    default:
      throw Error("No such table configuration!");
  }
}

export function lastChild(p: TreeElementProps): ReferencedItem {
  return p.dataItems[p.dataItems.length - 1];
}

export function filterChildren(props: TreeElementProps): ReferencedItem[] {
  const target = determineTableConfig(props.tableState)[props.dataItems.length];
  const idSetFunc = idSetAccessor(target);
  let ids = idSetFunc(props.dataItems[0]);
  for (let i = 1; i < props.dataItems.length; i++) {
    ids = ids.filter((e) => idSetFunc(props.dataItems[i]).includes(e));
  }
  switch (target) {
    case ItemType.Bug:
      return DataStorageService.getInstance().getReferencedBugsWithIds(ids);
    case ItemType.Library:
      return DataStorageService.getInstance().getReferencedFilesWithIds(ids);
    case ItemType.Module:
      return DataStorageService.getInstance().getReferencedModulesWithIds(ids);
    case ItemType.Repository:
      return DataStorageService.getInstance().getReferencedRepositoriesWithIds(
        ids
      );
    default:
      return [];
  }
}

function idSetAccessor(itemType: ItemType): (r: ReferencedItem) => number[] {
  switch (itemType) {
    case ItemType.Bug:
      return (r: ReferencedItem) => r.refs.bugIds;
    case ItemType.Library:
      return (r: ReferencedItem) => r.refs.libraryIds;
    case ItemType.Module:
      return (r: ReferencedItem) => r.refs.moduleIds;
    case ItemType.Repository:
      return (r: ReferencedItem) => r.refs.repositoryIds;
    default:
      return (r: ReferencedItem) => [];
  }
}
