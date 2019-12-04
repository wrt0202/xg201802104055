package cn.edu.sdjzu.xg.bysj.domain;

import java.io.Serializable;

public final class GraduateProjectCategory implements Comparable<GraduateProjectCategory>,Serializable{
	private Integer id;
	private String description;
	private String no;
	private String remarks;
	
	public GraduateProjectCategory(Integer id, String description, String no,
                                   String remarks) {
		super();
		this.id = id;
		this.description = description;
		this.no = no;
		this.remarks = remarks;
	}
	
	
	
	public void setId(Integer id) {
		this.id = id;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public void setNo(String no) {
		this.no = no;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public Integer getId() {
		return id;
	}



	public String getDescription() {
		return description;
	}



	public String getNo() {
		return no;
	}



	public String getRemarks() {
		return remarks;
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
	    
	    retValue = "ProjectCategrory ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "description = " + this.description + TAB
	        + "no = " + this.no + TAB
	        + "remarks = " + this.remarks + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int compareTo(GraduateProjectCategory o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}		
}
