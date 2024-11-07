package ifaces;


import POJOS.Role;
import POJOS.User;

import java.util.*;


public interface UserManager {
	 void connect();
	 void disconnect();
	 void newRole(Role role);
	 void newUser(User user);
	 List<Role> getRoles();
	 Role getRole(Integer id);
	 User getUser(String email);
	 User checkPassword(String email, String pass);
	 void changePassword(User user, String new_passwd);

}

