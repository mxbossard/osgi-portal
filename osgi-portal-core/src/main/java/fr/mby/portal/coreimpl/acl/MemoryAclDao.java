
package fr.mby.portal.coreimpl.acl;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IAclDao;
import fr.mby.portal.core.acl.RoleAlreadyExistsException;
import fr.mby.portal.core.acl.RoleNotFoundException;
import fr.mby.portal.core.security.PrincipalAlreadyExistsException;
import fr.mby.portal.core.security.PrincipalNotFoundException;

@Service
public class MemoryAclDao implements IAclDao {

	private final Map<String, IRole> rolesCache = new HashMap<String, IRole>(128);

	private final Map<Principal, Set<IRole>> aclCache = new HashMap<Principal, Set<IRole>>(128);

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

		final IRole role = this.rolesCache.get(name);

		if (role == null) {
			throw new RoleNotFoundException(name);
		}

		return role;
	}

	@Override
	public void updateRole(final IRole role) throws RoleNotFoundException {
		Assert.notNull(role, "No role supplied !");

		final String roleName = role.getName();

		// Test if role exists
		this.findRole(roleName);

		this.rolesCache.put(roleName, role);
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

		// Test if supplied principal is already known
		final Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		if (alreadyGrantedRoles == null) {
			throw new PrincipalNotFoundException(principal);
		}

		// Search all supplied role for existence
		for (final IRole role : roles) {
			this.findRole(role.getName());
		}

		alreadyGrantedRoles.addAll(roles);

		return Collections.unmodifiableSet(alreadyGrantedRoles);
	}

	@Override
	public Set<IRole> revokeRoles(final Principal principal, final Set<IRole> roles) throws RoleNotFoundException,
			PrincipalNotFoundException {
		Assert.notNull(principal, "No principal supplied !");
		Assert.notNull(roles, "No roles supplied !");

		// Test if supplied principal is already known
		final Set<IRole> alreadyGrantedRoles = this.aclCache.get(principal);
		if (alreadyGrantedRoles == null) {
			throw new PrincipalNotFoundException(principal);
		}

		// Search all supplied role for existence
		for (final IRole role : roles) {
			this.findRole(role.getName());
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
