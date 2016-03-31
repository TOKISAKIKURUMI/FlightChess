package flightchess.stsm.bussiness.entities.repositories;

import java.util.List;

import flightchess.stsm.bussiness.entities.Room;


public interface RoomDAO {
	public void addRoom(Room room);
    public void updateRoom(Room room);
    public Room getRoom(int id);
    public void deleteRoom(int id);
    public List<Room> getRooms();
}
