package flightchess.stsm.bussiness.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flightchess.stsm.bussiness.entities.User;
import flightchess.stsm.bussiness.entities.repositories.UserDAO;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserDAO UserRepository; 
    
    
    public void addUser(User User) {
    	UserRepository.addUser(User);      
    }
 
    public void updateUser(User User) {
    	UserRepository.updateUser(User);
    }
 
    public User getUser(int id) {
        return UserRepository.getUser(id);
    }
 
    public void deleteUser(int id) {
    	UserRepository.deleteUser(id);
    }
 
    public List<User> getUsers() {
        return UserRepository.getUsers();
    }
    
}