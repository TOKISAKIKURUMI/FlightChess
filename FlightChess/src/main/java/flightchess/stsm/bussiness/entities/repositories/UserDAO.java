package flightchess.stsm.bussiness.entities.repositories;

import java.util.List;

import flightchess.stsm.bussiness.entities.User;

public interface UserDAO {
	public void addUser(User user);
    public void updateUser(User user);
    public User getUser(int id);
    public void deleteUser(int id);
    public List<User> getUsers();
}
