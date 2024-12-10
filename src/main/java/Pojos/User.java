package Pojos;

import java.util.Arrays;
import java.util.Objects;

/**
 * The `User` class represents a user in the system with attributes such as ID, email, password, and role.
 */
public class User {
	// Fields

	/** Unique identifier for the user. */
	private Integer id;

	/** Email address of the user. */
	private String email;

	/** Encrypted password of the user as a byte array. */
	private byte[] password;

	/** Role associated with the user. */
	private Role role;

	// Constructors

	/**
	 * Default constructor for the `User` class.
	 */
	public User() {
		super();
	}

	/**
	 * Constructs a `User` object with specified ID, email, password, and role.
	 *
	 * @param id       the unique identifier for the user.
	 * @param email    the email address of the user.
	 * @param password the encrypted password of the user.
	 * @param role     the role associated with the user.
	 */
	public User(int id, String email, byte[] password, Role role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	/**
	 * Constructs a `User` object with specified email, password, and role, without an ID.
	 *
	 * @param email    the email address of the user.
	 * @param password the encrypted password of the user.
	 * @param role     the role associated with the user.
	 */
	public User(String email, byte[] password, Role role) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// Getters and Setters

	/**
	 * Gets the unique identifier for the user.
	 *
	 * @return the user ID.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the user.
	 *
	 * @param id the user ID to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the email address of the user.
	 *
	 * @return the email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address of the user.
	 *
	 * @param email the email address to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the encrypted password of the user.
	 *
	 * @return the password as a byte array.
	 */
	public byte[] getPassword() {
		return password;
	}

	/**
	 * Sets the encrypted password of the user.
	 *
	 * @param password the password as a byte array to set.
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}

	/**
	 * Gets the role associated with the user.
	 *
	 * @return the role of the user.
	 */
	public Role getRole() {
		return role;
	}
	/**
	 * Sets the role associated with the user.
	 *
	 * @param role the role to set.
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	// Override Methods

	/**
	 * Computes the hash code for the `User` object.
	 *
	 * @return the hash code value for the object.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + Objects.hash(email, id, role);
		return result;
	}

	/**
	 * Compares this `User` object to another object for equality.
	 *
	 * @param obj the object to compare with.
	 * @return `true` if the objects are equal; `false` otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Arrays.equals(password, other.password) && Objects.equals(role, other.role);
	}

	/**
	 * Returns a string representation of the `User` object.
	 *
	 * <p>The representation includes the user's ID, email, password, and role.</p>
	 *
	 * @return a string representation of the `User` object.
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + Arrays.toString(password) + ", role=" + role
				+ "]";
	}
}

