package cn.edu.sdjzu.xg.bysj.domain;

import util.IdService;

import java.io.Serializable;

public final class GraduateProjectStatus implements Comparable<GraduateProjectStatus>,Serializable{
	private Integer id;//对应着数据库表中的非业务主键 object id
	private String description;
	private String no;
	private String remarks;
	{
		this.id = IdService.getId();
	}
	public GraduateProjectStatus(Integer id, String description, String no, String remarks) {
		this(description, no, remarks);
		this.id = id;
	}

	public GraduateProjectStatus(String description, String no, String remarks) {
		super();
		this.description = description;
		this.no = no;
		this.remarks = remarks;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "School ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "description = " + this.description + TAB
	        + "no = " + this.no + TAB
	        + "remarks = " + this.remarks + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int compareTo(GraduateProjectStatus o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}
}
