package flightchess.stsm.bussiness.entities;

public enum RoomStatus {
	WAITING("waiting"), READYING("readying"), PLAYING("playing");
	private String status;
	
	private RoomStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}
}
