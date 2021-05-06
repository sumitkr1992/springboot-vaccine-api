package io.quickstart.pojo;

public class Vaccine {
	private int id;
	private String v_name;
	private int v_avlcap;
	private String v_sname;
	private String v_dname;
	private String v_cname;
	private String v_date;
	
	public Vaccine() {
		
	}
	
	public Vaccine(int id, String v_name, int v_avlcap, String v_sname, String v_dname, String v_cname, String v_date) {
		super();
		this.id = id;
		this.v_name = v_name;
		this.v_avlcap = v_avlcap;
		this.v_sname = v_sname;
		this.v_dname = v_dname;
		this.v_cname = v_cname;
		this.v_date = v_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getV_name() {
		return v_name;
	}
	public void setV_name(String v_name) {
		this.v_name = v_name;
	}
	public int getV_avlcap() {
		return v_avlcap;
	}
	public void setV_avlcap(int v_avlcap) {
		this.v_avlcap = v_avlcap;
	}
	public String getV_sname() {
		return v_sname;
	}
	public void setV_sname(String v_sname) {
		this.v_sname = v_sname;
	}
	public String getV_dname() {
		return v_dname;
	}
	public void setV_dname(String v_dname) {
		this.v_dname = v_dname;
	}
	public String getV_cname() {
		return v_cname;
	}
	public void setV_cname(String v_cname) {
		this.v_cname = v_cname;
	}
	public String getV_date() {
		return v_date;
	}
	public void setV_date(String v_date) {
		this.v_date = v_date;
	}
	
	

}
