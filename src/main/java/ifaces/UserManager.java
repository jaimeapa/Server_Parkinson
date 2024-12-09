package ifaces;


import Pojos.Role;
import Pojos.User;

import java.util.*;


public interface UserManager {
	public void addUser(String email, String password, Integer role_id);
	public User logIn(String email, String password);
	public void assignRole(User user, Role role);
	public User checkPassword(String email, String password);
	public void changePassword(User user, String newPassword);
	public User checkUsername(String email);
	public int getIdFromEmail(String email);
}
// ESTO SON LOS METODOS DE JPA Y HAY QUE HACERLO CON JDBC
	 /*void connect();
	 void disconnect();
	 void newRole(Role role);
	 void newUser(User user);
	 List<Role> getRoles();
	 Role getRole(Integer id);
	 User getUser(String email);
	 User checkPassword(String email, String pass);
	 void changePassword(User user, String new_passwd);
*/


