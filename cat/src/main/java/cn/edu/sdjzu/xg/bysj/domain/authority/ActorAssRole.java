package cn.edu.sdjzu.xg.bysj.domain.authority;

import util.IdService;

import java.io.Serializable;
import java.util.Date;

public class ActorAssRole implements Comparable<ActorAssRole>,Serializable{
	
	private Integer id;
	private Actor actor;
	private Role role;
	private Date createTime;
	
	{
		this.id = IdService.getId();
	}

	public ActorAssRole(Actor actor, Role role, Date createTime) {
		super();
		this.actor = actor;
		this.role = role;
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public Actor getActor() {
		return actor;
	}

	public Role getRole() {
		return role;
	}

	public Date getCreateTime() {
		return createTime;
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
	    
	    retValue = "ActorAssRole ( "
//	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "actor = " + this.actor + TAB
	        + "role = " + this.role + TAB
	        + "createTime = " + this.createTime + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int compareTo(ActorAssRole o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}
	
	
}
