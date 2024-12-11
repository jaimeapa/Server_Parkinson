package ifaces;

import Pojos.Role;

/**
 * The RoleManager interface provides methods for managing role-related data.
 */
public interface RoleManager {

    /**
     * Retrieves a role based on its unique ID.
     *
     * @param role_id the unique ID of the role.
     * @return the Role object associated with the specified ID.
     */
    Role getRoleById(int role_id);
}