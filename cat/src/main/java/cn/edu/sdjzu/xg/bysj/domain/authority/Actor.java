package cn.edu.sdjzu.xg.bysj.domain.authority;

import java.util.Collection;

public abstract class Actor {	
	private Collection<ActorAssRole> actorAssRoles;

	public Collection<ActorAssRole> getActorAssRoles() {
		return actorAssRoles;
	}

	public void setActorAssRoles(Collection<ActorAssRole> actorAssRoles) {
		this.actorAssRoles = actorAssRoles;
	}
	
	public abstract String getName();
}
