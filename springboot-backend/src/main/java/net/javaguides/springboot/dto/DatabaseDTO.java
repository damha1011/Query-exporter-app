package net.javaguides.springboot.dto;

public class DatabaseDTO {
    private long id;
    private String link;
    private String serviceCode;
    private String label;
    private String connectSQL;
    private String autoCommit;
    private String keepConnect;

    public DatabaseDTO(long id, String link, String serviceCode, String label, String connectSQL, String autoCommit, String keepConnect) {
        this.id = id;
        this.link = link;
        this.serviceCode = serviceCode;
        this.label = label;
        this.connectSQL = connectSQL;
        this.autoCommit = autoCommit;
        this.keepConnect = keepConnect;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getConnectSQL() {
        return connectSQL;
    }

    public void setConnectSQL(String connectSQL) {
        this.connectSQL = connectSQL;
    }

    public String getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(String autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getKeepConnect() {
        return keepConnect;
    }

    public void setKeepConnect(String keepConnect) {
        this.keepConnect = keepConnect;
    }
}
