package jpa;

import POJOS.Role;
import POJOS.User;
import ifaces.UserManager;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class JPAManager implements UserManager {
    private EntityManager em;

    public JPAManager() {
        super();
        this.connect();
    }


    @Override
    public User checkPassword(String email, String pass) {
        User user=null;
        try {
            Query query = em.createNativeQuery("SELECT * from users where email =? and password=?", User.class);
            query.setParameter(1, email);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] pw = md.digest();

            query.setParameter(2, pw);


            user = (User) query.getSingleResult();

        }catch(NoResultException e) {
            System.out.println("No user found with the provided email and password.");

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }catch(Exception e) {


        }
        return user;
    }

    @Override
    public void connect() {


        em = Persistence.createEntityManagerFactory("server_parkinson-provider").createEntityManager();

        em.getTransaction().begin();
        em.createNativeQuery("PRAGMA foreign_keys = ON").executeUpdate();
        em.getTransaction().commit();

        if(this.getRoles().isEmpty())
        {
            Role manager = new Role("manager");
            Role doctor = new Role("doctor");
            Role patient=new Role ("patient");
            this.newRole(manager);
            this.newRole(doctor);
            this.newRole(patient);

        }

    }




    @Override
    public List<Role> getRoles() {
        List<Role> roles = null;
        try {
            Query query = em.createNativeQuery("SELECT * FROM roles", Role.class);
            roles = (List<POJOS.Role>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }


    @Override
    public void newRole(Role role) {

        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();

    }



    @Override
    public void newUser(User user) {

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

    }




    @Override
    public void disconnect() {
        em.close();
    }

    @Override
    public User getUser(String email) {
        User user=null;
        try {
            Query query = em.createNativeQuery("SELECT * FROM users where email="+email, User.class);
            user = (User) query.getSingleResult();
        }catch(NoResultException nre) {
            System.out.println("\"No user found with email: \" + email");
        }catch(Exception e) {
            e.printStackTrace();
        }
        return user;
    }



    @Override
    public Role getRole(Integer id) {
        Role role=null;
        try {
            Query query = em.createNativeQuery("SELECT * FROM roles where id="+id, Role.class);
            role = (Role) query.getSingleResult();
        }catch(NoResultException nre) {
            System.out.println("No role found with id: "+id);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return role;
    }


    @Override
    public void changePassword(User user, String new_passwd) {
        try {

            em.getTransaction().begin();
            Query query = em.createNativeQuery("UPDATE users SET password = ? WHERE id = ?");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(new_passwd.getBytes());
            byte[] newPw = md.digest();
            query.setParameter(1, new_passwd);
            query.setParameter(2, user.getId());

            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

}
