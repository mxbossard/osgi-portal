package fr.mby.portal.coreimpl.acl;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IAclDao;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.acl.IRoleFactory;

@Service
public class MemoryAclDao implements IAclDao {

	private final Map<String, IRole> rolesCache = new HashMap<String, IRole>(128);

	private final Map<Principal, Set<IRole>> aclCache = new HashMap<Principal, Set<IRole>>(128);

	@Autowired(required = true)
	private IRoleFactory roleFactory;

	@Autowired(required = true)
	private IPermissionFactory permissionFactory;

	@Override
	public void createRole(IRole role) {
		Assert.notNull(role, "No role supplied !");
		
		final String roleName = role.getName();
		
		if (StringUtils.hasText(roleName) && !this.rolesCache.containsKey(roleName)) {
			this.rolesCache.put(roleName, role);
		} else {
			// TODO MBD: throw an Exception not yet specified in API !
			throw new RuntimeException("Role already exists !");
		}
	}

	@Override
	public IRole findRole(String name) {
		Assert.hasText(name, "No name supplied !");
		
		final IRole role = this.roleFactory.build(name);
		
		return role;
	}

	@Override
	public IRole addRolePermissions(IRole role, Set<IPermission> permissions) {
		Assert.notNull(role, "No role supplied !");
		Assert.notNull(permissions, "No permissions supplied !");
		
		final String roleName = role.getName();
		final HashSet<IPermission> allPermissions = new HashSet<>(role.getPermissions());
		allPermissions.addAll(permissions);
		
		// Reinitialize the Role in the Factory
		this.roleFactory.initializeRole(roleName, allPermissions, role.getSubRoles());

		return this.roleFactory.build(roleName);
	}

	@Override
	public void resetRole(IRole role) {
		Assert.notNull(role, "No role supplied !");
		
		final String roleName = role.getName();
		
		if (StringUtils.hasText(roleName) && this.rolesCache.containsKey(roleName)) {
			this.rolesCache.put(roleName, role);
		} else {
			// TODO MBD: throw an Exception not yet specified in API !
			throw new RuntimeException("Role doesn't exists !");
		}
	}

	@Override
	public Set<IRole> grantRoles(Principal principal, Set<IRole> roles) {
		Assert.notNull(principal, "No principal supplied !");
		Assert.notNull(roles, "No roles supplied !");
		
		Set<IRole> alreadyGrantedRoles = this.initializeAclCache(principal);
		
		alreadyGrantedRoles.addAll(roles);
		
		return Collections.unmodifiableSet(alreadyGrantedRoles);
	}

	@Override
	public Set<IRole> revokeRoles(Principal principal, Set<IRole> roles) {
		Assert.notNull(principal, "No principal supplied !");
		Assert.notNull(roles, "No roles supplied !");
		
		Set<IRole> alreadyGrantedRoles = this.initializeAclCache(principal);
		
		alreadyGrantedRoles.removeAll(roles);
		
		return Collections.unmodifiableSet(alreadyGrantedRoles);
	}

	@Override
	public Set<IRole> findPrincipalRoles(Principal principal) {
		Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		
		if (alreadyGrantedRoles == null) {
			alreadyGrantedRoles = Collections.emptySet();
		} else {
			alreadyGrantedRoles = Collections.unmodifiableSet(alreadyGrantedRoles);
		}
		
		return alreadyGrantedRoles;
	}

	protected Set<IRole> initializeAclCache(Principal principal) {
		Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		if (alreadyGrantedRoles == null) {
			alreadyGrantedRoles = new HashSet<IRole>(8);
			this.aclCache.put(principal, alreadyGrantedRoles);
		}
		return alreadyGrantedRoles;
	}

}
