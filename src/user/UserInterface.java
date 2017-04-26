package user;

import flight.Flight;
import flight.Flights;
import dao.FlightPlansGenerator;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.Reservation;
import plans.SearchParams;
import gui.LoadingGui;
import gui.SearchGui;
import gui.SearchResultsGui;

import java.util.ArrayList;

public class UserInterface {
	public static UserInterface instance =  new UserInterface();

	public void DisplaySearch(){
		new SearchGui();
	}
	
	public void DisplaySearchResults(ArrayList<FlightPlans> flightList, SearchParams userParams){
		new SearchResultsGui(flightList, new ArrayList<FlightPlan>(),false, 0, userParams);
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
	 * then calls display flights. Also closes the processing message after the flight list is created.
	 */
	public void HandleSearch(SearchParams userParams, LoadingGui loadingPage){
		FlightPlansGenerator plansGenerator = new FlightPlansGenerator();
		
		userParams.convertToGMT();
		
		ArrayList<FlightPlans> flightList = plansGenerator.GeneratorManager(userParams);
		
		loadingPage.dispose();
		
		DisplaySearchResults(flightList, userParams);
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
