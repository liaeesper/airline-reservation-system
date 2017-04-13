package dao;

import java.util.ArrayList;

import flight.Flight;
import flight.Flights;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.SearchParams;
import plans.Ticket;
import utils.Date;
import utils.DateTime;

public class FlightPlansGenerator {
	
	public int getTimeBetween(DateTime a, DateTime b){
		int aMinutes = a.getTime().getTimeInMinutes();
		int bMinutes = b.getTime().getTimeInMinutes();
		
		if(aMinutes >= bMinutes){
			bMinutes += 24*60;
		}
		
		return bMinutes - aMinutes;
		
	}
	
	public int getLayoverTime(DateTime prevArrival, DateTime nextDeparture){
		int layoverTime = getTimeBetween(prevArrival, nextDeparture);
		
		if(layoverTime >= 30 && layoverTime <= 4*60){
			return layoverTime;
		}
		
		return -1;		
	}
	
	
	
	public boolean EnoughSeats(Flight flight, char seatType){
		return true;
	}
	
	public int getFlightDuration(Flight flight){
		return getTimeBetween(flight.getDepartureTime(), flight.getArrivalTime());
	}
	
	public boolean HappenedBefore(DateTime prevArrival, DateTime nextDeparture){
		
		if(nextDeparture.getDate().getDay() > prevArrival.getDate().getDay()){
			return false;
		}
		else if(nextDeparture.getDate().getDay() < prevArrival.getDate().getDay()){
			return true;
		}
		else{
			if(nextDeparture.getTime().getHours() > prevArrival.getTime().getHours()){
				return false;
			}
			else if(nextDeparture.getTime().getHours() < prevArrival.getTime().getHours()){
				return true;
			}
			else{
				if(nextDeparture.getTime().getMinutes() > prevArrival.getTime().getMinutes()){
					return false;
				}
				else if(nextDeparture.getTime().getMinutes() < prevArrival.getTime().getMinutes()){
					return true;
				}
			}
		}
		return true;//dates are equal
	}
	
	public ArrayList<Flight> FilterFlightsList(FlightPlan originPlan, ArrayList<Flight> unfiltered, int level){
		ArrayList<Flight> filtered = new ArrayList<Flight>();
		DateTime arrival, departure;
		
		for(Flight newF: unfiltered){
			arrival = originPlan.getLegs().get(level - 1).getForFlight().getArrivalTime();
			departure = newF.getDepartureTime();
			
			if(!HappenedBefore(arrival, departure) && getLayoverTime(arrival, departure) != -1 && EnoughSeats(newF, originPlan.getLegs().get(0).getSeatType())){
				filtered.add(new Flight(newF));				
			}		
		}
		return filtered;
	}
	
	
	public ArrayList<ArrayList<FlightPlan>> FindInitialLists(Flights sResults, SearchParams uParams){
		
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		
		int flightDuration, dTime;
		ArrayList<Ticket> tempTicket;
		
		int dTime1 = uParams.getDepartureTime()[0].getTimeInMinutes();
		int dTime2 = uParams.getDepartureTime()[1].getTimeInMinutes();
		
		char[] tempAirportCode;
		char[] aAirportCode = uParams.getArrivalAirportCode();
		
		for(Flight possibleLeg1 : sResults.getFlightList()){
			
			tempTicket = new ArrayList<Ticket>();
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			tempTicket.add(new Ticket());
			
			dTime = possibleLeg1.getDepartureTime().getTime().getTimeInMinutes();
			
			if(EnoughSeats(possibleLeg1, uParams.getSeatType()) && dTime >= dTime1 && dTime <= dTime2){
				
				tempTicket.get(0).setForFlight(possibleLeg1);
				tempTicket.get(0).setSeatType(uParams.getSeatType());
				
				
				flightDuration = getFlightDuration(possibleLeg1);
				tempAirportCode = possibleLeg1.getArrivalAirport().getCode().toCharArray();
				
				if(aAirportCode[0] == tempAirportCode[0] && aAirportCode[1] == tempAirportCode[1] && aAirportCode[2] == tempAirportCode[2]){
					if(uParams.getSeatType() == 'C'){
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}					
				}
				else{
					if(uParams.getSeatType() == 'C'){
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
	
	public int CalculateTotalTime(FlightPlan flightPlan, int level){
		int total = 0;
		
		for(int i = 0; i < level; i++){
			total += getFlightDuration(flightPlan.getLegs().get(i).getForFlight());
			if(i >= 1){
				total += getLayoverTime(flightPlan.getLegs().get(i-1).getForFlight().getArrivalTime(), flightPlan.getLegs().get(i).getForFlight().getDepartureTime());
			}
		}
		return total;
	}
	
	
	
	
	public FlightPlans GenerateFlightPlans(SearchParams userParams){
		ServerInterface serverInterface = new ServerInterface();
		int aTime, dTime;
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		ArrayList<ArrayList<FlightPlan>> InitialLists = new ArrayList<ArrayList<FlightPlan>>();
		Flights searchResults = new Flights();
		Flights lastChanceResults = new Flights();
		SearchParams tempParams = userParams;
		int flightDuration;
		ArrayList<Ticket> tempTicket;
		FlightPlan tempNewFlightPlan;

		char[] tempAirportCode;
		char[] aAirportCode = userParams.getArrivalAirportCode();
		//Flight possibleLeg1, possibleLeg2, possibleLeg3;
		
		ArrayList<Flight> filteredListLeg1 = new ArrayList<Flight>();
		ArrayList<Flight> filteredListLeg2 = new ArrayList<Flight>();
		
		//leg 1
		searchResults.setFlightList(serverInterface.GetDepartingFlights(userParams).getFlightList());
		InitialLists = FindInitialLists(searchResults, userParams);
		concludedList = InitialLists.get(0);
		unconcludedList = InitialLists.get(1);
		
		
		ArrayList<Flight> tempSResults = new ArrayList<Flight>();
		int fTime2, lTime2, fTime3, lTime3;
		
		//failed solo leg 1s
		for(FlightPlan possibleLeg2Plan : unconcludedList){
			
			tempParams.setDepartureAirportCode(possibleLeg2Plan.getLegs().get(0).getForFlight().getArrivalAirport().getCode().toCharArray());
			tempParams.setDepartureDate(possibleLeg2Plan.getLegs().get(0).getForFlight().getArrivalTime().getDate());
			aTime = possibleLeg2Plan.getLegs().get(0).getForFlight().getArrivalTime().getTime().getTimeInMinutes();
			dTime = aTime + 4*60;
			
			searchResults.setFlightList((serverInterface.GetDepartingFlights(tempParams).getFlightList()));
			

			//layover over midnight
			if(dTime >= 24*60){
				tempParams.setDepartureDate(IncrementDate(tempParams.getDepartureDate()));
				tempSResults = new ArrayList<Flight>();
				tempSResults.addAll(searchResults.getFlightList());
				tempSResults.addAll(serverInterface.GetDepartingFlights(tempParams).getFlightList());
				searchResults.setFlightList(tempSResults);
				
			}
			
			filteredListLeg1 = FilterFlightsList(possibleLeg2Plan, searchResults.getFlightList(), 1);
			
			//leg 2
			for(Flight possibleLeg2 : filteredListLeg1){
				
				fTime2 = getFlightDuration(possibleLeg2);
				lTime2 = getLayoverTime(possibleLeg2Plan.getLegs().get(0).getForFlight().getArrivalTime(), possibleLeg2.getDepartureTime());
				
				
				tempAirportCode = possibleLeg2.getArrivalAirport().getCode().toCharArray();
				
				tempTicket = new ArrayList<Ticket>();
				tempTicket.add(new Ticket(possibleLeg2Plan.getLegs().get(0).getSeatType(), possibleLeg2Plan.getLegs().get(0).getForFlight()));
				tempTicket.add(new Ticket(userParams.getSeatType(), possibleLeg2));
				tempTicket.add(new Ticket());
				
				
				flightDuration = possibleLeg2Plan.getTotalTime() + lTime2 + fTime2;
				
				if(aAirportCode[0] == tempAirportCode[0] && aAirportCode[1] == tempAirportCode[1] && aAirportCode[2] == tempAirportCode[2]){
					
					if(userParams.getSeatType() == 'C'){
						
						concludedList.add(new FlightPlan(2, possibleLeg2.getPriceC(), flightDuration, tempTicket));
					}
					else{
						concludedList.add(new FlightPlan(2, possibleLeg2.getPriceFc(), flightDuration, tempTicket));
					}	
				}
				else{
					//leg 3
					if(userParams.getSeatType() == 'C'){
						tempNewFlightPlan = new FlightPlan(2, possibleLeg2.getPriceC(), flightDuration, tempTicket);
					}
					else{
						tempNewFlightPlan = new FlightPlan(2, possibleLeg2.getPriceFc(), flightDuration, tempTicket);
					}
					//lastChanceResults = serverInterface.GetDepartingFlights(tempParams);
					
					tempParams.setDepartureAirportCode(possibleLeg2.getArrivalAirport().getCode().toCharArray());
					tempParams.setDepartureDate(possibleLeg2.getArrivalTime().getDate());
					aTime = possibleLeg2.getArrivalTime().getTime().getTimeInMinutes();
					dTime = aTime + 4*60;
					
					lastChanceResults.setFlightList((serverInterface.GetDepartingFlights(tempParams).getFlightList()));
					

					//layover over midnight
					if(dTime >= 24*60){
						tempParams.setDepartureDate(IncrementDate(tempParams.getDepartureDate()));
						tempSResults = new ArrayList<Flight>();
						tempSResults.addAll(lastChanceResults.getFlightList());
						tempSResults.addAll(serverInterface.GetDepartingFlights(tempParams).getFlightList());
						lastChanceResults.setFlightList(tempSResults);
						
					}
					
					filteredListLeg2 = FilterFlightsList(tempNewFlightPlan, lastChanceResults.getFlightList(), 2);
					
					for(Flight possibleLeg3 : filteredListLeg2){
					
						fTime3 = getFlightDuration(possibleLeg3);
						lTime3 = getLayoverTime(possibleLeg2.getArrivalTime(), possibleLeg3.getDepartureTime());
					
						tempAirportCode = possibleLeg3.getArrivalAirport().getCode().toCharArray();
							
						if(aAirportCode[0] == tempAirportCode[0] && aAirportCode[1] == tempAirportCode[1] && aAirportCode[2] == tempAirportCode[2]){
							tempTicket = new ArrayList<Ticket>();
							tempTicket.add(new Ticket(possibleLeg2Plan.getLegs().get(0).getSeatType(), possibleLeg2Plan.getLegs().get(0).getForFlight()));
							tempTicket.add(new Ticket(userParams.getSeatType(), possibleLeg2));
							tempTicket.add(new Ticket(userParams.getSeatType(), possibleLeg3));
							
							flightDuration = possibleLeg2Plan.getTotalTime() + lTime2 + fTime2 + lTime3 + fTime3;
							
							if(userParams.getSeatType() == 'C'){
								concludedList.add(new FlightPlan(3, possibleLeg2.getPriceC(), flightDuration, tempTicket));
							}
							else{
								concludedList.add(new FlightPlan(3, possibleLeg2.getPriceFc(), flightDuration, tempTicket));
							}	
						}											
					}
				}
			}
		}
		
		
		
		return new FlightPlans(concludedList);
	}
	
	
	private Date IncrementDate(Date departureDate) {
		int day = departureDate.getDay();
		int month = departureDate.getMonth();
		int year = departureDate.getYear();
		
		if(day == 31 && month == 12){
			return new Date(1, 1, year++);
		}
		else if(day == 31 && (month == 1 || month == 3 || month == 5  || month == 7 || month == 8 || month == 10)){
			return new Date(1, month++, year);
		}
		else if(day == 28 && month == 2){
			return new Date(1, month++, year);
		}
		else if(day == 30 && (month == 4 || month == 6 || month == 9|| month == 11)){
			return new Date(1, month++, year);
		}
		else{
			return new Date(day++, month, year);
		}
		
	}
	
}
