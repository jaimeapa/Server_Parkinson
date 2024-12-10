package Pojos;

import java.util.List;
import java.util.Objects;

/**
 * The `Role` class represents a user role in the system.
 * It contains a unique identifier, a name for the role, and a list of associated users.
 */
public class Role {
    // Fields

    /** Unique identifier for the role. */
    private Integer id;

    /** Name of the role. */
    private String name;

    /** List of users associated with the role. */
    private List<User> users;

    // Constructors

    /**
     * Default constructor for the `Role` class.
     */
    public Role() {
        super();
    }

    /**
     * Constructs a `Role` with a unique identifier and name.
     *
     * @param role_id the unique identifier for the role.
     * @param name    the name of the role.
     */
    public Role(int role_id, String name) {
        this.id = role_id;
        this.name = name;
    }

    /**
     * Constructs a `Role` with only a name.
     *
     * @param name the name of the role.
     */
    public Role(String name) {
        super();
        this.name = name;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the role.
     *
     * @return the role ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the role.
     *
     * @param id the role ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of the role.
     *
     * @return the role name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     *
     * @param name the role name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of users associated with the role.
     *
     * @return the list of users.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets the list of users associated with the role.
     *
     * @param users the list of users to set.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Override Methods

    /**
     * Computes the hash code for the `Role` object.
     *
     * @return the hash code value for the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, users);
    }

    /**
     * Compares this `Role` to another object for equality.
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
        Role other = (Role) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(users, other.users);
    }

    /**
     * Returns a string representation of the `Role` object.
     *
     * @return the name of the role.
     */
    @Override
    public String toString() {
        return this.name;
    }
}

