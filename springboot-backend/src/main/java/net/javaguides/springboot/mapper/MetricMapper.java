package net.javaguides.springboot.mapper;

import net.javaguides.springboot.dto.MetricYaml;
import net.javaguides.springboot.dto.MetricYaml;
import net.javaguides.springboot.model.*;
import net.javaguides.springboot.model.Metric;
import net.javaguides.springboot.model.Metric;
import net.javaguides.springboot.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class MetricMapper {
    @Autowired
    private MetricRepository metricRepo;

    public MetricMapper (MetricRepository metricRepo) {
        this.metricRepo = metricRepo;
    }

    public static MetricYaml toYamlModel(Metric metric){
        MetricYaml metricYaml = new MetricYaml();
        metricYaml.setType(metric.getType());
        metricYaml.setDescription(metric.getDescription());
        metricYaml.setLabel(metric.getLabels().split(","));
        return metricYaml;
    }

    private boolean checkMetricExist (List<Metric> listData, String name) {
        for (Metric metric : listData) {
            if (metric.getName().equals(name))
                return true;
        }
        return false;
    }

    public Map<String,MetricYaml> toYamlMap(List<Metric> metrics){
        Map<String,MetricYaml> metricMap = new HashMap<String,MetricYaml>();
        for(int i =0 ; i < metrics.size() ; i++ ){
            metricMap.put(metrics.get(i).getName(),MetricMapper.toYamlModel(metrics.get(i)));
        }
        return metricMap;
    }
    public Map<String,Map<String,Object>>toYamlMapAdd(List<Metric> metrics){
        Map<String,Map<String,Object>> metricMap = new LinkedHashMap<String,Map<String,Object>>();
        for(int i =0 ; i < metrics.size() ; i++ ){
            Map<String,Object> metricFieldMap = new LinkedHashMap<String,Object>();

            if(metrics.get(i).getType() != null){
                metricFieldMap.put("type",metrics.get(i).getType());
            }
            if(metrics.get(i).getDescription() != null && !metrics.get(i).getDescription().isEmpty()){
                metricFieldMap.put("description",metrics.get(i).getDescription());
            }
            if(metrics.get(i).getExpiration() != null){
                metricFieldMap.put("expiration",metrics.get(i).getExpiration());
            }
            if(metrics.get(i).getLabels() != null && !metrics.get(i).getLabels().isEmpty()){
                String[] labelsArray = metrics.get(i).getLabels().split("\\s*,\\s*");
                metricFieldMap.put("labels", labelsArray);
            }
            if(metrics.get(i).getBuckets() != null){
                String[] strArray = metrics.get(i).getBuckets().split("\\s*,\\s*");
                Integer[] bucketsArray = Arrays.stream(strArray)
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new);
                metricFieldMap.put("buckets", bucketsArray);
            }
            if(metrics.get(i).getStates() != null){
                String[] statesArray = metrics.get(i).getStates().split("\\s*,\\s*");
                metricFieldMap.put("states", statesArray);
            }
            if(metrics.get(i).getIncrement() != null){
                metricFieldMap.put("increment",metrics.get(i).getIncrement());
            }
            metricMap.put(metrics.get(i).getName(),metricFieldMap);
        }
        return  metricMap;
    }
    public List<Metric>toModelFromYaml(Map<String,Map<String,Object>> listMetricMap, UserEntity user){
        List<Metric> result = new ArrayList<Metric>();
        for (String key : listMetricMap.keySet()) {
            Metric metric = new Metric();
            Map<String,Object> value = listMetricMap.get(key);
            System.out.println("Key: " + key + ", Value: " + value);

            // Check metric exist
            List<Metric> userMetrics = metricRepo.findByUsernameFromJoinedTables(user.getUsername());
            if (checkMetricExist(userMetrics, key)) return null;

            metric.setName(key);

            if(value.containsKey("type")){
                metric.setType(value.get("type").toString());
            }
            if(value.containsKey("description")){
                metric.setDescription(value.get("description").toString());
            }
            if(value.containsKey("expiration")){
                metric.setExpiration(value.get("expiration").toString());
            }
            if(value.containsKey("labels")){
                metric.setLabels(value.get("labels").toString().replaceAll("\\[|\\]", ""));
            }
            if(value.containsKey("buckets")){
                metric.setBuckets(value.get("buckets").toString().replaceAll("\\[|\\]", ""));
            }
            if(value.containsKey("states")){
                metric.setStates(value.get("states").toString().replaceAll("\\[|\\]", ""));
            }
            if(value.containsKey("increment")){
                metric.setIncrement(value.get("increment").toString());
            }
            metric.setUser(user);
            result.add(metric);
        }
        return result;
    }
}
