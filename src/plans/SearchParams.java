package plans;

import utils.Date;
import utils.Time;

public class SearchParams {
	private Date DepartureDate;
	private Time[] DepartureTime;
	private char[] DepartureAirportCode;
	private Date ArrivalDate;
	private Time[] ArrivalTime;
	private char[] ArrivalAirportCode;
	private boolean IsRoundTrip;
	private Date RDepartureDate;
	private Time[] RDepartureTime;
	private Date RArrivalDate;
	private Time[] RArrivalTime;
	private char SeatType;
	
	public void convertToGMT(){
		if(DepartureDate != null){
			//DEPARTURE
			for (int i = 0; i < DepartureTime.length; i++){
				int GMTHours = this.DepartureTime[i].getGMTHours(String.valueOf(this.DepartureAirportCode));
				if (GMTHours > 23){
					GMTHours = GMTHours - 24;
					
					if(i == 0){
						this.DepartureDate.setDay(this.DepartureDate.getDay() + 1);
					}
				}
				this.DepartureTime[i].setHours(GMTHours);
			}
		}
		else{
			//ARRIVAL
			for (int i = 0; i < ArrivalTime.length; i++){
				int GMTHours = this.ArrivalTime[i].getGMTHours(String.valueOf(this.ArrivalAirportCode));
				if (GMTHours > 23){
					GMTHours = GMTHours - 24;
					
					if(i == 0){
						this.ArrivalDate.setDay(this.ArrivalDate.getDay() + 1);
					}
				}
				this.ArrivalTime[i].setHours(GMTHours);
			}
		}
	}
	
	public void setDepartureDate(Date departureDate){
		this.DepartureDate = departureDate;
	}
	
	public void setDepartureTime(Time[] departureTime){
		this.DepartureTime = departureTime;
	}
	
	public void setDepartureAirportCode(char[] departureAirportCode){
		this.DepartureAirportCode = departureAirportCode;
	}
	
	public void setArrivalDate(Date arrivalDate){
		this.ArrivalDate = arrivalDate;
	}
	
	public void setArrivalTime(Time[] arrivalTime){
		this.ArrivalTime = arrivalTime;
	}
	
	public void setArrivalAirportCode(char[] arrivalAirportCode){
		this.ArrivalAirportCode = arrivalAirportCode;
	}
	
	public void setIsRoundTrip(boolean isRoundTrip){
		this.IsRoundTrip = isRoundTrip;
	}
	
	public void setRDepartureDate(Date rDepartureDate){
		this.RDepartureDate = rDepartureDate;
	}
	
	public void setRDepartureTime(Time[] rDepartureTime){
		this.RDepartureTime = rDepartureTime;
	}
	
	public void setRArrivalDate(Date rArrivalDate){
		this.RArrivalDate = rArrivalDate;
	}
	
	public void setRArrivalTime(Time[] rArrivalTime){
		this.RArrivalTime = rArrivalTime;
	}
	
	public void setSeatType(char seatType){
		this.SeatType = seatType;
	}
	
	
	public Date getDepartureDate(){
		return this.DepartureDate;
	}
	
	public Time[] getDepartureTime(){
		return this.DepartureTime;
	}
	
	public char[] getDepartureAirportCode(){
		return this.DepartureAirportCode;
	}
	
	public Date getArrivalDate(){
		return this.ArrivalDate;
	}
	
	public Time[] getArrivalTime(){
		return this.ArrivalTime;
	}
	
	public char[] getArrivalAirportCode(){
		return this.ArrivalAirportCode;
	}
	
	public boolean getIsRoundTrip(){
		return this.IsRoundTrip;
	}
	
	public Date getRDepartureDate(){
		return this.RDepartureDate;
	}
	
	public Time[] getRDepartureTime(){
		return this.RDepartureTime;
	}
	
	public Date getRArrivalDate(Date rArrivalDate){
		return this.RArrivalDate;
	}
	
	public Time[] getRArrivalTime(){
		return this.RArrivalTime;
	}
	
	public char getSeatType(){
		return this.SeatType;
	}
	
	//super long constructor we can copy/paste and break down later as needed.
	public SearchParams(Date departureDate, Time[] departureTime, char[] departureAirportCode, Date arrivalDate, Time[] arrivalTime,
			char[] arrivalAirportCode, boolean isRoundTrip, Date rDepartureDate, Time[] rDepartureTime, Date rArrivalDate,
			Time[] rArrivalTime, char seatType){
		
		this.DepartureDate = departureDate;
		this.DepartureTime = departureTime;
		this.DepartureAirportCode = departureAirportCode;
		this.ArrivalDate = arrivalDate;
		this.ArrivalTime = arrivalTime;
		this.ArrivalAirportCode = arrivalAirportCode;
		this.IsRoundTrip = isRoundTrip;
		this.RDepartureDate = rDepartureDate;
		this.RDepartureTime = rDepartureTime;
		this.RArrivalDate = rArrivalDate;
		this.RArrivalTime = rArrivalTime;
		this.SeatType = seatType;
	}
	
	//super long constructor we can copy/paste and break down later as needed.
	public SearchParams(Date departureDate, Time[] departureTime, char[] departureAirportCode,
			char[] arrivalAirportCode, char seatType){
		
		this.DepartureDate = departureDate;
		this.DepartureTime = departureTime;
		this.DepartureAirportCode = departureAirportCode;
		this.ArrivalAirportCode = arrivalAirportCode;
		this.SeatType = seatType;
	}
	
	
	public SearchParams(Date departureDate, char[] departureAirportCode){
		// only for the prototype
		this.DepartureDate = departureDate;
		this.DepartureTime = null;
		this.DepartureAirportCode = departureAirportCode;
		this.ArrivalDate = null;
		this.ArrivalTime = null;
		this.ArrivalAirportCode = null;
		this.IsRoundTrip = false;
		this.RDepartureDate = null;
		this.RDepartureTime = null;
		this.RArrivalDate = null;
		this.RArrivalTime = null;
		this.SeatType = '\0';
	}
	
	public SearchParams(){
		this.DepartureDate = null;
		this.DepartureTime = null;
		this.DepartureAirportCode = null;
		this.ArrivalDate = null;
		this.ArrivalTime = null;
		this.ArrivalAirportCode = null;
		this.IsRoundTrip = false;
		this.RDepartureDate = null;
		this.RDepartureTime = null;
		this.RArrivalDate = null;
		this.RArrivalTime = null;
		this.SeatType = '\0';
	}
	
	public void SetReturnParams(SearchParams outgoingParams){
		this.DepartureDate = outgoingParams.RDepartureDate;
		this.DepartureTime = outgoingParams.RDepartureTime;
		this.DepartureAirportCode = outgoingParams.ArrivalAirportCode;
		this.ArrivalDate = outgoingParams.RArrivalDate;
		this.ArrivalTime = outgoingParams.RArrivalTime;
		this.ArrivalAirportCode = outgoingParams.DepartureAirportCode;
		this.IsRoundTrip = outgoingParams.IsRoundTrip;
		this.SeatType = outgoingParams.SeatType;
	}
	
	
}
