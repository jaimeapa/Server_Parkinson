package Pojos;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;





public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private List<User> users;

    public Role() {
        super();
    }

    public Role(int role_id, String name)
    {
        this.id = role_id;
        this.name = name;
    }
    public Role(String name) {
        super();
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, users);
    }

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

    @Override
    public String toString() {
        return this.name;
    }
}
