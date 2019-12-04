package cn.edu.sdjzu.xg.bysj.domain.authority;

import util.IdService;

import java.io.Serializable;

public class Resource implements Comparable<Resource>,Serializable {	
	private static int nextID=1;
	private Integer id;
	private String description;
	private String url;	
	private String no;
	
	{
		this.id= IdService.getId();
	}
	
	public Resource(String description, String url, String no) {
		super();
		this.description = description;
		this.url = url;
		this.no = no;
	}
	
	{
		this.id = nextID;
		nextID++;
	}
	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public String getNo() {
		return no;
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
	    
	    retValue = "Resource ( "
	        
	        + "id = " + this.id + TAB
	        + "description = " + this.description + TAB
	        + "url = " + this.url + TAB
	        + "no = " + this.no + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int compareTo(Resource o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}
	
	
}
