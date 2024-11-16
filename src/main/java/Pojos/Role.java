package Pojos;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity // Añadir la anotación @Entity para marcar esta clase como una entidad persistente
@Table(name="roles")
public class Role implements Serializable {

   
	private static final long serialVersionUID = 1L;

	@Id // Marcar el campo id como la clave primaria de la entidad
    @GeneratedValue(generator = "roles")
    @TableGenerator(name = "roles", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "roles")
    private Integer id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role") // Relación uno a muchos con la clase User
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
