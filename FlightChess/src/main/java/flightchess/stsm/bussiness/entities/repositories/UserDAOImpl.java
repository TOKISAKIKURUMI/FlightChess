package flightchess.stsm.bussiness.entities.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import flightchess.stsm.bussiness.entities.User;

@Repository
public class UserDAOImpl implements UserDAO {
 
    @Autowired
    private SessionFactory sessionFactory;
 
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
 
    public void addUser(User user) {
        getCurrentSession().save(user);
    }
 
    public void updateUser(User user) {
    	User userToUpdate = getUser(user.getUserId());
    	userToUpdate.setUserName(user.getUserName());
    	userToUpdate.setPassword(user.getPassword());
    	userToUpdate.setRoom(user.getRoom());
        getCurrentSession().update(userToUpdate);
 
    }
 
    public User getUser(int id) {
    	User user = (User) getCurrentSession().get(User.class, id);
        return user;
    }
 
	public void deleteUser(int id) {
		User user = getUser(id);
        if (user != null)
            getCurrentSession().delete(user);
		
	}
 
    @SuppressWarnings("unchecked")
	public List<User> getUsers() {
	        return getCurrentSession().createQuery("from User").list();
	}

}
