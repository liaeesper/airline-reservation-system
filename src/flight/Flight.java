package flight;

import airport.Airport;
import utils.Price;

import utils.Date;
import utils.Time;
import utils.DateTime;

public class Flight {
	//private Flights FlightGroup; //- what is this.
	private Airport DepartureAirport;
	private int FlightNumber;
	private String PlaneType;
	private int FlightTime;
	private DateTime DepartureTime;
	private Airport ArrivalAirport;
	private DateTime ArrivalTime;
	//private int FlightLength; - same as flighttime, no?
	private int SeatFc;
	private int SeatC;
	private Price PriceFc;
	private Price PriceC;
	
	//public void setFlightGroup(Flights flightGroup){
	//	this.FlightGroup = flightGroup;
	//}
	
	public void setDepartureAirport(Airport departureAirport){
		this.DepartureAirport = departureAirport;
	}
	
	public void setFlightNumber(int flightNumber){
		this.FlightNumber = flightNumber;
	}
	
	public void setPlaneType(String planeType){
		this.PlaneType = planeType;
	}
	
	public void setFlightTime(int flightTime){
		this.FlightTime = flightTime;
	}
	
	public void setDepartureTime(DateTime departureTime){
		this.DepartureTime = departureTime;
	}
	
	public void setArrivalAirport(Airport arrivalAirport){
		this.ArrivalAirport = arrivalAirport;
	}
	
	public void setArrivalTime(DateTime arrivalTime){
		this.ArrivalTime = arrivalTime;
	}
	
	//public void setFlightLength(int flightLength){
	//	this.FlightLength = flightLength;
	//}
	
	public void setSeatFc(int seatFc){
		this.SeatFc = seatFc;
	}
	
	public void setSeatC(int seatC){
		this.SeatC = seatC;
	}
	
	public void setPriceFc(Price priceFc){
		this.PriceFc = priceFc;
	}
	
	public void setPriceC(Price priceC){
		this.PriceC = priceC;
	}
	
	public Airport getDepartureAirport(){
		return this.DepartureAirport;
	}
	//public Flights getFlightGroup(){
	//	return this.FlightGroup;
	//}
	
	public int getFlightNumber(){
		return this.FlightNumber;
	}
	
	public String getPlaneType(){
		return this.PlaneType;
	}
	
	public int getFlightTime(){
		return this.FlightTime;
	}
	
	public DateTime getDepartureTime(){
		return this.DepartureTime;
	}
	
	public Airport getArrivalAirport(){
		return this.ArrivalAirport;
	}
	
	public DateTime getArrivalTime(){
		return this.ArrivalTime;
	}
	
	//public int getFlightLength(){
	//	return this.FlightLength;
	//}
	
	public int getSeatFc(){
		return this.SeatFc;
	}
	
	public int getSeatC(){
		return this.SeatC;
	}
	
	public Price getPriceFc(){
		return this.PriceFc;
	}
	
	public Price getPriceC(){
		return this.PriceC;
	}
	
	// TODO : make some is valid logic
	
	//super long constructor we can copy/paste and break down later as needed.
	public Flight(Airport departureAirport, 
			int flightNumber, 
			String planeType, 
			int flightTime, 
			DateTime departureTime, 
			Airport arrivalAirport, 
			DateTime arrivalTime, 
			int seatFc, 
			int seatC, 
			Price priceFc,
			Price priceC){
		//this.FlightGroup = flightGroup;
		this.DepartureAirport = departureAirport;
		this.FlightNumber = flightNumber;
		this.PlaneType = planeType;
		this.FlightTime = flightTime;
		this.DepartureTime = departureTime;
		this.ArrivalAirport = arrivalAirport;
		this.ArrivalTime = arrivalTime;
		//this.FlightLength = flightLength;
		this.SeatFc = seatFc;
		this.SeatC = seatC;
		this.PriceFc = priceFc;
		this.PriceC = priceC;
	}
	
	/**
	 * Convert object to printable string of format
	 * 
	 * @return the object formatted as String to display
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(FlightNumber).append(" ");
		sb.append(" From " + DepartureAirport.getName() + " to " + ArrivalAirport.getName()).append(" ");
		/*
		if(searchParams.getSeatType()  == 'F'){
			sb.append("$" + PriceFc);
		}
		else{
			sb.append("$" + PriceC);
		}
		*/
		sb.append("First Class $" + String.valueOf(PriceFc.getMoney()) + " Coach $" + String.valueOf(PriceC.getMoney()) + " ");
		
		
		sb.append(String.valueOf(DepartureTime.getTime().getHours()) + ":" + String.valueOf(DepartureTime.getTime().getMinutes()) + " ");
		sb.append(String.valueOf(DepartureTime.getDate().getDay()) + "/" + String.valueOf(DepartureTime.getDate().getMonth()) + "/" + String.valueOf(DepartureTime.getDate().getYear()) + "\n");

		return sb.toString();
	}	
}
