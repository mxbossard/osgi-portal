
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
import fr.mby.portal.core.acl.RoleAlreadyExistsException;
import fr.mby.portal.core.acl.RoleNotFoundException;
import fr.mby.portal.core.security.PrincipalAlreadyExistsException;
import fr.mby.portal.core.security.PrincipalNotFoundException;

@Service
public class MemoryAclDao implements IAclDao {

	private final Map<String, IRole> rolesCache = new HashMap<String, IRole>(128);

	private final Map<Principal, Set<IRole>> aclCache = new HashMap<Principal, Set<IRole>>(128);

	@Autowired(required = true)
	private IRoleFactory roleFactory;

	@Autowired(required = true)
	private IPermissionFactory permissionFactory;

	@Override
	public void createRole(final IRole role) throws RoleAlreadyExistsException {
		Assert.notNull(role, "No role supplied !");

		final String roleName = role.getName();

		if (StringUtils.hasText(roleName) && !this.rolesCache.containsKey(roleName)) {
			this.rolesCache.put(roleName, role);
		} else {
			throw new RoleAlreadyExistsException(role);
		}
	}

	@Override
	public IRole findRole(final String name) throws RoleNotFoundException {
		Assert.hasText(name, "No name supplied !");

		final IRole role = this.roleFactory.build(name);

		if (role == null) {
			throw new RoleNotFoundException(name);
		}

		return role;
	}

	@Override
	public IRole addRolePermissions(final IRole role, final Set<IPermission> permissions) throws RoleNotFoundException {
		Assert.notNull(role, "No role supplied !");
		Assert.notNull(permissions, "No permissions supplied !");

		final String roleName = role.getName();

		// Reinitialize the Role in the Factory
		final IRole roleFound = this.findRole(roleName);
		final HashSet<IPermission> allPermissions = new HashSet<IPermission>(roleFound.getPermissions());
		allPermissions.addAll(permissions);
		this.roleFactory.initializeRole(roleFound.getName(), allPermissions, roleFound.getSubRoles());

		return this.roleFactory.build(roleName);
	}

	@Override
	public void resetRole(final IRole role) throws RoleNotFoundException {
		Assert.notNull(role, "No role supplied !");

		final String roleName = role.getName();

		final IRole roleFound = this.findRole(roleName);

		final IRole initializedRole = this.roleFactory.initializeRole(roleFound.getName(), roleFound.getPermissions(),
				roleFound.getSubRoles());

		this.rolesCache.put(roleName, initializedRole);

	}

	@Override
	public void registerPrincipal(final Principal principal) throws PrincipalAlreadyExistsException {
		Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		if (alreadyGrantedRoles == null) {
			alreadyGrantedRoles = new HashSet<IRole>(8);
			this.aclCache.put(principal, alreadyGrantedRoles);
		} else {
			throw new PrincipalAlreadyExistsException(principal);
		}

	}

	@Override
	public Set<IRole> grantRoles(final Principal principal, final Set<IRole> roles) throws RoleNotFoundException,
			PrincipalNotFoundException {
		Assert.notNull(principal, "No principal supplied !");
		Assert.notNull(roles, "No roles supplied !");

		// Search all supplied role for existence
		for (final IRole role : roles) {
			this.findRole(role.getName());
		}

		// Test if supplied principal is already known
		final Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		if (alreadyGrantedRoles == null) {
			throw new PrincipalNotFoundException(principal);
		}

		alreadyGrantedRoles.addAll(roles);

		return Collections.unmodifiableSet(alreadyGrantedRoles);
	}

	@Override
	public Set<IRole> revokeRoles(final Principal principal, final Set<IRole> roles) throws RoleNotFoundException,
			PrincipalNotFoundException {
		Assert.notNull(principal, "No principal supplied !");
		Assert.notNull(roles, "No roles supplied !");

		// Search all supplied role for existence
		for (final IRole role : roles) {
			this.findRole(role.getName());
		}

		// Test if supplied principal is already known
		final Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		if (alreadyGrantedRoles == null) {
			throw new PrincipalNotFoundException(principal);
		}

		alreadyGrantedRoles.removeAll(roles);

		return Collections.unmodifiableSet(alreadyGrantedRoles);
	}

	@Override
	public Set<IRole> findPrincipalRoles(final Principal principal) throws PrincipalNotFoundException {
		Assert.notNull(principal, "No principal supplied !");

		// Test if supplied principal is already known
		final Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		if (alreadyGrantedRoles == null) {
			throw new PrincipalNotFoundException(principal);
		}

		return Collections.unmodifiableSet(alreadyGrantedRoles);
	}

}
