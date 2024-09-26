package net.javaguides.springboot.mapper;


import com.viettel.security.PassTranformer;
import net.javaguides.springboot.model.Database;
import net.javaguides.springboot.model.UserEntity;
import net.javaguides.springboot.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.util.*;

public class DatabaseMapper {
    @Autowired
    private DatabaseRepository databaseRepo;

    public DatabaseMapper (DatabaseRepository databaseRepo) {
        this.databaseRepo = databaseRepo;
    }

    private boolean checkDatabaseExist (List<Database> listData, String name) {
        for (Database database : listData) {
            if (database.getName().equals(name))
                return true;
        }
        return false;
    }

    public Map<String,Map<String,Object>>toYamlMapAdd(List<Database> databases){
        Map<String,Map<String,Object>> dbMap = new LinkedHashMap<String,Map<String,Object>>();
        for(int i =0 ; i < databases.size() ; i++ ){
            Map<String,Object> dbFieldMap = new LinkedHashMap<String,Object>();
            if(databases.get(i).getDsn() != null){
                dbFieldMap.put("dsn",databases.get(i).getLink());
            }
            if(databases.get(i).getLabel() != null){
                Map<String,String> labelsFieldMap = new HashMap<String,String>();
                if(databases.get(i).getHostName() != null){
                    labelsFieldMap.put("host",databases.get(i).getHostName());
                }
                if(databases.get(i).getServiceCode() != null){
                    labelsFieldMap.put("services_code",databases.get(i).getServiceCode());
                }
                dbFieldMap.put("labels",labelsFieldMap);
            }
            if(databases.get(i).getConnectSQL() != null){
                dbFieldMap.put("connect-sql",databases.get(i).getConnectSQL());
            }
            if(databases.get(i).getKeepConnect() != null){
                dbFieldMap.put("keep-connected",databases.get(i).getKeepConnect());
            }
            if(databases.get(i).getAutoCommit() != null){
                dbFieldMap.put("autocommit",databases.get(i).getAutoCommit());
            }
            dbMap.put(databases.get(i).getName(),dbFieldMap);
        }
        return dbMap;
    }
    public List<Database>toModelFromYaml(Map<String,Map<String,Object>> listDBMap, UserEntity user) throws Exception {
        //Map<String,Object> dbMap = new listDBMap.get();
            List<Database> result = new ArrayList<Database>();
            for (String key : listDBMap.keySet()) {
                Database db = new Database();
                Map<String,Object> value = listDBMap.get(key);
                System.out.println("Key: " + key + ", Value: " + value);

                // Check database exist
                List<Database> userDb = databaseRepo.findByUsernameFromJoinedTables(user.getUsername());
                if (checkDatabaseExist(userDb, key)) return null;

                db.setName(key);
                if(value.containsKey("dsn")){
                    if (value.get("dsn") instanceof LinkedHashMap) {
                        String dsnStr = "";
                        Map<String, Object> dsnValues = (Map<String, Object>) value.get("dsn");
                        if (dsnValues.containsKey("dialect"))
                            dsnStr += dsnValues.get("dialect").toString() + "://";
                        if (dsnValues.containsKey("user"))
                            dsnStr += dsnValues.get("user") + ":";
                        if (dsnValues.containsKey("password"))
                            dsnStr += dsnValues.get("password");
                        if (dsnValues.containsKey("host")){
//                            db.setLabel("hostname: " + dsnValues.get("host").toString());
                            db.setHostName(dsnValues.get("host").toString());
                            dsnStr += "@" + dsnValues.get("host") + ":";
                        }
                        if (dsnValues.containsKey("port"))
                            dsnStr += dsnValues.get("port") + "/";
                        if (dsnValues.containsKey("database"))
                            dsnStr += dsnValues.get("database");
                        db.setLink(dsnStr);
                        System.out.println("link: " + dsnStr);
                        db.setDsn(PassTranformer.encrypt(dsnStr));
                    }
                    else {
                        String dsn = value.get("dsn").toString();
                        db.setLink(dsn);
                        int start = dsn.indexOf("@") + 1;
                        int end = dsn.lastIndexOf(":");
                        db.setHostName(dsn.substring(start, end));
                        db.setDsn(PassTranformer.encrypt(dsn));
                    }
                }
                if(value.containsKey("labels")){
                    Map<String, String> labels = (Map<String, String>) value.get("labels");
                    String labelStr = "";
                    for (Map.Entry<String,String> entry : labels.entrySet()) {
                        if (entry.getKey().equals("host")) {
                            labelStr += "hostname: " + entry.getValue();
                        } else {
                            if (entry.getKey().equals("services_code")) db.setServiceCode(entry.getValue());
                            labelStr += "\\n" + entry.getKey() + ": " + entry.getValue();
                        }
                    }
                    db.setLabel(labelStr);
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    try {
//                        Map<String, String> resultMap = objectMapper.readValue(value.get("labels").toString(), Map.class);
//                        db.setHostName(resultMap.get("host"));
//                        db.setServiceCode(resultMap.get("services_code"));
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
                }
                if(value.containsKey("services_code")){
                    db.setServiceCode(value.get("services_code").toString());
                }
                if(value.containsKey("connect-sql")){
                    db.setConnectSQL(value.get("connect-sql").toString());
                }
                if(value.containsKey("keep-connected")){
                    db.setKeepConnect(value.get("keep-connected").toString());
                }
                if(value.containsKey("autocommit")){
                    db.setAutoCommit(value.get("autocommit").toString());
                }
                db.setUser(user);
                result.add(db);
            }
            return result;
    }
}
