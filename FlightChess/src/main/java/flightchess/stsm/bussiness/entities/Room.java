package flightchess.stsm.bussiness.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Room")
public class Room {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer RoomId;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "rstatus")
	private RoomStatus rstatus;
	@OneToMany(mappedBy="room" ,cascade=(CascadeType.ALL),fetch=FetchType.LAZY)//多的一方为主导
	private List<User> playerslist = new ArrayList<User>();
	

	public Room() {}
	
	public Integer getRoomId() {
		return this.RoomId;
	}
	
	public void setRoomStatus(RoomStatus status) {
		this.rstatus = status;
	}
	
	public RoomStatus getRoomRstatus() {
		return this.rstatus;
	}
	
	public void setPlayerslist(List<User> players) {
		this.playerslist = players;
	}
	
	public List<User> getPlayerslist() {
		return this.playerslist;
	}
}
