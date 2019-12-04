package cn.edu.sdjzu.xg.bysj.domain;

import util.IdService;

import java.io.Serializable;

public final class Degree implements
		Comparable<Degree>,Serializable{
	private Integer id;
	private String description;
	private String no;
	private String remarks;
	{
		this.id = IdService.getId();
	}
	public Degree(Integer id, String description, String no, String remarks) {
		this(description, no, remarks);
		this.id = id;
	}
	public Degree(String description, String no, String remarks) {
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Degree other = (Degree) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
	    
	    retValue = "Degree ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "description = " + this.description + TAB
	        + "no = " + this.no + TAB
	        + "remarks = " + this.remarks + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int compareTo(Degree o) {
		return this.id - o.getId();
	}

	
}
