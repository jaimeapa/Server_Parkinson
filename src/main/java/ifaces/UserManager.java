package ifaces;


import Pojos.Role;
import Pojos.User;



/**
 * The UserManager interface provides methods for managing user accounts and roles.
 * It includes functionality for adding users, logging in, and managing roles.
 */
public interface UserManager {

	/**
	 * Adds a new user to the system.
	 *
	 * @param email the email address of the user.
	 * @param password the password of the user.
	 * @param role_id the ID of the role assigned to the user.
	 */
	void addUser(String email, String password, Integer role_id);

	/**
	 * Authenticates a user with the given email and password.
	 *
	 * @param email the email address of the user.
	 * @param password the password of the user.
	 * @return the User object if authentication is successful, null otherwise.
	 */
	User logIn(String email, String password);

	/**
	 * Assigns a specific role to a user.
	 *
	 * @param user the User object to which the role is assigned.
	 * @param role the Role object to be assigned.
	 */
	void assignRole(User user, Role role);

	/**
	 * Checks if the provided password matches the user's password.
	 *
	 * @param email the email address of the user.
	 * @param password the password to be checked.
	 * @return the User object if the password matches, null otherwise.
	 */
	User checkPassword(String email, String password);

	/**
	 * Checks if a username (email) exists in the system.
	 *
	 * @param email the email address to be checked.
	 * @return the User object if the username exists, null otherwise.
	 */
	User checkUsername(String email);

	/**
	 * Retrieves the unique ID of a user based on their email address.
	 *
	 * @param email the email address of the user.
	 * @return the unique ID of the user.
	 */
	int getIdFromEmail(String email);
}


