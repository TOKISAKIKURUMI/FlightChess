package flightchess.stsm.bussiness.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flightchess.stsm.bussiness.entities.Room;
import flightchess.stsm.bussiness.entities.repositories.RoomDAO;

@Service
@Transactional
public class RoomService {
    
    @Autowired
    private RoomDAO RoomRepository; 
    
    
    public void addRoom(Room Room) {
    	RoomRepository.addRoom(Room);      
    }
 
    public void updateRoom(Room Room) {
    	RoomRepository.updateRoom(Room);
    }
 
    public Room getRoom(int id) {
        return RoomRepository.getRoom(id);
    }
 
    public void deleteRoom(int id) {
    	RoomRepository.deleteRoom(id);
    }
 
    public List<Room> getRooms() {
        return RoomRepository.getRooms();
    }
    
}