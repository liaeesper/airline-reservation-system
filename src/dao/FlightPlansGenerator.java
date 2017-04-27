
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
	 * Filters the a given flight list so it gets a valid list of flight legs
	 * Makes sure that one a previous leg did not occur before another, that layover time is valid, 
	 * that there is enough seats of the specified seat type, and that there is no backtracking airports
	 * @param originPlan Plan that yielded the SearchParams that was used to get the unfiltered list
	 * @param unfiltered list of possible flight legs that occur within a proximity of originPlan
	 * @param level of search the system is in in terms of how many flight legs have been found for originPlan already
	 * @param searchType of search that is going on (by arrival date or departure date)
	 * @return a list of valid flight legs
	 */
	public ArrayList<Flight> FilterFlightsList(FlightPlan originPlan, ArrayList<Flight> unfiltered, int level, char searchType){
		ArrayList<Flight> filtered = new ArrayList<Flight>();
		DateTime arrival = null, departure = null;
		String dAirport = null, aAirport = null;
		
		//comment out if not testing ~
		ArrayList<Flight> failed = new ArrayList<Flight>();
		
		//if searching by departure date
		if (searchType == 'd'){
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
			if(searchType == 'd'){
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
			if(arrival.HappenedAfter(departure) && getLayoverTime(arrival, departure) != -1 && EnoughSeats(newF, originPlan.getLegs().get(0).getSeatType()) && !dAirport.equals(aAirport)){
				
				//adds it to the filtered list if passes
				filtered.add(new Flight(newF));				
			}
			//comment out if not testing ~
			else{
				failed.add(new Flight(newF));
			}
		}
		
		//comment out if not testing ~
		TestStringFilter(originPlan, filtered, failed, searchType, level, unfiltered.size());
		
		//returns filtered list
		return filtered;
	}
	
	/**
	 * Finds the initial lists based on the user-specified search paramters
	 * @param userParams user-specified user parameters
	 * @param searchType type of search being done (by departure or arrival date)
	 * @return returns a list containing 2 lists -- direct flights and valid flight legs
	 */
	public ArrayList<ArrayList<FlightPlan>> FindInitialLists(SearchParams userParams, char searchType){
		ServerInterface serverInterface = new ServerInterface();
		Flights searchResults = new Flights();
		SearchParams tempParams = new SearchParams();
		ArrayList <Flight> tempSResults;
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		int flightDuration, nTime, wTime1, wTime2, mTime;
		ArrayList<Ticket> tempTicket;
		Date neededDate, nextDay;
		char[] tempAirportCode, testingAirportCode;
		boolean firstDayLegal = false, secondDayLegal = false;
		
		//comment out when not testing ~
		ArrayList <Flight> failed = new ArrayList<Flight>();
		
		//if searching by departure date
		if(searchType == 'd'){
			//gets flights by departure
			searchResults.setFlightList(serverInterface.GetDepartingFlights(userParams).getFlightList());
			
			//gets the time window
			wTime1 = userParams.getDepartureTime()[0].getTimeInMinutes();
			wTime2 = userParams.getDepartureTime()[1].getTimeInMinutes();
			
			//gets the date we need to search for
			neededDate = new Date(userParams.getDepartureDate().getDay(), userParams.getDepartureDate().getMonth(), userParams.getDepartureDate().getYear());
			
			//gets airport to test equivalency with
			testingAirportCode = userParams.getArrivalAirportCode();
		}
		//if searching by arrival date
		else{
			//gets flights by arrival
			searchResults.setFlightList(serverInterface.GetArrivingFlights(userParams).getFlightList());
			
			//gets the time window
			wTime1 = userParams.getArrivalTime()[0].getTimeInMinutes();
			wTime2 = userParams.getArrivalTime()[1].getTimeInMinutes();
			
			//gets the date we need to search for
			neededDate = new Date(userParams.getArrivalDate().getDay(), userParams.getArrivalDate().getMonth(), userParams.getArrivalDate().getYear());
			
			//gets airport to test equivalency with
			testingAirportCode = userParams.getDepartureAirportCode();
		}
		
		//gets next date in case it is needed and tests if the next day needs to be searched (if the time window goes over midnight)
		//if so, it does and adds it to the search results list
		nextDay = neededDate.IncrementDate();
		mTime = wTime2;
		if(wTime1 > wTime2){
			mTime = 24*60 - 1;
			tempParams.setArrivalAirportCode(userParams.getArrivalAirportCode());
			tempParams.setDepartureAirportCode(userParams.getDepartureAirportCode());
			tempSResults = new ArrayList<Flight>();
			tempSResults.addAll(searchResults.getFlightList());
			if(searchType == 'd'){
				tempParams.setDepartureDate(nextDay);
				tempSResults.addAll(serverInterface.GetDepartingFlights(tempParams).getFlightList());
			}
			else{
				tempParams.setArrivalDate(nextDay);
				tempSResults.addAll(serverInterface.GetArrivingFlights(tempParams).getFlightList());
			}
			searchResults.setFlightList(tempSResults);
		}
		
		//goes through each resulting flight legs
		for(Flight possibleLeg1 : searchResults.getFlightList()){
			tempTicket = new ArrayList<Ticket>();
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			
			firstDayLegal = false;
			secondDayLegal = false;
			
			//gets testing parameters depending on the type of search
			if(searchType == 'd'){
				nTime = possibleLeg1.getDepartureTime().getTime().getTimeInMinutes();
				firstDayLegal = (neededDate.getDay() == possibleLeg1.getDepartureTime().getDate().getDay());
				secondDayLegal = (nextDay.getDay() == possibleLeg1.getDepartureTime().getDate().getDay());
				tempAirportCode = possibleLeg1.getArrivalAirport().getCode().toCharArray();
			}
			else{
				nTime = possibleLeg1.getArrivalTime().getTime().getTimeInMinutes();
				firstDayLegal = (neededDate.getDay() == possibleLeg1.getArrivalTime().getDate().getDay());
				secondDayLegal = (nextDay.getDay() == possibleLeg1.getArrivalTime().getDate().getDay());
				tempAirportCode = possibleLeg1.getDepartureAirport().getCode().toCharArray();
			}
			
			//tests if the leg has enough seats and fits in the correct time window, if so, continue compiling the FlightPlan
			if(EnoughSeats(possibleLeg1, userParams.getSeatType()) && ((nTime >= wTime1 && nTime <= mTime && firstDayLegal) || (nTime >= 0 && nTime <= wTime2 && secondDayLegal))){
				
				tempTicket.get(0).setForFlight(possibleLeg1);
				tempTicket.get(0).setSeatType(userParams.getSeatType());
				
				
				flightDuration = getFlightDuration(possibleLeg1);
				
				//tests if the flight is direct and adds it to concluded if so
				if(testingAirportCode[0] == tempAirportCode[0] && testingAirportCode[1] == tempAirportCode[1] && testingAirportCode[2] == tempAirportCode[2]){
					if(userParams.getSeatType() == 'c'){
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}					
				}
				//if not concluded, adds it to unconcluded, but it does pass
				else{
					if(userParams.getSeatType() == 'c'){
						unconcludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						unconcludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}		
				}
			}
			//comment out when not testing ~
			else{
				failed.add(possibleLeg1);
			}
		}
		
		//comment out when not testing ~
		TestStringInitial(userParams, unconcludedList, failed, concludedList, searchType, searchResults.getFlightList().size());
		
		//returns two lists
		ArrayList<ArrayList<FlightPlan>> List = new ArrayList<ArrayList<FlightPlan>>();
		List.add(concludedList);
		List.add(unconcludedList);
		return List;
	}
	
	/**
	 * Gets flight legs after initial legs have been established. Recursively calls itself for one more level to get 3rd leg if necessary
	 * @param unconcluded gets flight plan to search off of
	 * @param searchParams search parameters to search with
	 * @param level of how deep the search is
	 * @param searchType by arrival or departure
	 * @return returns list of concluded flight plans
	 */
	public ArrayList<FlightPlan> GenerateFlightLegs(FlightPlan unconcluded, SearchParams searchParams, int level, char searchType){
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		Flights searchResults = new Flights();
		SearchParams nextParams = new SearchParams();
		FlightPlan tempNewFlightPlan;
		ArrayList<Flight> filteredListLeg = new ArrayList<Flight>();
		char[] tempAirportCode, nAirportCode = null;
		
		//for testing ~
		ArrayList<Flight> concluded = new ArrayList<Flight>();
		
		//gets search results for the unconcluded flight plan's last added leg
		searchResults = LegSearchResults(unconcluded, searchParams, nextParams, level, searchType);
		
		//filters the results
		filteredListLeg = FilterFlightsList(unconcluded, searchResults.getFlightList(), level, searchType);
		
		//goes through the filtered results
		for(Flight possibleLeg : filteredListLeg){
			
			if(searchType == 'd'){
				//used to test if it is arrival airport
				tempAirportCode = possibleLeg.getArrivalAirport().getCode().toCharArray();
				nAirportCode = searchParams.getArrivalAirportCode();
			}
			else{
				//used to test if it is departure airport
				tempAirportCode = possibleLeg.getDepartureAirport().getCode().toCharArray();
				nAirportCode = searchParams.getDepartureAirportCode();
			}
			
			//assembles the next flight plan to use or put in concluded list
			tempNewFlightPlan = AssembleFlightPlan(unconcluded, searchParams, possibleLeg, level, searchType);

			//checks if the plan is concluded
			if(nAirportCode[0] == tempAirportCode[0] && nAirportCode[1] == tempAirportCode[1] && nAirportCode[2] == tempAirportCode[2]){
				//comment out if not testing ~
				concluded.add(possibleLeg);
				
				concludedList.add(tempNewFlightPlan);	
				
			}
			//if not, and we are only searching the second leg, searches for a third leg
			else if(level == 1){
				concludedList.addAll(GenerateFlightLegs(tempNewFlightPlan, nextParams, 2, searchType));
			}

		}
		
		//comment out if not testing ~
		StringBuilder sb = new StringBuilder();
		if(!concluded.isEmpty()){
			sb.append("\n\nConcluded (out of the passed):\n");
			sb.append(DisplayTestFList(concluded, searchType, nextParams.getSeatType()));
			System.out.println(sb.toString());
		}
		
		return concludedList;
		
	}

	/**
	 * Assembles a flight plan given information
	 * @param sourcePlan source flight plan that was used to search the new leg with
	 * @param sourceParams search params that was used to search the new leg with
	 * @param level in the search
	 * @param searchType by arrival or departure
	 * @param possibleLeg leg to add to the flight plan
	 * @return returns an assembled flight plan out of the parameters
	 */
	private FlightPlan AssembleFlightPlan(FlightPlan sourcePlan, SearchParams sourceParams, Flight possibleLeg, int level, char searchType) {
		ArrayList<Ticket> tempTicket;
		BigDecimal price;
		int fTime, lTime, flightDuration;
		
		//to calculate total flight duration
		fTime = getFlightDuration(possibleLeg);
		
		tempTicket = new ArrayList<Ticket>();
		
		//gets layover time and adds tickets depending on the type of search
		if(searchType == 'd'){
			lTime = getLayoverTime(sourcePlan.getLegs().get(level - 1).getForFlight().getArrivalTime(), possibleLeg.getDepartureTime());
			
			//sets first leg to initially received leg
			tempTicket.add(new Ticket(sourcePlan.getLegs().get(0).getSeatType(), sourcePlan.getLegs().get(0).getForFlight()));
		}
		else{
			lTime = getLayoverTime(possibleLeg.getArrivalTime(), sourcePlan.getLegs().get(0).getForFlight().getDepartureTime());			
			
			//sets new leg to the first leg and the leg that was previously first into the second slot
			tempTicket.add(new Ticket(sourceParams.getSeatType(), possibleLeg));
			tempTicket.add(new Ticket(sourcePlan.getLegs().get(0).getSeatType(), sourcePlan.getLegs().get(0).getForFlight()));
		}
		
		price = new BigDecimal(0.0);
		
		//if search was for a second leg
		if(level == 1){
			if(searchType == 'd'){
				//add this new leg to the second slot
				tempTicket.add(new Ticket(sourceParams.getSeatType(), possibleLeg));
			}
			
			//add an empty leg to the last slot last
			tempTicket.add(new Ticket());
			
			//gets total price so far
			if(sourceParams.getSeatType() == 'c'){
				price = price.add(sourcePlan.getLegs().get(0).getForFlight().getPriceC().getMoney());
				price = price.add(possibleLeg.getPriceC().getMoney());
				
			}
			else{
				price = price.add(sourcePlan.getLegs().get(0).getForFlight().getPriceFc().getMoney());
				price = price.add(possibleLeg.getPriceFc().getMoney());
			}	
			
		}
		else{
			//sets next leg to what was in the second slot for the unconcluded flight plan (different order for by departure and by arrival)
			tempTicket.add(new Ticket(sourcePlan.getLegs().get(1).getSeatType(), sourcePlan.getLegs().get(1).getForFlight()));
			
			//adds new leg to the third slot if search is by departure
			if(searchType == 'd'){
				tempTicket.add(new Ticket(sourceParams.getSeatType(), possibleLeg));
			}
			
			//calculates total price
			if(sourceParams.getSeatType() == 'c'){
				
				price = price.add(sourcePlan.getLegs().get(0).getForFlight().getPriceC().getMoney());
				price = price.add(sourcePlan.getLegs().get(1).getForFlight().getPriceC().getMoney());
				price = price.add(possibleLeg.getPriceC().getMoney());
			}
			else{
				price = price.add(sourcePlan.getLegs().get(0).getForFlight().getPriceFc().getMoney());
				price = price.add(sourcePlan.getLegs().get(1).getForFlight().getPriceFc().getMoney());
				price = price.add(possibleLeg.getPriceFc().getMoney());
			}	
		}
		
		//calculates flight duration
		flightDuration = sourcePlan.getTotalTime() + lTime + fTime;
		
		//returns compiled flight plan
		return new FlightPlan(level + 1, new Price(price), flightDuration, tempTicket);
	}
	
	/**
	 * Gets search results for last compiled leg in a source plan
	 * @param sourcePlan source flight plan to get the next legs of
	 * @param sourceParams search parameters to derive nextParams from
	 * @param nextParams search parameters to use
	 * @param level of the search
	 * @param searchType by arrival or departure
	 * @return list of unfiltered legs
	 */
	private Flights LegSearchResults(FlightPlan sourcePlan, SearchParams sourceParams, SearchParams nextParams, int level, char searchType) {
		int aTime, dTime;
		ArrayList<Flight> tempSResults;
		ServerInterface serverInterface = new ServerInterface();
		Flights searchResults = new Flights();
		SearchParams tempParams = new SearchParams();
		
		//compiles next params with different params, depending on the type of search
		nextParams.setSeatType(sourceParams.getSeatType());
		
		if(searchType == 'd'){
			
			//overall arrival airport
			nextParams.setArrivalAirportCode(sourceParams.getArrivalAirportCode());
			
			//departure airport and date are the ones of the last leg that was added
			nextParams.setDepartureAirportCode(sourcePlan.getLegs().get(level - 1).getForFlight().getArrivalAirport().getCode().toCharArray());
			nextParams.setDepartureDate(sourcePlan.getLegs().get(level - 1).getForFlight().getArrivalTime().getDate());
			
			//sets arrival time and departure time to test with
			aTime = sourcePlan.getLegs().get(level - 1).getForFlight().getArrivalTime().getTime().getTimeInMinutes();
			dTime = aTime + 4*60;
			
			//searches
			searchResults.setFlightList((serverInterface.GetDepartingFlights(nextParams).getFlightList()));
			
			//if there is a layover over midnight
			//re-does the search for the next day and adds it to the list
			if(dTime >= 24*60){
				tempParams.setArrivalAirportCode(sourceParams.getArrivalAirportCode());
				tempParams.setDepartureAirportCode(sourcePlan.getLegs().get(level - 1).getForFlight().getArrivalAirport().getCode().toCharArray());
				tempParams.setDepartureDate(nextParams.getDepartureDate().IncrementDate());
				tempSResults = new ArrayList<Flight>();
				tempSResults.addAll(searchResults.getFlightList());
				tempSResults.addAll(serverInterface.GetDepartingFlights(tempParams).getFlightList());
				searchResults.setFlightList(tempSResults);			
			}
			
		}
		else{
			//arrival airport is the one of the last leg that was added
			nextParams.setArrivalAirportCode(sourcePlan.getLegs().get(0).getForFlight().getDepartureAirport().getCode().toCharArray());
			
			//overall departure airport
			nextParams.setDepartureAirportCode(sourceParams.getDepartureAirportCode());
			
			//arrival airport is the one of the last leg that was added
			nextParams.setArrivalDate(sourcePlan.getLegs().get(0).getForFlight().getDepartureTime().getDate());
			
			//sets arrival time and departure time to test with
			dTime = sourcePlan.getLegs().get(0).getForFlight().getDepartureTime().getTime().getTimeInMinutes();
			aTime = dTime - 4*60;
			
			//searches
			searchResults.setFlightList((serverInterface.GetArrivingFlights(nextParams).getFlightList()));
			
			//if there is a layover over midnight
			//redoes the search for the next day and adds it to the list
			if(aTime < 0){
				tempParams.setArrivalAirportCode(sourcePlan.getLegs().get(0).getForFlight().getDepartureAirport().getCode().toCharArray());
				tempParams.setDepartureAirportCode(sourceParams.getDepartureAirportCode());
				tempParams.setArrivalDate(nextParams.getArrivalDate().DecrementDate());
				tempSResults = new ArrayList<Flight>();
				tempSResults.addAll(searchResults.getFlightList());
				tempSResults.addAll(serverInterface.GetArrivingFlights(tempParams).getFlightList());
				searchResults.setFlightList(tempSResults);			
			}
			
		}
		
		//returns the results
		return searchResults;
	}
	
	/**
	 * Calls proper functions to search with
	 * @param searchParams user-specified search parameters
	 * @param searchType type of search by arrival or departure
	 * @return returns list of concluded flight plans
	 */
	public FlightPlans GenerateFlightPlans(SearchParams searchParams, char searchType){
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		ArrayList<ArrayList<FlightPlan>> InitialLists = new ArrayList<ArrayList<FlightPlan>>();
		
		//Initial legs
		InitialLists = FindInitialLists(searchParams, searchType);
		concludedList = InitialLists.get(0);
		unconcludedList = InitialLists.get(1);
				
		//other legs
		for(FlightPlan unconcluded : unconcludedList){
			concludedList.addAll(GenerateFlightLegs(unconcluded, searchParams, 1, searchType));
		}
		
		//returns list of concluded valid list
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
	public ArrayList<FlightPlans> GeneratorManager(SearchParams userParams){
		
		
		//Lia's test parameters comment out if not testing ~
		userParams.setDepartureAirportCode("BOS".toCharArray());
		userParams.setArrivalAirportCode("DFW".toCharArray());
		Time timed[] = {new Time(9, 00), new Time(2, 00)};
		/*
		userParams.setArrivalTime(timed);
		userParams.setDepartureDate(null);
		userParams.setArrivalDate(new Date(9,5,2017));
		*/
		
		userParams.setDepartureTime(timed);
		userParams.setArrivalDate(null);
		userParams.setDepartureDate(new Date(5,5,2017));
		
		
		userParams.setIsRoundTrip(false);
		userParams.setSeatType('c');
		
		
		
		ArrayList<FlightPlans> Lists = new ArrayList<FlightPlans>();
		SearchParams returnParams = new SearchParams();
		
		//calls different search functions depending if the departure date or arrival date was specified
		if(userParams.getDepartureDate() != null){
			Lists.add(GenerateFlightPlans(userParams, 'd'));
		}
		else{
			Lists.add(GenerateFlightPlans(userParams, 'a'));
		}
		
		
		//does search with rearranged search parameters if the reservation is to be for a round trip
		if(userParams.getIsRoundTrip()){
			returnParams.SetReturnParams(userParams);
			
			if(userParams.getRDepartureDate() != null){
				Lists.add(GenerateFlightPlans(returnParams, 'd'));
			}
			else{
				Lists.add(GenerateFlightPlans(returnParams, 'a'));
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

	void TestStringFilter(FlightPlan origin, ArrayList<Flight> passedLegs, ArrayList<Flight> failedLegs, char searchType, int level, int numberFiltered){
		StringBuffer sb = new StringBuffer();
		Flight originfl;
		Date date = new Date();
		int time1, time2;
		
		for(int i = 0; i < 10; i++){
		sb.append("\n");
		}
		
		char tempType;
		
		if(searchType == 'd'){
			sb.append("Search by Departure:\n");
			originfl = origin.getLegs().get(level - 1).getForFlight();
			date = originfl.getArrivalTime().getDate();
			time1 = originfl.getArrivalTime().getTime().getTimeInMinutes();
			time2 = time1 + 4*60;
			time1 = time1 + 30;
			tempType = 'a';
		}
		else{
			sb.append("Search by Arrival:\n");
			originfl = origin.getLegs().get(0).getForFlight();
			date = originfl.getDepartureTime().getDate();
			time2 = originfl.getDepartureTime().getTime().getTimeInMinutes();
			time1 = time2 - 4*60;
			time2 = time2 - 30;
			tempType = 'd';
		}
		
		
		sb.append("Need flights for this flight leg with proper credentials:\n");
		ArrayList<Flight> temp = new ArrayList<Flight>();
		temp.add(originfl);
		sb.append(DisplayTestFList(temp, tempType, origin.getLegs().get(0).getSeatType()));
		
		sb.append("\nProper layover time allows flights between these times in minutes:\n");
		
		if(searchType == 'd'){
			if(time1 >= 24*60 && time2 >= 24*60){
				time1 = time1 - 24*60;
				time2 = time2 - 24*60;
				date = date.IncrementDate();
				sb.append(String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/" + String.valueOf(date.getYear()));
				sb.append(" " + String.valueOf(time1) + " to " + String.valueOf(time2) + "\n");
			}
			else if(time2 >= 24*60){
				time2 = time2 - 24*60;
				sb.append(String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/" + String.valueOf(date.getYear()));
				sb.append(" " + String.valueOf(time1) + " to " + String.valueOf(time2) + " of the next day\n");
			}
			else{
				sb.append(String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/" + String.valueOf(date.getYear()));
				sb.append(" " + String.valueOf(time1) + " to " + String.valueOf(time2) + "\n");
			}
		}
		else if(searchType == 'a'){
			if(time2 < 0){
				time2 = time2 + 24*60;
			}
			if(time1 < 0){
				date = date.DecrementDate();
				time1 = time1 + 24*60;
				sb.append(String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/" + String.valueOf(date.getYear()));
				sb.append(" " + String.valueOf(time1) + " to " + String.valueOf(time2) + " of the next day\n");
			}
			else{
				sb.append(String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/" + String.valueOf(date.getYear()));
				sb.append(" " + String.valueOf(time1) + " to " + String.valueOf(time2) + "\n");
			}
		}
		
		
		sb.append("\nNumber Legs Looked At: " + String.valueOf(numberFiltered));

		
		if(!passedLegs.isEmpty()){
			sb.append("\n\nPassed (and to be searched further):\n");
			sb.append(DisplayTestFList(passedLegs, searchType, origin.getLegs().get(0).getSeatType()));
		}
		
		if(!failedLegs.isEmpty()){
			sb.append("\n\nFailed (failed validation):\n");
			sb.append(DisplayTestFList(failedLegs, searchType, origin.getLegs().get(0).getSeatType()));
		}
		
		
		System.out.print(sb.toString());
		
		
		
	}
	
	void TestStringInitial(SearchParams sp, ArrayList<FlightPlan> passed, ArrayList<Flight> failed, ArrayList<FlightPlan> direct, char searchType, int numberFiltered){
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
			sb.append("1439, 0 to ");
		}
		
		sb.append(String.valueOf(minutesT2));
		
		if(minutesT1 >= minutesT2){
			sb.append(" of the next day");
		}
		
		sb.append("\nSeat Type: " + sp.getSeatType() + "\n");
		
		sb.append("\nNumber Legs Looked At: " + String.valueOf(numberFiltered));
		
		if(!direct.isEmpty()){
			sb.append("\n\nConcluded:\n");
			sb.append(DisplayTestFPList(direct, searchType, 0));
			
		}
		
		if(!passed.isEmpty()){
			sb.append("\n\nPassed (and to be searched further):\n");
			sb.append(DisplayTestFPList(passed, searchType, 0));
		}
		
		if(!failed.isEmpty()){
			sb.append("\n\nFailed (failed validation):\n");
			sb.append(DisplayTestFList(failed, searchType, sp.getSeatType()));
		}
		
		
		System.out.print(sb.toString());
		
		
	}
	
	String DisplayTestFPList(ArrayList<FlightPlan> list, char searchType, int level){
		StringBuilder sb = new StringBuilder();
		Date tDate;
		int count = 0;
		Flight tempFlight;
		for(FlightPlan fp : list){
			count = count + 1;
			//for(int i = 0; i < level+1; i++){
				//tempFlight = fp.getLegs().get(level).getForFlight();

				tempFlight = fp.getLegs().get(0).getForFlight();
				sb.append(String.valueOf(count) + ". " + String.valueOf(tempFlight.getFlightNumber()) + " " + tempFlight.getDepartureAirport().getCode() + "->" + tempFlight.getArrivalAirport().getCode());
				
				
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
	
	String DisplayTestFList(ArrayList<Flight> list, char searchType, char seatType){
		
		StringBuilder sb = new StringBuilder();
		Date tDate;
		int count = 0;
		for(Flight tempFlight : list){
			count = count + 1;
			sb.append(String.valueOf(count) + ". " + String.valueOf(tempFlight.getFlightNumber()) + " " + tempFlight.getDepartureAirport().getCode() + "->" + tempFlight.getArrivalAirport().getCode());
			
			
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

