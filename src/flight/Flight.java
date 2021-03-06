package flight;

import airport.Airport;
import utils.Price;

import utils.DateTime;

/**
 * Contains information about a single flight
 * @param DepartureAirport is the airport that the flight is departing from
 * @param FlightNumber is the flight number used as an identifier in WPI's server
 * @param PlaneType is the type of plane that the flight is using. Necessary for seating information
 * @param FlightTime is the total amount of time a flight takes from departure to arrival
 * @param DepartureTime contains the date and time of departure
 * @param ArrivalAirport is the airport that the flight is arriving at
 * @param ArrivalTime is the airport that the flight is arriving at
 * @param SeatFc is the number of first class seats available on the flight
 * @param SeatC is the number of coach seats available on the flight
 * @param PriceFc is the cost of reserving a first class seat
 * @param PriceC is the cost of reserving a coach seat
 * @author Team G
 *
 */
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
	
	public Flight() {
	}
	
	//written because of location based issues in FlightPlansGenerator
	public Flight(Flight f) {
		
		Airport departureAirport = f.DepartureAirport;
		int flightNumber = f.FlightNumber;
		String planeType = f.PlaneType;
		int flightTime = f.FlightTime;
		DateTime departureTime = f.DepartureTime;
		Airport arrivalAirport = f.ArrivalAirport;
		DateTime arrivalTime = f.ArrivalTime;
		int seatFc = f.SeatFc;
		int seatC = f.SeatC;
		Price priceFc = f.PriceFc;
		Price priceC = f.PriceC;
		
		this.DepartureAirport = departureAirport;
		this.FlightNumber = flightNumber;
		this.PlaneType = planeType;
		this.FlightTime = flightTime;
		this.DepartureTime = departureTime;
		this.ArrivalAirport = arrivalAirport;
		this.ArrivalTime = arrivalTime;
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
		
		//Get departure time and day data
		int localHours_DEP = DepartureTime.getTime().getLocalHours(this.DepartureAirport.getCode());
		String timeZone_DEP = DepartureTime.getTime().getTimeZone(this.DepartureAirport.getCode());
		int day_DEP = DepartureTime.getDate().getDay();
		String month = String.valueOf(DepartureTime.getDate().getMonth());
		String year = String.valueOf(DepartureTime.getDate().getYear());
		
		//Handle case where time conversion causes day change
		if (localHours_DEP < 0){
			localHours_DEP = 24 + localHours_DEP;
			day_DEP--;
		}
		
		//Append Flight number, departure airport, arrival airport, and date to the string buffer
		sb.append(FlightNumber).append(" ");
		sb.append(DepartureAirport.getName() + " (" + DepartureAirport.getCode() + ") -> " + ArrivalAirport.getName() + " (" + ArrivalAirport.getCode() + ")\n");
		
		
		sb.append(month + "/" + day_DEP + "/" + year + " ");
		
		//Append time in 12 hour format to string buffer
		if(localHours_DEP%12 == 0){
			sb.append("12:");
		}
		else{
			sb.append(localHours_DEP%12 + ":");
		}
		
		
		if(DepartureTime.getTime().getMinutes() < 10){
			sb.append("0");
		}
		
		
		sb.append(String.valueOf(DepartureTime.getTime().getMinutes()));
		
		if(localHours_DEP >= 12){
			sb.append("pm ");
		}
		else{
			sb.append("am ");
		}
		
		sb.append(timeZone_DEP + " to ");
		
		//Get arrival flight data
		int localHours_ARR = ArrivalTime.getTime().getLocalHours(this.ArrivalAirport.getCode());
		int day_ARR = ArrivalTime.getDate().getDay();
		String timeZone_ARR = ArrivalTime.getTime().getTimeZone(this.ArrivalAirport.getCode());
		
		//Handle case where time conversion causes day change
				if (localHours_ARR < 0){
					localHours_ARR = 24 + localHours_ARR;
					day_ARR--;
				}
		
		sb.append(month + "/" + day_ARR + "/" + year + " ");
		
		if(localHours_ARR%12 == 0){
			sb.append("12:");
		}
		else{
			sb.append(localHours_ARR%12 + ":");
		}
		
		
		if(ArrivalTime.getTime().getMinutes() < 10){
			sb.append("0");
		}
		
		
		sb.append(String.valueOf(ArrivalTime.getTime().getMinutes()));
		
		if(localHours_ARR >= 12){
			sb.append("pm ");
		}
		else{
			sb.append("am ");
		}
		
		sb.append(timeZone_ARR + "\n");
		
		
		
		sb.append("Coach $" + String.valueOf(PriceC.getMoney()) + "\nFirst Class $" + String.valueOf(PriceFc.getMoney()) + "\n");
		
		
		
		return sb.toString();
	}
	
	
	
}