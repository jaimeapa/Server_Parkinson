package Pojos;

import java.io.Serializable;

import java.rmi.NotBoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class User implements Serializable {


	private static final long serialVersionUID = 5280902101084365639L;
	private Integer id;
	private String email;
	private byte[] password;
	private Role role;
	
	public User() {
		super();
	}

	public User (int id, String email, byte[] password, Role role){
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	public User(String email, byte[] password, Role role) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) throws NotBoundException {
		Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
		Matcher mather = pattern.matcher(email);
		if (mather.find() == true) {
			this.email = email;
		} else {
			throw new NotBoundException("Not valid email");
		}
	}
	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + Objects.hash(email, id, role);
		return result;
	}

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + Arrays.toString(password) + ", role=" + role
				+ "]";
	}
	
	
}
