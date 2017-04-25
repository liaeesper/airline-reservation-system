package plans;

import utils.Date;
import utils.Time;

/**
 * Contains user desired search criteria used for querying WPI's server for informations and in flightPlan generation
 * @param DepartureDate is the user desired date of departure. Null if ArrivalDate contains a value
 * @param DepartureTime is an array containing the range of desired departure time. Index 0 is the earliest time and index 1 is the latest time. Null if ArrivalTime contains a value
 * @param DepartureAirportCode is the 3 letter code of the desired departure airport
 * @param ArrivalDate is the user desired date of arrival. Null if DepartureDate contains a value
 * @param ArrivalTime is an array containing the range of desired arrival time. Index 0 is the earliest time and index 1 is the latest time. Null if DepartureTime contains a value
 * @param ArrivalAirportCode is the 3 letter code of the desired arrival airport
 * @param IsRoundTrip is true if the user desires a round trip flight, else false
 * @param RDepartureDate is the date of departure for the return flight in a round trip flight. Null if not round trip
 * @param RDepartureTime is an array containing the range of desired departure time for the return flight in a round trip flight. Index 0 is the earliest time and index 1 is the latest time. Null if RArrivalTime contains a value or if not round trip
 * @param RArrivalDate is the date of arrival for the return flight in a round trip flight. Null if not round trip
 * @param RArrivalTime is an array containing the range of desired arrival time for the return flight in a round trip flight. Index 0 is the earliest time and index 1 is the latest time. Null if RDepartureTime contains a value or if not round trip
 * @param SeatType is the desired seating type. Either first class or coach.
 * @author Team G
 *
 */
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
	
	/**
	 * Converts the local time retrieved from the user to GMT time. GMT time is used for querying the server
	 * for flight information and for flightPlan generation
	 */
	public void convertToGMT(){
		if(DepartureDate != null){
			//DEPARTURE OUTGOING
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
		else if (ArrivalDate != null){
			//ARRIVAL OUTGOING
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
		if(RDepartureDate != null){
			//DEPARTURE RETURN
			for (int i = 0; i < RDepartureTime.length; i++){
				int GMTHours = this.RDepartureTime[i].getGMTHours(String.valueOf(this.ArrivalAirportCode));
				if (GMTHours > 23){
					GMTHours = GMTHours - 24;
					
					if(i == 0){
						this.RDepartureDate.setDay(this.RDepartureDate.getDay() + 1);
					}
				}
				this.RDepartureTime[i].setHours(GMTHours);
			}
		}
		else if (RArrivalDate != null){
			//ARRIVAL RETURN
			for (int i = 0; i < RArrivalTime.length; i++){
				int GMTHours = this.RArrivalTime[i].getGMTHours(String.valueOf(this.DepartureAirportCode));
				if (GMTHours > 23){
					GMTHours = GMTHours - 24;
					
					if(i == 0){
						this.RArrivalDate.setDay(this.RArrivalDate.getDay() + 1);
					}
				}
				this.RArrivalTime[i].setHours(GMTHours);
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
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		StringBuilder dA = new StringBuilder();
		StringBuilder aA = new StringBuilder();
		String aAirport, dAirport;
		
		Time Time1, Time2;
		Date specifiedDate;
		
		dAirport = dA.append(DepartureAirportCode).toString();
		aAirport = aA.append(ArrivalAirportCode).toString();
		
		if(DepartureDate != null){
			Time1 = new Time(DepartureTime[0].getHours(), DepartureTime[0].getMinutes());
			Time2 = new Time(DepartureTime[1].getHours(), DepartureTime[1].getMinutes());
			specifiedDate = new Date(DepartureDate.getDay(), DepartureDate.getMonth(), DepartureDate.getYear());
			sb.append("Searched by Departure Date/Time\n");
		}
		else{
			Time1 = new Time(ArrivalTime[0].getHours(), ArrivalTime[0].getMinutes());
			Time2 = new Time(ArrivalTime[1].getHours(), ArrivalTime[1].getMinutes());
			specifiedDate = new Date(ArrivalDate.getDay(), ArrivalDate.getMonth(), ArrivalDate.getYear());
			sb.append("Searched by Arrival Date/Time\n");
		}
		
		sb.append(dAirport + "->" + aAirport + "\n");
		
		sb.append(String.valueOf(specifiedDate.getMonth()) + "/" + String.valueOf(specifiedDate.getDay()) + "/" + String.valueOf(specifiedDate.getYear()) + "\n");
		sb.append(String.valueOf(Time1.getHours()) + ":" + String.valueOf(Time1.getMinutes()));
		sb.append(" to " + String.valueOf(Time2.getHours()) + ":" + String.valueOf(Time2.getMinutes()));
		
		sb.append("\nSeat Type: " + SeatType);
		sb.append("\nIs Round Trip: " + IsRoundTrip + "\n\n");
		
		return sb.toString();
	}
}
