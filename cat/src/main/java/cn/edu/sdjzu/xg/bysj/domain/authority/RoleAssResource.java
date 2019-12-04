package cn.edu.sdjzu.xg.bysj.domain.authority;

import java.io.Serializable;
import java.util.Date;

public class RoleAssResource  implements Comparable<RoleAssResource>,Serializable{
	private static int nextID=1;
	private Integer id;	
	private Role role;
	private Resource resource;
	private Date createTime;
	
	{
		this.id = nextID;
		nextID++;
	}
	
	public Role getRole() {
		return role;
	}
	public Resource getResource() {
		return resource;
	}
	public Date getCreateTime() {
		return createTime;
	}
	
	
	
	public Integer getId() {
		return id;
	}
	public RoleAssResource(Role role, Resource resource, Date createTime) {
		super();
		this.role = role;
		this.resource = resource;
		this.createTime = createTime;
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
	    
	    retValue = "RoleAssResource ( "
//	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "role = " + this.role + TAB
	        + "resource = " + this.resource + TAB
	        + "createTime = " + this.createTime + TAB
	        + " )";
	
	    return retValue;
	}
	@Override
	public int compareTo(RoleAssResource o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}
	
	
}
