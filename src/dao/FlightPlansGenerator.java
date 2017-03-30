package dao;

import java.util.ArrayList;

import flight.Flight;
import flight.Flights;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.SearchParams;
import plans.Ticket;
import utils.DateTime;

public class FlightPlansGenerator {
	
	public int layoverTime(DateTime prevArrival, DateTime nextDeparture){
		int prevArrivalMinutes = prevArrival.getTime().getTimeInMinutes();
		int nextDepartureMinutes = nextDeparture.getTime().getTimeInMinutes();
		
		if(prevArrivalMinutes >= nextDepartureMinutes){
			nextDepartureMinutes += 24*60;
		}
		
		int layoverTime = nextDepartureMinutes - prevArrivalMinutes;
		
		if(layoverTime >= 30 && layoverTime <= 4*60){
			return layoverTime;
		}
		
		return -1;
		
	}
	
	public boolean enoughSeats(Flight flight){
		return true;
	}
	
	public int getFlightDuration(Flight flight){
		int dTime = flight.getDepartureTime().getTime().getTimeInMinutes();
		int aTime = flight.getArrivalTime().getTime().getTimeInMinutes();
		
		if(aTime <= dTime){
			return ((24*60) - dTime) + aTime;
		}
		else{
			return aTime - dTime;
		}
	}
	
	
	public FlightPlans GenerateFlightPlans(SearchParams userParams){
		ServerInterface serverInterface = new ServerInterface();
		
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		Flights searchResults = new Flights();
		Flights lastChanceResults = new Flights();
		SearchParams tempParams = userParams;
		int flightDuration;
		int lTime;
		Ticket[] tempTicket = {null, null, null};
		
		//leg 1
		
		searchResults.setFlightList(serverInterface.GetDepartingFlights(userParams).getFlightList());
		
		for(Flight possibleLeg1 : searchResults.getFlightList()){
			
			//if(possibleLeg1.getArrivalAirport().getCode().equals(userParams.getArrivalAirportCode())){
			if(enoughSeats(possibleLeg1) && possibleLeg1.getDepartureTime().getTime().getTimeInMinutes() >= userParams.getDepartureTime()[0].getTimeInMinutes()
					&& possibleLeg1.getDepartureTime().getTime().getTimeInMinutes() <= userParams.getDepartureTime()[1].getTimeInMinutes()){
				
				tempTicket[0].setForFlight(possibleLeg1);
				tempTicket[0].setSeatType(userParams.getSeatType());
				
				flightDuration = getFlightDuration(possibleLeg1);
				
				if(possibleLeg1.getArrivalAirport().getCode().equals(userParams.getArrivalAirportCode())){
					if(userParams.getSeatType() == 'C'){
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						concludedList.add(new FlightPlan(1, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}					
				}
				else{
					if(userParams.getSeatType() == 'C'){
						unconcludedList.add(new FlightPlan(0, possibleLeg1.getPriceC(), flightDuration, tempTicket));
					}
					else{
						unconcludedList.add(new FlightPlan(0, possibleLeg1.getPriceFc(), flightDuration, tempTicket));
					}		
				}
			}
		}
		
		
		int aTime, dTime;
		DateTime arrival, nextDeparture;
		
		for(FlightPlan possibleLeg2Plan : unconcludedList){
			tempParams.setDepartureAirportCode(possibleLeg2Plan.getLegs()[0].getForFlight().getDepartureAirport().getCode().toCharArray());
			tempParams.setDepartureAirportCode(possibleLeg2Plan.getLegs()[0].getForFlight().getDepartureAirport().getCode().toCharArray());
			
			
			
			searchResults = serverInterface.GetDepartingFlights(tempParams);
			
			
			//leg 2
			for(Flight possibleLeg2 : searchResults.getFlightList()){
				
				if(possibleLeg2.getArrivalAirport().getCode().equals(userParams.getArrivalAirportCode())){
					
				}
				else{
					//leg 3
					
					lastChanceResults = serverInterface.GetDepartingFlights(tempParams);
					
					for(Flight possibleLeg3 : lastChanceResults.getFlightList()){
						
						
						
					}
				}
			}
		}
		
		
		
		return new FlightPlans(concludedList);
	}
	
}
