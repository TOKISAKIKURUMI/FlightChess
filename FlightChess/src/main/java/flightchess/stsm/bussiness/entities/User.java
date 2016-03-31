package flightchess.stsm.bussiness.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="User")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer UserId;
	private String UserName;
	private String Password;
	@ManyToOne(fetch=FetchType.EAGER,cascade=(CascadeType.ALL))//解决1+N,级联用ALL
	@JoinColumn(name="room_Id")
	private Room room;
	
	public User() {};
	
	public Integer getUserId() {
		return this.UserId;
	}
	
	public void setUserName(String username) {
		this.UserName = username;
	}
	
	public String getUserName() {
		return this.UserName;
	}
	
	public void setPassword(String pass) {
		this.Password = pass;
	}
	
	public String getPassword() {
		return this.Password;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public Room getRoom() {
		return this.room;
	}
	@Override
    public String toString() {
        return "User{" +
                "Id='" + UserId + '\'' +
                ", name=" + UserName +
                '}';
    }

}
