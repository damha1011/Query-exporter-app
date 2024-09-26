package net.javaguides.springboot.mapper;

import net.javaguides.springboot.dto.QueryYaml;
import net.javaguides.springboot.model.Database;
import net.javaguides.springboot.model.Queries;
import net.javaguides.springboot.model.UserEntity;
import net.javaguides.springboot.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class QueryMapper {
    @Autowired
    private QueryRepository queryRepo;

    public QueryMapper (QueryRepository queryRepo) {
        this.queryRepo = queryRepo;
    }

    public static QueryYaml toYamlModel(Queries query) {
        QueryYaml queryYaml = new QueryYaml();
        queryYaml.setInterval(query.getInterval());
        queryYaml.setTimeout(query.getTimeout());
        queryYaml.setDatabaseNames(query.getDatabases().split(","));
        queryYaml.setSql(query.getSql());
        return queryYaml;
    }

    private boolean checkQueryExist (List<Queries> listData, String name) {
        for (Queries query : listData) {
            if (query.getName().equals(name))
                return true;
        }
        return false;
    }

    public Map<String, QueryYaml> toYamlMap(List<Queries> queries) {
        Map<String, QueryYaml> queryMap = new HashMap<String, QueryYaml>();
        for (int i = 0; i < queries.size(); i++) {
            queryMap.put(queries.get(i).getName(), QueryMapper.toYamlModel(queries.get(i)));
        }
        return queryMap;
    }

    public Map<String, Map<String, Object>> toYamlMapAdd(List<Queries> queries) {
        Map<String, Map<String, Object>> queryMap = new LinkedHashMap<String, Map<String, Object>>();
        for (int i = 0; i < queries.size(); i++) {
            Map<String, Object> queryFieldMap = new LinkedHashMap<String, Object>();
            if (queries.get(i).getInterval() != 0) {
                queryFieldMap.put("interval", queries.get(i).getInterval());
            }
            if (queries.get(i).getTimeout() != 0) {
                queryFieldMap.put("timeout", queries.get(i).getTimeout());
            }
            if (queries.get(i).getDatabases() != null) {
                String[] databasesArray = queries.get(i).getDatabases().split("\\s*,\\s*");
                queryFieldMap.put("databases", databasesArray);
            }
            if (queries.get(i).getMetrics() != null) {
                String[] metricsArray = queries.get(i).getMetrics().split("\\s*,\\s*");
                queryFieldMap.put("metrics", metricsArray);
            }
            if (queries.get(i).getSql() != null) {
                queryFieldMap.put("sql", queries.get(i).getSql());
            }
            if (queries.get(i).getParameters() != null) {
                String[] parametersArray = queries.get(i).getParameters().split("\\s*,\\s*");
                queryFieldMap.put("parameters", parametersArray);
            }
            queryMap.put(queries.get(i).getName(), queryFieldMap);
        }
        return queryMap;
    }
    public List<Queries>toModelFromYaml(Map<String,Map<String,Object>> listQueryMap, UserEntity user){
        List<Queries> result = new ArrayList<Queries>();
        for (String key : listQueryMap.keySet()) {
            Queries query = new Queries();
            Map<String,Object> value = listQueryMap.get(key);
            System.out.println("Key: " + key + ", Value: " + value);

            // Check query exist
            List<Queries> userQueries = queryRepo.findByUsernameFromJoinedTables(user.getUsername());
            if (checkQueryExist(userQueries, key)) return null;

            query.setName(key);

            if(value.containsKey("interval")){
                query.setInterval(Integer.parseInt(value.get("interval").toString()));
            }
            if(value.containsKey("timeout")){
                query.setTimeout(Integer.parseInt(value.get("timeout").toString()));
            }
            if(value.containsKey("databases")){
                query.setDatabases(value.get("databases").toString().replaceAll("\\[|\\]", ""));
            }
            if(value.containsKey("metrics")){
                query.setMetrics(value.get("metrics").toString().replaceAll("\\[|\\]", ""));
            }
            if(value.containsKey("sql")){
                query.setSql(value.get("sql").toString());
            }
            if(value.containsKey("parameters")){
                query.setParameters(value.get("parameters").toString().replaceAll("\\[|\\]", ""));
            }
            query.setUser(user);
            result.add(query);
        }
        return result;
    }
}