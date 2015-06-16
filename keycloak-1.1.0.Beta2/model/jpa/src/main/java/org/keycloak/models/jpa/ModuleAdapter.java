package org.keycloak.models.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.keycloak.models.ModuleModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.jpa.entities.ModuleEntity;
import org.keycloak.models.jpa.entities.ModuleRoleMappingEntity;

public class ModuleAdapter implements ModuleModel {

	protected RealmModel realm;
	protected EntityManager em;
	protected ModuleEntity moduleEntity;
	protected ApplicationAdapter applicationAdapter;
	
	private final static Logger log = Logger.getLogger(ModuleAdapter.class);
	
	public ModuleAdapter(RealmModel realm, EntityManager em, ModuleEntity moduleEntity, ApplicationAdapter applicationAdapter) {
		this.realm = realm;
		this.em = em;
		this.moduleEntity = moduleEntity;
		this.applicationAdapter = applicationAdapter;
	}
	
	@Override
	public String getId() {
		return moduleEntity.getId();
	}
	
	@Override
	public String getName() {
		return moduleEntity.getName();
	}

	@Override
	public void setName(String name) {
		moduleEntity.setName(name);
	}

	@Override
	public String getDescription() {
		return moduleEntity.getDescription();
	}

	@Override
	public void setDescription(String description) {
		moduleEntity.setDescription(description);
	}

	@Override
	public String getUrl() {
		return moduleEntity.getUrl();
	}

	@Override
	public void setUrl(String url) {
		moduleEntity.setUrl(url);
	}
	
	public ModuleEntity getModuleEntity() {
		return moduleEntity;
	}
	
	@Override
	public List<String> getListRoles(String userId) {
		Set<RoleModel> entities = getRoles(userId);
        List<String> roles = new ArrayList<String>();
        if (entities == null) return roles;
        for (RoleModel entity : entities) {
            roles.add(entity.getName());
        }
        return roles;
	}
	
	@Override
	public RoleModel addRole(String userId, String rolename) {
		RoleModel role = applicationAdapter.getRole(rolename);
        
        Set<RoleModel> roles = getRoles(userId);
        for (RoleModel rm : roles) {
            if (rm.getId().equals(role.getId())) {
                return role;
            }
        }
        
        ModuleRoleMappingEntity moduleRoleMappingEntity = new ModuleRoleMappingEntity();
        moduleRoleMappingEntity.setModule(moduleEntity);
        moduleRoleMappingEntity.setRoleId(role.getId());
        moduleRoleMappingEntity.setUserId(userId);
        em.persist(moduleRoleMappingEntity);
        em.flush();
        
        return role;
	}
	
	@Override
	public boolean removeRole(String userId, RoleModel role) {
		if (role == null) {
            return false;
        }
		
		em.createNamedQuery("deleteModuleRoleMappingByUser")
			.setParameter("module", moduleEntity)
			.setParameter("roleId", role.getId())
			.setParameter("userId", userId)
			.executeUpdate();
		em.flush();
		return true;
	}
	
	@Override
	public void updateModule() {
		if (moduleEntity != null) {
			log.info(""+moduleEntity.toString());
		} else {
			log.info("module is null");
		}
		em.flush();
	}

	@Override
	public Set<RoleModel> getRoles(String userId) {
		Set<RoleModel> roles = new HashSet<RoleModel>();
		
		TypedQuery<ModuleRoleMappingEntity> query = em.createNamedQuery("selectRolesByUserModule", ModuleRoleMappingEntity.class);
		query.setParameter("module", moduleEntity);
	    query.setParameter("userId", userId);
	    
	    List<ModuleRoleMappingEntity> ls = query.getResultList();
	    
	    for (ModuleRoleMappingEntity entity : ls) {
	    	roles.add(applicationAdapter.getRoleById(entity.getRoleId()));
	    }
		
		return roles;
	}
	
	@Override
	public boolean container(String userId, RoleModel role) {
		Set<RoleModel> roles = getRoles(userId);
		if (roles.size() == 0) return false;
		
		for (RoleModel rm : roles) {
			if (rm.getId().equals(role.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RoleModel getRoleByName(String userId, String name) {
	    RoleModel role = applicationAdapter.getRole(name);
	    if (role == null) return null;
	    
	    TypedQuery<ModuleRoleMappingEntity> query = em.createNamedQuery("selectRolesByRoleId", ModuleRoleMappingEntity.class);
		query.setParameter("module", moduleEntity);
	    query.setParameter("userId", userId);
	    query.setParameter("roleId", role.getId());
	    
	    List<ModuleRoleMappingEntity> ls = query.getResultList();
	    
	    if (ls.size() == 0) {
	    	return null;
	    }
	    
		return role;
	}

	@Override
	public boolean hasRole(String roleId) {
		TypedQuery<ModuleRoleMappingEntity> query = em.createNamedQuery("moduleHasRole", ModuleRoleMappingEntity.class);
	    query.setParameter("roleId", roleId);
	    
	    List<ModuleRoleMappingEntity> ls = query.getResultList();
	    if (ls.size() == 0) {
	    	return false;
	    }
	    
		return true;
	}

	@Override
	public boolean removeAllRoles() {
	    em.createNamedQuery("deleteModuleRoleMappingByModule")
	      .setParameter("module", moduleEntity)
	      .executeUpdate();
	    em.flush();
		return true;
	}
	
	public static ModuleEntity toModuleEntity(ModuleModel model, EntityManager em) {
        if (model instanceof ModuleAdapter) {
            return ((ModuleAdapter)model).getModuleEntity();
        }
        return em.getReference(ModuleEntity.class, model.getId());
    }
	
}
