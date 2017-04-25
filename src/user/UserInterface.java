package user;

import flight.Flight;
import flight.Flights;
import airport.Airports;
import dao.FlightPlansGenerator;
import dao.ServerInterface;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.Reservation;
import plans.SearchParams;
import utils.Date;
import utils.Time;
import gui.LoadingGui;
import gui.SearchGui;
import gui.SearchResultsGui;

import java.util.ArrayList;

public class UserInterface {
	public static UserInterface instance =  new UserInterface();

	public void DisplaySearch(){
		SearchGui search = new SearchGui();
	}
	
	public void DisplaySearchResults(ArrayList<FlightPlans> flightList){
		SearchResultsGui searchResults = new SearchResultsGui(flightList, new ArrayList<FlightPlan>(),false, 0);
	}
	
	public void DisplaySearchResultsProto(Flights flightList){
		System.out.print("\n");
		if(flightList.getFlightList().size() == 0){
			System.out.print("No results :(\n");
		}
		int count = 1;
		for(Flight flight: flightList.getFlightList()){
			System.out.print(String.valueOf(count) + ".\n");
			count++;
			System.out.print(flight.toString());
			System.out.print("\n");
		}
		
		
	}
	
	public void DisplayAlternativeSeating(FlightPlan flight){
		
	}
	
	public void DisplayNoFlights(){
		
	}
	
	public void DisplayConfirmation(Reservation reservation){
		
	}
	
	public void DisplayFinalConfirmation(){
		
	}
	
	/**
	 * HandleSearch()
	 * takes user search parameters as input and passes them to the server interface,
	 * then calls display flights
	 */
	public void HandleSearch(SearchParams userParams, LoadingGui loadingPage){
		// TODO
		// convert times to GMT here
		
		ServerInterface resSys = ServerInterface.instance;
		//Flights flightList = resSys.GetDepartingFlights(userParams);
		//DisplaySearchResultsProto(flightList);
		FlightPlansGenerator plansGenerator = new FlightPlansGenerator();
		
		// TODO
		userParams.convertToGMT();
		
		ArrayList<FlightPlans> flightList = plansGenerator.GeneratorManager(userParams);
		
		loadingPage.dispose();
		
		DisplaySearchResults(flightList);
		return;
	}
	
	public FlightPlan HandleSelection(){
		return null;
	}
	
	public void HandleConfirmation(Reservation reservation){
		
	}
	
	public void HandleSortByTime(FlightPlans flightList){
		
	}
	
	public void HandleSortByPrice(FlightPlans flightList){
		
	}
}
