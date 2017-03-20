package flight;

import airport.Airport;
import utils.Price;
import utils.Date;

public class Flight {
	//private Flights FlightGroup; //- what is this.
	private String DepartureAirport;
	private int FlightNumber;
	private String PlaneType;
	private int FlightTime;
	private Date DepartureTime;
	private Airport ArrivalAirport;
	private Date ArrivalTime;
	private int FlightLength;
	private int SeatFc;
	private int SeatC;
	private Price PriceFc;
	private Price PriceC;
	
	//public void setFlightGroup(Flights flightGroup){
	//	this.FlightGroup = flightGroup;
	//}
	
	public void setDepartureAirport(String departureAirport){
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
	
	public void setDepartureTime(Date departureTime){
		this.DepartureTime = departureTime;
	}
	
	public void setArrivalAirport(Airport arrivalAirport){
		this.ArrivalAirport = arrivalAirport;
	}
	
	public void setArrivalTime(Date arrivalTime){
		this.ArrivalTime = arrivalTime;
	}
	
	public void setFlightLength(int flightLength){
		this.FlightLength = flightLength;
	}
	
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
	
	public String getDepartureAirport(){
		return this.DepartureAirport;
	}
	//public Flights getFlightGroup(){
	//	return this.FlightGroup;
	//}
	
	public int getFlightNumber(int flightNumber){
		return this.FlightNumber;
	}
	
	public String getPlaneType(){
		return this.PlaneType;
	}
	
	public int getFlightTime(){
		return this.FlightTime;
	}
	
	public Date getDepartureTime(){
		return this.DepartureTime;
	}
	
	public Airport getArrivalAirport(){
		return this.ArrivalAirport;
	}
	
	public Date getArrivalTime(){
		return this.ArrivalTime;
	}
	
	public int getFlightLength(){
		return this.FlightLength;
	}
	
	public int getSeatFc(int seatFc){
		return this.SeatFc;
	}
	
	public int getSeatC(int seatC){
		return this.SeatC;
	}
	
	public Price getPriceFc(Price priceFc){
		return this.PriceFc;
	}
	
	public Price getPriceC(){
		return this.PriceC;
	}
	
	// TODO : make some isvalid logic
	
	//super long constructor we can copy/paste and break down later as needed.
	public Flight(Flights flightGroup, int flightNumber, int planeType, int flightTime, Time departureTime, 
			Airport arrivalAirport, Time arrivalTime, int flightLength, int seatFc, int seatC, Price priceFc,
			Price priceC){
		this.FlightGroup = flightGroup;
		this.FlightNumber = flightNumber;
		this.PlaneType = planeType;
		this.FlightTime = flightTime;
		this.DepartureTime = departureTime;
		this.ArrivalAirport = arrivalAirport;
		this.ArrivalTime = arrivalTime;
		this.FlightLength = flightLength;
		this.SeatFc = seatFc;
		this.SeatC = seatC;
		this.PriceFc = priceFc;
		this.PriceC = priceC;
	}
	
}
