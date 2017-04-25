package dao;

import java.math.BigDecimal;
import java.util.ArrayList;

import flight.Flight;
import flight.Flights;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.SearchParams;
import plans.Ticket;
import utils.Date;
import utils.DateTime;
import utils.Price;
import utils.Time;


/**
 * Searches and calculates valid flight plans given search criteria via a SearchParam object
 * 
 * @author Team G
 *
 */
public class FlightPlansGenerator {
	
	/**
	 * Finds the time between DateTime a and b in minutes
	 * Assumes that DateTime a and b are no longer than 24 hours apart
	 * @param a
	 * @param b
	 * @return returns the time in minutes between DateTime a and b
	 */
	public int getTimeBetween(DateTime a, DateTime b){
		int aMinutes = a.getTime().getTimeInMinutes();
		int bMinutes = b.getTime().getTimeInMinutes();
		
		//Add 24 hours to b's minutes if b's time of day takes place at or before a's
		if(aMinutes >= bMinutes){
			bMinutes += 24*60;
		}
		
		return bMinutes - aMinutes;
		
	}
	
	/*
	 * Gets the time in minutes between the arrival time of the previous flight leg and the departure time of the next flight
	 * If the layover time is invalid (not between 30 minutes and 4 hours) it returns a -1 as to act like a boolean
	 * @prevArrival DateTime of the previous flight leg's arrival time
	 * @nextDeparture DateTime of the next flight leg's departure time
	 * @return either returns valid layover time (in minutes) or a -1 to indicate invalid layover time
	 * 
	 */
	public int getLayoverTime(DateTime prevArrival, DateTime nextDeparture){
		int layoverTime = 0;
		Date pdate = prevArrival.getDate();
		
		int aMinutes = prevArrival.getTime().getTimeInMinutes();
		int bMinutes = nextDeparture.getTime().getTimeInMinutes();
		
		//increments the date of prevArrival by a day if it sees that nextDeparture's time of day takes place on or before prevArrival's
		//also updates the this change for later comparison purposes
		//this can occur because of layovers occurring over midnight
		if(aMinutes >= bMinutes){
			pdate = prevArrival.getDate().IncrementDate();
			bMinutes += 24*60;
		}
		
		//calculates layover time
		layoverTime = bMinutes - aMinutes;
		
		//checks if the layover time is between 30 minutes and 4 hours
		if(layoverTime >= 30 && layoverTime <= 4*60){
			
			//makes sure the dates are correct
			if(pdate.getDay() == nextDeparture.getDate().getDay() &&
					pdate.getMonth() == nextDeparture.getDate().getMonth() &&
					pdate.getYear() == nextDeparture.getDate().getYear()){
				return layoverTime;
			}
			
		}
		
		return -1;		
	}
	
	
	/**
	 * Determines if there is available seating for a flight
	 * @param flight given flight to check the seating availability of
	 * @param seatType seating type to check the availability of
	 * @return Returns true if there is available seating, false if not
	 */
	public boolean EnoughSeats(Flight flight, char seatType){
		
		int numSeats;

		if(seatType == 'c'){
			
			numSeats = flight.getSeatC();
		}
		else{
			numSeats = flight.getSeatFc();
		}
		
		if(numSeats > 0){
			return true;
		}
		else{
			return false;
		}
				
	}

	/**
	 * gets the duration time of a flight
	 * @param flight to get the duration of
	 * @return the duration time of a flight in minutes
	 */
	public int getFlightDuration(Flight flight){
		return getTimeBetween(flight.getDepartureTime(), flight.getArrivalTime());
	}
	
	/**
	 * Checks that a "previous" flight has already landed by the time the next departure occurs
	 * Assumes the DateTimes are within the same month and year
	 * @prevArrival DateTime of the previous flight leg's arrival time
	 * @nextDeparture DateTime of the next flight leg's departure time
	 * @return true if prevArrival occurred before nextDeparture, false if not
	 */
	public boolean HappenedAfter(DateTime prevArrival, DateTime nextDeparture){
		
		if(nextDeparture.getDate().getDay() > prevArrival.getDate().getDay()){
			return true;
		}
		else if(nextDeparture.getDate().getDay() < prevArrival.getDate().getDay()){
			return false;
		}
		else{
			if(nextDeparture.getTime().getHours() > prevArrival.getTime().getHours()){
				return true;
			}
			else if(nextDeparture.getTime().getHours() < prevArrival.getTime().getHours()){
				return false;
			}
			else{
				if(nextDeparture.getTime().getMinutes() > prevArrival.getTime().getMinutes()){
					return true;
				}
				else if(nextDeparture.getTime().getMinutes() < prevArrival.getTime().getMinutes()){
					return false;
				}
			}
		}
		return true;//dates are equal
	}
	
	
	/**
	 * Filters the a given flight list so it gets a valid list of flight legs
	 * Makes sure that one a previous leg did not occur before another, that layover time is valid, 
	 * that there is enough seats of the specified seat type, and that there is no backtracking airports
	 * @param originPlan Plan that yielded the SearchParams that was used to get the unfiltered list
	 * @param unfiltered list of possible flight legs that occur within a proximity of originPlan
	 * @param level of search the system is in in terms of how many flight legs have been found for originPlan already
	 * @param type of search that is going on (by arrival date or departure date)
	 * @return a list of valid flight legs
	 */
	public ArrayList<Flight> FilterFlightsList(FlightPlan originPlan, ArrayList<Flight> unfiltered, int level, char type){
		ArrayList<Flight> filtered = new ArrayList<Flight>();
		DateTime arrival = null, departure = null;
		String dAirport = null, aAirport = null;
		
		//if searching by departure date
		if (type == 'd'){
			//departure airport is the originally specified departure airport in the whole search (the one the user specified, or arrival airport if this is a returning flight search)
			dAirport = originPlan.getLegs().get(0).getForFlight().getDepartureAirport().getCode();
			
			//arrival time is when the previous flight leg landed (the last flight leg that was added to the originPlan)
			arrival = originPlan.getLegs().get(level - 1).getForFlight().getArrivalTime();
		}
		//if searching by arrival date
		else{
			//arrival airport is the originally specified arrival airport in the whole search (the one the user specified, or departure airport if this is a returning flight search)
			aAirport = originPlan.getLegs().get(level - 1).getForFlight().getArrivalAirport().getCode();
			
			//departure time is when the previous flight leg landed (the last flight leg that was added to the originPlan)
			departure = originPlan.getLegs().get(0).getForFlight().getDepartureTime();
		}
		
		for(Flight newF: unfiltered){
			if(type == 'd'){
				//arrival airport of the next potential flight leg to test for backtracking
				aAirport = newF.getArrivalAirport().getCode();
				
				//departure time of the next potential flight leg
				departure = newF.getDepartureTime();
			}
			else{
				//departure airport of the next potential flight leg to test for backtracking
				dAirport = newF.getDepartureAirport().getCode();
				
				//departure time of the previous potential flight leg
				arrival = newF.getArrivalTime();
			}
			
			//checks is valid by checking that the potential leg is occuring in order, has a valid layover time, enough seats, and doesn't backtrack
			if(HappenedAfter(arrival, departure) && getLayoverTime(arrival, departure) != -1 && EnoughSeats(newF, originPlan.getLegs().get(0).getSeatType()) && !dAirport.equals(aAirport)){
				
				//adds it to the filtered list if passes
				filtered.add(new Flight(newF));				
			}		
		}
		
		//returns filtered list
		return filtered;
	}
	
	
	
	public ArrayList<ArrayList<FlightPlan>> FindInitialLists(SearchParams uParams){
		ServerInterface serverInterface = new ServerInterface();
		Flights searchResults = new Flights();
		
		searchResults.setFlightList(serverInterface.GetDepartingFlights(uParams).getFlightList());
		SearchParams tempParams = new SearchParams();
		ArrayList <Flight> tempSResults;
		
		
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		
		int flightDuration, dTime;
		ArrayList<Ticket> tempTicket;
		
		int dTime1 = uParams.getDepartureTime()[0].getTimeInMinutes();
		int dTime2 = uParams.getDepartureTime()[1].getTimeInMinutes();
		
		Date departureDate = new Date(uParams.getDepartureDate().getDay(), uParams.getDepartureDate().getMonth(), uParams.getDepartureDate().getYear());
		Date nextDay = departureDate.IncrementDate();
		
		int mTime = dTime2;
		
		if(dTime1 > dTime2){
			mTime = 24*60 - 1;
			
			tempParams.setArrivalAirportCode(uParams.getArrivalAirportCode());
			tempParams.setDepartureAirportCode(uParams.getDepartureAirportCode());
			tempParams.setDepartureDate(nextDay);
			
			tempSResults = new ArrayList<Flight>();
			tempSResults.addAll(searchResults.getFlightList());
			tempSResults.addAll(serverInterface.GetDepartingFlights(tempParams).getFlightList());
			searchResults.setFlightList(tempSResults);	
			
		}
		
		char[] tempAirportCode;
		char[] aAirportCode = uParams.getArrivalAirportCode();
		
		boolean firstDayLegal = false;
		boolean secondDayLegal = false;
		
		for(Flight possibleLeg1 : searchResults.getFlightList()){
			
			tempTicket = new ArrayList<Ticket>();
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			
			dTime = possibleLeg1.getDepartureTime().getTime().getTimeInMinutes();
			
			firstDayLegal = false;
			secondDayLegal = false;
			
			if(departureDate.getDay() == possibleLeg1.getDepartureTime().getDate().getDay()){
				firstDayLegal = true;
			}
			else if(nextDay.getDay() == possibleLeg1.getDepartureTime().getDate().getDay()){
				secondDayLegal = true;
			}
			
			if(EnoughSeats(possibleLeg1, uParams.getSeatType()) && ((dTime >= dTime1 && dTime <= mTime && firstDayLegal) || (dTime >= 0 && dTime <= dTime2 && secondDayLegal))){
				
				tempTicket.get(0).setForFlight(possibleLeg1);
				tempTicket.get(0).setSeatType(uParams.getSeatType());
				
				
				flightDuration = getFlightDuration(possibleLeg1);
				tempAirportCode = possibleLeg1.getArrivalAirport().getCode().toCharArray();
				
				if(aAirportCode[0] == tempAirportCode[0] && aAirportCode[1] == tempAirportCode[1] && aAirportCode[2] == tempAirportCode[2]){
					if(uParams.getSeatType() == 'c'){
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}					
				}
				else{
					if(uParams.getSeatType() == 'c'){
						unconcludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						unconcludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}		
				}
			}
		}
		
		
		ArrayList<ArrayList<FlightPlan>> List = new ArrayList<ArrayList<FlightPlan>>();
		List.add(concludedList);
		List.add(unconcludedList);
		return List;
	}
	
	public ArrayList<FlightPlan> GenerateFlightLegs(FlightPlan unconcluded, SearchParams userParams, int level){
		ServerInterface serverInterface = new ServerInterface();
		ServerInterface serverInterface2 = new ServerInterface();
		int aTime, dTime;
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		Flights searchResults = new Flights();
		//SearchParams tempParams = userParams;
		SearchParams tempParams = new SearchParams();
		SearchParams tempParams2 = new SearchParams();
		int flightDuration;
		ArrayList<Ticket> tempTicket;
		FlightPlan tempNewFlightPlan;
		BigDecimal price;// = new BigDecimal(0.00);

		char[] tempAirportCode;
		char[] aAirportCode = userParams.getArrivalAirportCode();
		
		ArrayList<Flight> filteredListLeg = new ArrayList<Flight>();
		

		ArrayList<Flight> tempSResults = new ArrayList<Flight>();
		int fTime, lTime;
		
		tempParams.setSeatType(userParams.getSeatType());
		tempParams.setArrivalAirportCode(userParams.getArrivalAirportCode());
		tempParams.setDepartureAirportCode(unconcluded.getLegs().get(level - 1).getForFlight().getArrivalAirport().getCode().toCharArray());
		tempParams.setDepartureDate(unconcluded.getLegs().get(level - 1).getForFlight().getArrivalTime().getDate());
		aTime = unconcluded.getLegs().get(level - 1).getForFlight().getArrivalTime().getTime().getTimeInMinutes();
		dTime = aTime + 4*60;
		
		searchResults.setFlightList((serverInterface.GetDepartingFlights(tempParams).getFlightList()));
		
		
		//layover over midnight
		if(dTime >= 24*60){
			tempParams2.setArrivalAirportCode(userParams.getArrivalAirportCode());
			tempParams2.setDepartureAirportCode(unconcluded.getLegs().get(level - 1).getForFlight().getArrivalAirport().getCode().toCharArray());
			tempParams2.setDepartureDate(tempParams.getDepartureDate().IncrementDate());
			tempSResults = new ArrayList<Flight>();
			tempSResults.addAll(searchResults.getFlightList());
			tempSResults.addAll(serverInterface2.GetDepartingFlights(tempParams2).getFlightList());
			searchResults.setFlightList(tempSResults);			
		}
		
		filteredListLeg = FilterFlightsList(unconcluded, searchResults.getFlightList(), level, 'd');
		
		for(Flight possibleLeg : filteredListLeg){
			
			//to calculate total flight duration
			fTime = getFlightDuration(possibleLeg);
			lTime = getLayoverTime(unconcluded.getLegs().get(level - 1).getForFlight().getArrivalTime(), possibleLeg.getDepartureTime());
			
			//to test if it is arrival airport
			tempAirportCode = possibleLeg.getArrivalAirport().getCode().toCharArray();
			
			tempTicket = new ArrayList<Ticket>();
			tempTicket.add(new Ticket(unconcluded.getLegs().get(0).getSeatType(), unconcluded.getLegs().get(0).getForFlight()));
			
			price = new BigDecimal(0.0);
			
			if(level == 1){
				tempTicket.add(new Ticket(userParams.getSeatType(), possibleLeg));
				tempTicket.add(new Ticket());
				
				if(userParams.getSeatType() == 'c'){
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceC().getMoney());
					price = price.add(possibleLeg.getPriceC().getMoney());
					
				}
				else{
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceFc().getMoney());
					price = price.add(possibleLeg.getPriceFc().getMoney());
				}	
				
			}
			else{
				tempTicket.add(new Ticket(unconcluded.getLegs().get(1).getSeatType(), unconcluded.getLegs().get(1).getForFlight()));
				tempTicket.add(new Ticket(userParams.getSeatType(), possibleLeg));
				
				if(userParams.getSeatType() == 'c'){
					
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceC().getMoney());
					price = price.add(unconcluded.getLegs().get(1).getForFlight().getPriceC().getMoney());
					price = price.add(possibleLeg.getPriceC().getMoney());
				}
				else{
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceFc().getMoney());
					price = price.add(unconcluded.getLegs().get(1).getForFlight().getPriceFc().getMoney());
					price = price.add(possibleLeg.getPriceFc().getMoney());
				}	
			}
			
			
			flightDuration = unconcluded.getTotalTime() + lTime + fTime;
			
			if(aAirportCode[0] == tempAirportCode[0] && aAirportCode[1] == tempAirportCode[1] && aAirportCode[2] == tempAirportCode[2]){
				
				concludedList.add(new FlightPlan(level + 1, new Price(price), flightDuration, tempTicket));	
				
			}
			else if(level == 1){
				
				tempNewFlightPlan = new FlightPlan(level + 1, new Price(price), flightDuration, tempTicket);
				concludedList.addAll(GenerateFlightLegs(tempNewFlightPlan, tempParams, 2));
				
			}

		}
		
		return concludedList;
		
	}
	
	public FlightPlans GenerateFlightPlansD(SearchParams userParams){
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		ArrayList<ArrayList<FlightPlan>> InitialLists = new ArrayList<ArrayList<FlightPlan>>();

		
		//leg 1
		InitialLists = FindInitialLists(userParams);
		concludedList = InitialLists.get(0);
		unconcludedList = InitialLists.get(1);
		
		
		//other legs
		for(FlightPlan unconcluded : unconcludedList){
			concludedList.addAll(GenerateFlightLegs(unconcluded, userParams, 1));
		}
		
		
		return new FlightPlans(concludedList);
	}
	
	
	

	public ArrayList<ArrayList<FlightPlan>> FindInitialListsA(SearchParams uParams){
		ServerInterface serverInterface = new ServerInterface();
		Flights searchResults = new Flights();
		
		ArrayList<Flight> failed = new ArrayList<Flight>();
		
		searchResults.setFlightList(serverInterface.GetArrivingFlights(uParams).getFlightList());
		SearchParams tempParams = new SearchParams();
		ArrayList <Flight> tempSResults;
		
		
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		
		int flightDuration, aTime;
		ArrayList<Ticket> tempTicket;
		
		int aTime1 = uParams.getArrivalTime()[0].getTimeInMinutes();
		int aTime2 = uParams.getArrivalTime()[1].getTimeInMinutes();
		
		Date arrivalDate = new Date(uParams.getArrivalDate().getDay(), uParams.getArrivalDate().getMonth(), uParams.getArrivalDate().getYear());
		Date nextDay = arrivalDate.IncrementDate();
		
		int mTime = aTime2;
		
		if(aTime1 > aTime2){
			mTime = 24*60 - 1;
			
			tempParams.setArrivalAirportCode(uParams.getArrivalAirportCode());
			tempParams.setDepartureAirportCode(uParams.getDepartureAirportCode());
			tempParams.setArrivalDate(nextDay);
			
			tempSResults = new ArrayList<Flight>();
			tempSResults.addAll(searchResults.getFlightList());
			tempSResults.addAll(serverInterface.GetArrivingFlights(tempParams).getFlightList());
			searchResults.setFlightList(tempSResults);
			
		}
		
		char[] tempAirportCode;
		char[] dAirportCode = uParams.getDepartureAirportCode();
		
		boolean firstDayLegal = false;
		boolean secondDayLegal = false;
		
		for(Flight possibleLeg1 : searchResults.getFlightList()){
			
			tempTicket = new ArrayList<Ticket>();
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			
			aTime = possibleLeg1.getArrivalTime().getTime().getTimeInMinutes();
			
			firstDayLegal = false;
			secondDayLegal = false;
			
			if(arrivalDate.getDay() == possibleLeg1.getArrivalTime().getDate().getDay()){
				firstDayLegal = true;
			}
			else if(nextDay.getDay() == possibleLeg1.getArrivalTime().getDate().getDay()){
				secondDayLegal = true;
			}
			
			if(EnoughSeats(possibleLeg1, uParams.getSeatType()) && ((aTime >= aTime1 && aTime <= mTime && firstDayLegal) || (aTime >= 0 && aTime <= aTime2 && secondDayLegal))){
				
				tempTicket.get(0).setForFlight(possibleLeg1);
				tempTicket.get(0).setSeatType(uParams.getSeatType());
				
				
				flightDuration = getFlightDuration(possibleLeg1);
				tempAirportCode = possibleLeg1.getDepartureAirport().getCode().toCharArray();
				
				if(dAirportCode[0] == tempAirportCode[0] && dAirportCode[1] == tempAirportCode[1] && dAirportCode[2] == tempAirportCode[2]){
					if(uParams.getSeatType() == 'c'){
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}					
				}
				else{
					if(uParams.getSeatType() == 'c'){
						unconcludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						unconcludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}		
				}
			}
			
			//comment out when not testing
			else{
				failed.add(possibleLeg1);
			}
		}
		
		//comment out when not testing
		//TestStringInitial(uParams, unconcludedList, failed, concludedList, 'a', searchResults.getFlightList().size());
		
		ArrayList<ArrayList<FlightPlan>> List = new ArrayList<ArrayList<FlightPlan>>();
		List.add(concludedList);
		List.add(unconcludedList);
		return List;
	}
	
	public ArrayList<FlightPlan> GenerateFlightLegsA(FlightPlan unconcluded, SearchParams userParams, int level){
		ServerInterface serverInterface = new ServerInterface();
		ServerInterface serverInterface2 = new ServerInterface();
		int aTime, dTime;
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		Flights searchResults = new Flights();
		//SearchParams tempParams = userParams;
		SearchParams tempParams = new SearchParams();
		SearchParams tempParams2 = new SearchParams();
		int flightDuration;
		ArrayList<Ticket> tempTicket;
		FlightPlan tempNewFlightPlan;
		BigDecimal price;// = new BigDecimal(0.00);

		char[] tempAirportCode;
		char[] dAirportCode = userParams.getDepartureAirportCode();
		
		ArrayList<Flight> filteredListLeg = new ArrayList<Flight>();
		

		ArrayList<Flight> tempSResults = new ArrayList<Flight>();
		int fTime, lTime;
		
		tempParams.setSeatType(userParams.getSeatType());
		tempParams.setArrivalAirportCode(unconcluded.getLegs().get(0).getForFlight().getDepartureAirport().getCode().toCharArray());
		tempParams.setArrivalDate(unconcluded.getLegs().get(0).getForFlight().getDepartureTime().getDate());
		tempParams.setDepartureAirportCode(userParams.getDepartureAirportCode());
		dTime = unconcluded.getLegs().get(0).getForFlight().getDepartureTime().getTime().getTimeInMinutes();
		aTime = dTime - 4*60;
		
		searchResults.setFlightList((serverInterface.GetArrivingFlights(tempParams).getFlightList()));
		
		
		//layover over midnight
		if(aTime < 0){
			tempParams2.setArrivalAirportCode(unconcluded.getLegs().get(0).getForFlight().getDepartureAirport().getCode().toCharArray());
			tempParams2.setDepartureAirportCode(userParams.getDepartureAirportCode());
			tempParams2.setArrivalDate(tempParams.getArrivalDate().DecrementDate());
			tempSResults = new ArrayList<Flight>();
			tempSResults.addAll(searchResults.getFlightList());
			tempSResults.addAll(serverInterface2.GetArrivingFlights(tempParams2).getFlightList());
			searchResults.setFlightList(tempSResults);			
		}
		
		filteredListLeg = FilterFlightsList(unconcluded, searchResults.getFlightList(), level, 'a');
		
		for(Flight possibleLeg : filteredListLeg){
			
			//to calculate total flight duration
			fTime = getFlightDuration(possibleLeg);
			lTime = getLayoverTime(possibleLeg.getArrivalTime(), unconcluded.getLegs().get(0).getForFlight().getDepartureTime());
			
			//to test if it is arrival airport
			tempAirportCode = possibleLeg.getDepartureAirport().getCode().toCharArray();
			
			tempTicket = new ArrayList<Ticket>();
			tempTicket.add(new Ticket(userParams.getSeatType(), possibleLeg));
			tempTicket.add(new Ticket(unconcluded.getLegs().get(0).getSeatType(), unconcluded.getLegs().get(0).getForFlight()));
			
			price = new BigDecimal(0.0);
			
			if(level == 1){
				tempTicket.add(new Ticket());
				
				if(userParams.getSeatType() == 'c'){
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceC().getMoney());
					price = price.add(possibleLeg.getPriceC().getMoney());
					
				}
				else{
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceFc().getMoney());
					price = price.add(possibleLeg.getPriceFc().getMoney());
				}	
				
			}
			else{
				tempTicket.add(new Ticket(unconcluded.getLegs().get(1).getSeatType(), unconcluded.getLegs().get(1).getForFlight()));
				
				if(userParams.getSeatType() == 'c'){
					
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceC().getMoney());
					price = price.add(unconcluded.getLegs().get(1).getForFlight().getPriceC().getMoney());
					price = price.add(possibleLeg.getPriceC().getMoney());
				}
				else{
					price = price.add(unconcluded.getLegs().get(0).getForFlight().getPriceFc().getMoney());
					price = price.add(unconcluded.getLegs().get(1).getForFlight().getPriceFc().getMoney());
					price = price.add(possibleLeg.getPriceFc().getMoney());
				}	
			}
			
			
			flightDuration = unconcluded.getTotalTime() + lTime + fTime;
			
			if(dAirportCode[0] == tempAirportCode[0] && dAirportCode[1] == tempAirportCode[1] && dAirportCode[2] == tempAirportCode[2]){
				
				concludedList.add(new FlightPlan(level + 1, new Price(price), flightDuration, tempTicket));	
				
			}
			else if(level == 1){
				
				tempNewFlightPlan = new FlightPlan(level + 1, new Price(price), flightDuration, tempTicket);
				concludedList.addAll(GenerateFlightLegsA(tempNewFlightPlan, tempParams, 2));
				
			}

		}
		
		return concludedList;
		
	}
	
	
	
	public FlightPlans GenerateFlightPlansA(SearchParams userParams){
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		ArrayList<ArrayList<FlightPlan>> InitialLists = new ArrayList<ArrayList<FlightPlan>>();

		
		//leg 3
		InitialLists = FindInitialListsA(userParams);
		concludedList = InitialLists.get(0);
		unconcludedList = InitialLists.get(1);
		
		
		//other legs
		for(FlightPlan unconcluded : unconcludedList){
			concludedList.addAll(GenerateFlightLegsA(unconcluded, userParams, 1));
		}
		
		
		return new FlightPlans(concludedList);
	}
	
	/*
	 * Function that calls different types of search functions as needed
	 * for outgoing and returning flights and their search criteria
	 * 
	 * @param searchParam user-specified search criteria needed to search
	 * @return a list of FlightPlans that is one or two lists long
	 * depending if the user wants a round trip
	 */
	public ArrayList<FlightPlans> GeneratorManager(SearchParams searchParams){
		
		/*
		//Lia's test parameters
		searchParams.setArrivalAirportCode("TPA".toCharArray());
		searchParams.setDepartureAirportCode("BOS".toCharArray());
		Time timed[] = {new Time(04, 45), new Time(2, 30)};
		searchParams.setArrivalTime(timed);
		searchParams.setDepartureDate(null);
		searchParams.setIsRoundTrip(false);
		searchParams.setArrivalDate(new Date(5,5,2017));
		searchParams.setSeatType('c');
		*/
		//System.out.print(searchParams.toString());
		
		//System.out.print(searchParams.toString());
		
		ArrayList<FlightPlans> Lists = new ArrayList<FlightPlans>();
		SearchParams returnParams = new SearchParams();
		
		//calls different search functions depending if the departure date or arrival date was specified
		if(searchParams.getDepartureDate() != null){
			Lists.add(GenerateFlightPlansD(searchParams));
		}
		else{
			Lists.add(GenerateFlightPlansA(searchParams));
		}
		
		
		//does search with rearranged search parameters if the reservation is to be for a round trip
		if(searchParams.getIsRoundTrip()){
			returnParams.SetReturnParams(searchParams);
			
			if(searchParams.getRDepartureDate() != null){
				Lists.add(GenerateFlightPlansD(returnParams));
			}
			else{
				Lists.add(GenerateFlightPlansA(returnParams));
			}			
		}
		
		// pre-sort lists for display
		for(int i = 0; i<Lists.size(); i++){
			Lists.get(i).sortByLowestPrice();
			Lists.get(i).sortByHighestPrice();
			Lists.get(i).sortByMostTime();
			Lists.get(i).sortByLeastTime();
		}
		
		return Lists;
	}

	
	void TestStringFilter(FlightPlan Origin, ArrayList<Flight> PassedLegs, ArrayList<Flight> FailedLegs, ArrayList<Flight> ConcludedLegs, char searchType, int level, int numberFiltered){
		StringBuffer sb = new StringBuffer();
		
		
		
		if(!ConcludedLegs.isEmpty()){
			sb.append("\n\nConcluded:\n");
			sb.append(DisplayTestFList(ConcludedLegs, searchType, Origin.getLegs().get(0).getSeatType()));
			
		}
		
		if(!PassedLegs.isEmpty()){
			sb.append("\n\nPassed (and to be searched further):\n");
			sb.append(DisplayTestFList(PassedLegs, searchType, Origin.getLegs().get(0).getSeatType()));
		}
		
		if(!FailedLegs.isEmpty()){
			sb.append("\n\nFailed (failed validation):\n");
			sb.append(DisplayTestFList(FailedLegs, searchType, Origin.getLegs().get(0).getSeatType()));
		}
		
		
		System.out.print(sb.toString());
		
		
		
	}
	
	
	void TestStringInitial(SearchParams sp, ArrayList<FlightPlan> Passed, ArrayList<Flight> Failed, ArrayList<FlightPlan> Direct, char searchType, int numberFiltered){
		StringBuffer sb = new StringBuffer();
		StringBuffer dA = new StringBuffer();
		StringBuffer aA = new StringBuffer();
		
		dA.append( sp.getDepartureAirportCode());
		aA.append( sp.getArrivalAirportCode());
		
		sb.append("Search Params:\n" + dA.toString() + "->" + aA.toString());
		
		int minutesT1, minutesT2;
		Date dt, nd;
		
		if(searchType == 'd'){
			dt = new Date(sp.getDepartureDate().getDay(), sp.getDepartureDate().getMonth(), sp.getDepartureDate().getYear());
			nd = dt.IncrementDate();
			minutesT1 = sp.getDepartureTime()[0].getTimeInMinutes();
			minutesT2 = sp.getDepartureTime()[1].getTimeInMinutes();
			sb.append("\nSearching by Departure Date " + String.valueOf(sp.getDepartureDate().getMonth()) + "/" + String.valueOf(sp.getDepartureDate().getDay()) + "/" + String.valueOf(sp.getDepartureDate().getYear()));
			sb.append(" " + String.valueOf(sp.getDepartureTime()[0].getHours()) + ":" + String.valueOf(sp.getDepartureTime()[0].getMinutes())+ " GMT to ");
			if(minutesT1 >= minutesT2){
				sb.append(String.valueOf(nd.getMonth()) + "/" + String.valueOf(nd.getDay()) + "/" + String.valueOf(nd.getYear()) + " ");
			}
			sb.append(String.valueOf(sp.getDepartureTime()[1].getHours()) + ":" + String.valueOf(sp.getDepartureTime()[1].getMinutes()));
		}
		else{
			dt = new Date(sp.getArrivalDate().getDay(), sp.getArrivalDate().getMonth(), sp.getArrivalDate().getYear());
			nd = dt.IncrementDate();
			minutesT1 = sp.getArrivalTime()[0].getTimeInMinutes();
			minutesT2 = sp.getArrivalTime()[1].getTimeInMinutes();
			sb.append("\nSearching by Arrival Date " + String.valueOf(sp.getArrivalDate().getMonth()) + "/" + String.valueOf(sp.getArrivalDate().getDay()) + "/" + String.valueOf(sp.getArrivalDate().getYear()));
			sb.append(" " + String.valueOf(sp.getArrivalTime()[0].getHours()) + ":" + String.valueOf(sp.getArrivalTime()[0].getMinutes()) + " GMT to ");
			if(minutesT1 >= minutesT2){
				sb.append(String.valueOf(nd.getMonth()) + "/" + String.valueOf(nd.getDay()) + "/" + String.valueOf(nd.getYear()) + " ");
			}
			sb.append(String.valueOf(sp.getArrivalTime()[1].getHours()) + ":" + String.valueOf(sp.getArrivalTime()[1].getMinutes()));
		}
		
		sb.append(" GMT\nNeed minutes in range of: " + String.valueOf(minutesT1) + " to ");
		
		if(minutesT1 >= minutesT2){
			sb.append("1439 to ");
		}
		
		sb.append(String.valueOf(minutesT2));
		
		if(minutesT1 >= minutesT2){
			sb.append(" of the next day");
		}
		
		sb.append("\nSeat Type: " + sp.getSeatType() + "\n");
		
		sb.append("\nNumber Filtered: " + String.valueOf(numberFiltered));
		
		if(!Direct.isEmpty()){
			sb.append("\n\nConcluded:\n");
			sb.append(DisplayTestFPList(Direct, searchType, 0));
			
		}
		
		if(!Passed.isEmpty()){
			sb.append("\n\nPassed (and to be searched further):\n");
			sb.append(DisplayTestFPList(Passed, searchType, 0));
		}
		
		if(!Failed.isEmpty()){
			sb.append("\n\nFailed (failed validation):\n");
			sb.append(DisplayTestFList(Failed, searchType, sp.getSeatType()));
		}
		
		
		System.out.print(sb.toString());
		
		
	}
	
	String DisplayTestFPList(ArrayList<FlightPlan> List, char searchType, int level){
		StringBuilder sb = new StringBuilder();
		Date tDate;
		int count = 0;
		Flight tempFlight;
		for(FlightPlan fp : List){
			count = count + 1;
			//for(int i = 0; i < level+1; i++){
				//tempFlight = fp.getLegs().get(level).getForFlight();

				tempFlight = fp.getLegs().get(0).getForFlight();
				sb.append(String.valueOf(count) + ". " + tempFlight.getDepartureAirport().getCode() + "->" + tempFlight.getArrivalAirport().getCode());
				tDate = tempFlight.getDepartureTime().getDate();
				
				if(searchType == 'd'){
					tDate = tempFlight.getDepartureTime().getDate();	
					sb.append("\n" + String.valueOf(tDate.getMonth()) + "/" + String.valueOf(tDate.getDay()) + "/" + String.valueOf(tDate.getYear()));
					sb.append(" " + String.valueOf(tempFlight.getDepartureTime().getTime().getHours()) + ":" +String.valueOf(tempFlight.getDepartureTime().getTime().getMinutes()) + " GMT");
					sb.append(" | Departure Time mins: " + String.valueOf(tempFlight.getDepartureTime().getTime().getTimeInMinutes()));
				}
				else{
					tDate = tempFlight.getArrivalTime().getDate();
					sb.append("\n" + String.valueOf(tDate.getMonth()) + "/" + String.valueOf(tDate.getDay()) + "/" + String.valueOf(tDate.getYear()));
					sb.append(" " + String.valueOf(tempFlight.getArrivalTime().getTime().getHours()) + ":" +String.valueOf(tempFlight.getArrivalTime().getTime().getMinutes()) + " GMT");
					sb.append(" | Arrival Time mins: " + String.valueOf(tempFlight.getArrivalTime().getTime().getTimeInMinutes()));
				}
				
				sb.append("\nSeats Left for ");
				
				if(fp.getLegs().get(0).getSeatType() == 'c'){
					sb.append("Coach: " + String.valueOf(tempFlight.getSeatC()));
				}
				else{
					sb.append("First Class: " + String.valueOf(tempFlight.getSeatFc()));
				}
				/*
				
				if(level > 0){
					sb.append("\nLayover Time: " + String.valueOf(getLayoverTime(fp.getLegs().get(i).getForFlight().getArrivalTime(), fp.getLegs().get(i+1).getForFlight().getDepartureTime())));
				}
				*/
				
			//}
			sb.append("\n-----------------------\n");
		}
		
		return sb.toString();
	}
	
	String DisplayTestFList(ArrayList<Flight> List, char searchType, char seatType){
		
		StringBuilder sb = new StringBuilder();
		Date tDate;
		int count = 0;
		for(Flight tempFlight : List){
			count = count + 1;
			sb.append(String.valueOf(count) + ". " + tempFlight.getDepartureAirport().getCode() + "->" + tempFlight.getArrivalAirport().getCode());
			tDate = tempFlight.getDepartureTime().getDate();
			
			if(searchType == 'd'){
				tDate = tempFlight.getDepartureTime().getDate();	
				sb.append("\n" + String.valueOf(tDate.getMonth()) + "/" + String.valueOf(tDate.getDay()) + "/" + String.valueOf(tDate.getYear()));
				sb.append(" " + String.valueOf(tempFlight.getDepartureTime().getTime().getHours()) + ":" +String.valueOf(tempFlight.getDepartureTime().getTime().getMinutes()) + " GMT");
				sb.append(" | Departure Time mins: " + String.valueOf(tempFlight.getDepartureTime().getTime().getTimeInMinutes()));
			}
			else{
				tDate = tempFlight.getArrivalTime().getDate();
				sb.append("\n" + String.valueOf(tDate.getMonth()) + "/" + String.valueOf(tDate.getDay()) + "/" + String.valueOf(tDate.getYear()));
				sb.append(" " + String.valueOf(tempFlight.getArrivalTime().getTime().getHours()) + ":" +String.valueOf(tempFlight.getArrivalTime().getTime().getMinutes()) + " GMT");
				sb.append(" | Arrival Time mins: " + String.valueOf(tempFlight.getArrivalTime().getTime().getTimeInMinutes()));
			}
			
			sb.append("\nSeats Left for ");
			
			if(seatType == 'c'){
				sb.append("Coach: " + String.valueOf(tempFlight.getSeatC()));
			}
			else{
				sb.append("First Class: " + String.valueOf(tempFlight.getSeatFc()));
			}
			

			sb.append("\n-----------------------\n");
				
			
		}
		
		return sb.toString();
	
	}
	
	
}
