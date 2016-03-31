package flightchess.stsm.bussiness.entities.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import flightchess.stsm.bussiness.entities.Room;

@Repository
public class RoomDAOImpl implements RoomDAO {
 
    @Autowired
    private SessionFactory sessionFactory;
 
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
 
    public void addRoom(Room room) {
        getCurrentSession().save(room);
    }
 
    public void updateRoom(Room room) {
    	Room roomToUpdate = getRoom(room.getRoomId());
    	roomToUpdate.setRoomStatus(room.getRoomRstatus());
    	roomToUpdate.setPlayerslist(room.getPlayerslist());
        getCurrentSession().update(roomToUpdate);
 
    }
 
    public Room getRoom(int id) {
    	Room room = (Room) getCurrentSession().get(Room.class, id);
        return room;
    }
 
	public void deleteRoom(int id) {
    	Room room = getRoom(id);
        if (room != null)
            getCurrentSession().delete(room);
		
	}
 
    @SuppressWarnings("unchecked")
	public List<Room> getRooms() {
	        return getCurrentSession().createQuery("from Room").list();
	}

}
