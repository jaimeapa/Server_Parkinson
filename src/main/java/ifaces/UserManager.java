package ifaces;


import Pojos.Role;
import Pojos.User;



public interface UserManager {
	void addUser(String email, String password, Integer role_id);
	User logIn(String email, String password);
	void assignRole(User user, Role role);
	User checkPassword(String email, String password);
	void changePassword(User user, String newPassword);
	User checkUsername(String email);
	int getIdFromEmail(String email);
}


