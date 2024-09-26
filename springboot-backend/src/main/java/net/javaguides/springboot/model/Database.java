package net.javaguides.springboot.model;


import javax.persistence.*;

@Entity
@Table(name = "DB")
public class Database {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "dsn")
	private String dsn;

	@Column(name = "labels")
	private String label;

	@Column(name = "host_name")
	private String hostName;

	@Column(name = "services_code")
	private String serviceCode;

	@Column(name = "linkDB")
	private String link;

	@Column(name = "connect_sql")
	private String connectSQL;

	@Column(name = "keep_connect")
	private String keepConnect;

	@Column(name = "autoCommit")
	private String autoCommit;

	@ManyToOne
	@JoinColumn(name = "user_id") // This specifies the foreign key column in the Post table
	private UserEntity user;

	public Database() {

	}

	public Database(long id, String label, String serviceCode, String link) {
		this.id = id;
		this.label = label;
		this.serviceCode = serviceCode;
		this.link = link;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDsn() {
		return dsn;
	}

	public void setDsn(String dsn) {
		this.dsn = dsn;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getLink() {
		return link;
	}

	public String getKeepConnect() {
		return keepConnect;
	}

	public void setKeepConnect(String keepConnect) {
		this.keepConnect = keepConnect;
	}

	public String getAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(String autoCommit) {
		this.autoCommit = autoCommit;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getConnectSQL() {
		return connectSQL;
	}

	public void setConnectSQL(String connectSQL) {
		this.connectSQL = connectSQL;
	}
}
