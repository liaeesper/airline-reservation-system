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
		String dAirport, aAirport;
		dAirport = originPlan.getLegs().get(0).getForFlight().getDepartureAirport().getCode();
		
		
		for(Flight newF: unfiltered){
			aAirport = newF.getArrivalAirport().getCode();
			
			arrival = originPlan.getLegs().get(level - 1).getForFlight().getArrivalTime();
			departure = newF.getDepartureTime();
			
			if(!HappenedBefore(arrival, departure) && getLayoverTime(arrival, departure) != -1 && EnoughSeats(newF, originPlan.getLegs().get(level).getSeatType()) && !dAirport.equals(aAirport)){
				filtered.add(new Flight(newF));				
			}		
		}
		return filtered;
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
	
	public ArrayList<FlightPlan> GenerateFlightLegs(FlightPlan unconcluded, SearchParams userParams, int level){
		ServerInterface serverInterface = new ServerInterface();
		int aTime, dTime;
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		Flights searchResults = new Flights();
		SearchParams tempParams = userParams;
		int flightDuration;
		ArrayList<Ticket> tempTicket;
		FlightPlan tempNewFlightPlan;
		BigDecimal price;// = new BigDecimal(0.00);

		char[] tempAirportCode;
		char[] aAirportCode = userParams.getArrivalAirportCode();
		
		ArrayList<Flight> filteredListLeg = new ArrayList<Flight>();
		

		ArrayList<Flight> tempSResults = new ArrayList<Flight>();
		int fTime, lTime;
		
		tempParams.setArrivalAirportCode(userParams.getArrivalAirportCode());
		tempParams.setDepartureAirportCode(unconcluded.getLegs().get(level - 1).getForFlight().getArrivalAirport().getCode().toCharArray());
		tempParams.setDepartureDate(unconcluded.getLegs().get(level - 1).getForFlight().getArrivalTime().getDate());
		aTime = unconcluded.getLegs().get(level - 1).getForFlight().getArrivalTime().getTime().getTimeInMinutes();
		dTime = aTime + 4*60;
		
		searchResults.setFlightList((serverInterface.GetDepartingFlights(tempParams).getFlightList()));
		

		//layover over midnight
		if(dTime >= 24*60){
			tempParams.setDepartureDate(tempParams.getDepartureDate().IncrementDate());
			tempSResults = new ArrayList<Flight>();
			tempSResults.addAll(searchResults.getFlightList());
			tempSResults.addAll(serverInterface.GetDepartingFlights(tempParams).getFlightList());
			searchResults.setFlightList(tempSResults);
			
		}
		
		filteredListLeg = FilterFlightsList(unconcluded, searchResults.getFlightList(), level);
		
		for(Flight possibleLeg : filteredListLeg){
			
			fTime = getFlightDuration(possibleLeg);
			lTime = getLayoverTime(unconcluded.getLegs().get(level - 1).getForFlight().getArrivalTime(), possibleLeg.getDepartureTime());
			
			
			tempAirportCode = possibleLeg.getArrivalAirport().getCode().toCharArray();
			
			tempTicket = new ArrayList<Ticket>();
			tempTicket.add(new Ticket(unconcluded.getLegs().get(0).getSeatType(), unconcluded.getLegs().get(0).getForFlight()));
			
			price = new BigDecimal(0.0);
			
			if(level == 1){
				tempTicket.add(new Ticket(userParams.getSeatType(), possibleLeg));
				tempTicket.add(new Ticket());
				
				if(userParams.getSeatType() == 'C'){
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
				
				if(userParams.getSeatType() == 'C'){
					
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
		ServerInterface serverInterface = new ServerInterface();
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		ArrayList<ArrayList<FlightPlan>> InitialLists = new ArrayList<ArrayList<FlightPlan>>();
		Flights searchResults = new Flights();

		
		//leg 1
		searchResults.setFlightList(serverInterface.GetDepartingFlights(userParams).getFlightList());
		InitialLists = FindInitialLists(searchResults, userParams);
		concludedList = InitialLists.get(0);
		unconcludedList = InitialLists.get(1);
		
		
		//other legs
		for(FlightPlan unconcluded : unconcludedList){
			concludedList.addAll(GenerateFlightLegs(unconcluded, userParams, 1));
		}
		
		
		return new FlightPlans(concludedList);
	}
	
	public FlightPlans GenerateFlightPlansA(SearchParams userParams){
		ServerInterface serverInterface = new ServerInterface();
		ArrayList<FlightPlan> concludedList = new ArrayList<FlightPlan>();
		ArrayList<FlightPlan> unconcludedList = new ArrayList<FlightPlan>();
		ArrayList<ArrayList<FlightPlan>> InitialLists = new ArrayList<ArrayList<FlightPlan>>();
		Flights searchResults = new Flights();

		
		//leg 1
		searchResults.setFlightList(serverInterface.GetDepartingFlights(userParams).getFlightList());
		InitialLists = FindInitialLists(searchResults, userParams);
		concludedList = InitialLists.get(0);
		unconcludedList = InitialLists.get(1);
		
		
		//other legs
		for(FlightPlan unconcluded : unconcludedList){
			concludedList.addAll(GenerateFlightLegs(unconcluded, userParams, 1));
		}
		
		
		return new FlightPlans(concludedList);
	}
	
	
	public ArrayList<FlightPlans> GeneratorManager(SearchParams searchParams){
		
		ArrayList<FlightPlans> Lists = new ArrayList<FlightPlans>();
		SearchParams returnParams = new SearchParams();
		
		if(searchParams.getDepartureDate() != null){
			Lists.add(GenerateFlightPlansD(searchParams));
		}
		else{
			Lists.add(GenerateFlightPlansA(searchParams));
		}
		
		if(searchParams.getIsRoundTrip()){
			returnParams.SetReturnParams(searchParams);
			
			if(searchParams.getRDepartureDate() != null){
				Lists.add(GenerateFlightPlansD(returnParams));
			}
			else{
				Lists.add(GenerateFlightPlansA(returnParams));
			}			
		}
		
		return Lists;
	}

	
	
}
