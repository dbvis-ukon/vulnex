package de.dbvis.sparta.server.core.dataset.sqlite.factories;

import de.dbvis.sparta.server.core.dataset.ModuleListFlattener;
import de.dbvis.sparta.server.core.dataset.sqlite.SqliteDataset;
import de.dbvis.sparta.server.rest.model.basic.Module;
import de.dbvis.sparta.server.rest.model.basic.Repository;
import de.dbvis.sparta.server.rest.model.data.MetaInfoData;
import de.dbvis.sparta.server.rest.model.data.RepositoryData;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class RepositoryFactory {

    private static final Logger log = Logger.getLogger(RepositoryFactory.class.getName());

    private List<Module> flatModuleList;

    public RepositoryFactory() {
        this.flatModuleList = new ModuleListFlattener(SqliteDataset.getInstance().getModules()).flatten();
    }

    public List<Repository> createReportsFromCachedResult(CachedRowSet cachedRowSet,
                                                          Map<String, MetaInfoData> metaInfoDataMap) {
        Map<String, Repository> map = new HashMap<String, Repository>();
        try {
            while (cachedRowSet.next()) {
                final int parentModuleId = cachedRowSet.getInt(2);
                final Optional<Module> optional = flatModuleList.stream().filter(e -> e.getId() == parentModuleId).findAny();
                if (optional.isPresent()) {
                    final String repoName = cachedRowSet.getString(3);
                    final Module parentModule = optional.get();
                    if (map.containsKey(repoName)) {
                        map.get(repoName).getParentModules().add(parentModule);
                    } else {
                        List<Module> parentModules = new ArrayList<Module>();
                        parentModules.add(parentModule);
                        Repository repository = new Repository(
                                cachedRowSet.getInt(1),
                                parentModules,
                                new RepositoryData(repoName, metaInfoDataMap.get(repoName))
                        );
                        map.put(repoName, repository);
                    }
                } else {
                    log.severe("Could not find parent module with id " + parentModuleId + ".");
                }
            }
            for (Repository r : map.values()) {
                r.setBugIds(new RepositoryInfoFactory().createBugIdSet(r.getParentModules()));
            }
        } catch (SQLException e) {
            log.severe("Could not create reports from cached result!");
        }
        return new ArrayList<Repository>(map.values());
    }

}
