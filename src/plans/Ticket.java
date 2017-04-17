package plans;

import flight.Flight;

public class Ticket {
	private char SeatType;
	private Flight ForFlight;
	
	public void setSeatType(char seatType){
		this.SeatType = seatType; 
	}
	
	public void setForFlight(Flight forFlight){
		this.ForFlight = forFlight;
	}
	
	
	public char getSeatType(){
		return this.SeatType;
	}
	
	public Flight getForFlight(){
		return this.ForFlight;
	}
	
	
	public Ticket(char seatType, Flight forFlight){
		this.SeatType = seatType;
		this.ForFlight = forFlight;
	}

	public Ticket() {
		this.SeatType = '\0';
		this.ForFlight = null;
	}
	
}
