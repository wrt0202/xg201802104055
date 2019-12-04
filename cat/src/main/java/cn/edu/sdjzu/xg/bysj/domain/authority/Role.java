package cn.edu.sdjzu.xg.bysj.domain.authority;

import java.io.Serializable;
import java.util.Collection;

public final class Role implements Comparable<Role>,Serializable {
	private static int nextID=1;
	private Integer id;
	private String description;
	private String no;
	
	private Collection<RoleAssResource> roleAssResource;
	
	
	
	public Role(String description, String no) {
		super();
		this.description = description;
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

	public String getNo() {
		return no;
	}

	public Collection<RoleAssResource> getRoleAssResource() {
		return roleAssResource;
	}

	public void setRoleAssResource(Collection<RoleAssResource> roleAssResource) {
		this.roleAssResource = roleAssResource;
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
	    
	    retValue = "Role ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "description = " + this.description + TAB
	        + "no = " + this.no + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int compareTo(Role o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}
	
	
	
}
