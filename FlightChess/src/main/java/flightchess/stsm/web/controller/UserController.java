package flightchess.stsm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import flightchess.stsm.bussiness.entities.Room;
import flightchess.stsm.bussiness.entities.RoomStatus;
import flightchess.stsm.bussiness.entities.User;
import flightchess.stsm.bussiness.services.RoomService;
import flightchess.stsm.bussiness.services.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private RoomService roomservice;
	
	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json",
			params = {"UserId", "password"})
	@ResponseBody
	public Callable<Object> UserLogin(final HttpServletRequest req) {
		
		return new Callable<Object>() {
		       public Object call() throws Exception {
			      Integer UId = Integer.parseInt(req.getParameter("UserId"));
			      User loginUser = userservice.getUser(UId);
			      Map<String, String> error = new HashMap<String, String>();
			      if (loginUser != null) {
				      if (loginUser.getPassword().equals(req.getParameter("password"))) {
					      loginUser.setRoom(null);
					      return loginUser;
				      } else {
					     return error.put("msg", "password_error");
				      }
			      } else {
				     return error.put("msg", "no_such_user");
			      }
		      }
		};
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST, produces="application/json",
			params = {"UserName", "password"})
	@ResponseBody
	public Callable<Object> UserRegister(final HttpServletRequest req) {
		
		return new Callable<Object>() {

			public Object call() throws Exception {
				// TODO Auto-generated method stub
				Map<String, String>error = new HashMap<String, String>();
				List<User> users = userservice.getUsers();
				for (User u : users) {
					if (u.getUserName().equals(req.getParameter("UserName"))) {
						return error.put("msg", "user has alerdy existed");
					}
				}
				User user = new User();
				user.setUserName(req.getParameter("UserName"));
				user.setPassword("password");
				user.setRoom(null);
				userservice.addUser(user);
				return user;
			}
		};
	}
	
	
	@RequestMapping(value="/createRoom", method=RequestMethod.POST, produces="application/json",
			params = {"UserId"})
	@ResponseBody
	public Callable<Object> CreateRoom(final HttpServletRequest req) {
		
		return new Callable<Object>() {

			public Object call() throws Exception {
				// TODO Auto-generated method stub
				User user = userservice.getUser(Integer.parseInt(req.getParameter("UserId")));
				if (user.getRoom() != null) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("msg", "you are alerdy in room");
					return m;
				}
				Room room = new Room();
				room.setRoomStatus(RoomStatus.WAITING);
				List<User> list = new ArrayList<User>();
				list.add(user);
				user.setRoom(room);
				room.setPlayerslist(list);
			    roomservice.addRoom(room);
			    return room;
			}
		};
	}
	
	@RequestMapping(value="/queryRoom", method=RequestMethod.GET)
	@ResponseBody
	public Callable<Object[]> QueryRoom() {
		System.out.println("线程名称："+Thread.currentThread().getName());
		return new Callable<Object[]>() {

			public Object[] call() throws Exception {
				// TODO Auto-generated method stub
				System.out.println("线程名称："+Thread.currentThread().getName());
				List<Room> list = roomservice.getRooms();
				Room[] result = null;
				if (list.size() > 0) {
				    result = new Room[list.size()];
				}
				int index = 0;
				for (Room room : list) {
					result[index++] = room;
				}
				return result;
			}
			
		};
	}
	
	@RequestMapping(value="/joinRoom", method=RequestMethod.POST, produces="application/json",
			params = {"UserId", "RoomId"})
	@ResponseBody
	public Callable<Object> JoinInRoom(final HttpServletRequest req) {
		return new Callable<Object>() {

			public Object call() throws Exception {
				// TODO Auto-generated method stub
				Integer UserId = Integer.parseInt(req.getParameter("UserId"));
				Integer RoomId = Integer.parseInt(req.getParameter("RoomId"));
				User user = userservice.getUser(UserId);
				Room room = roomservice.getRoom(RoomId);
				Map<String, String> map = new HashMap<String, String>();
				if (user.getRoom() != null) {
					map.put("msg", "you are alerdy in room");
					return map;
				}
				if (room.getPlayerslist().size() == 4) {
					map.put("msg", "Room is full");
					return map;
				}
				if (room.getRoomRstatus() == RoomStatus.PLAYING || room.getRoomRstatus() == RoomStatus.READYING) {
					map.put("msg", "Can't join room now");
					return map;
				}
				
				user.setRoom(room);
				List<User> players = room.getPlayerslist();
				players.add(user);
				if (players.size() == 4) {
					room.setRoomStatus(RoomStatus.READYING);
				}

				room.setPlayerslist(players);
				return room;
			}
		};
	}
}
