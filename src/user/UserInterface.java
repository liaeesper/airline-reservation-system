package user;

import flight.Flight;
import flight.Flights;
import dao.FlightPlansGenerator;
import dao.ServerInterface;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.SearchParams;
import gui.LoadingGui;
import gui.SearchGui;
import gui.SearchResultsGui;

import java.util.ArrayList;

import airport.Airplanes;
import airport.Airports;

public class UserInterface {
	public static UserInterface instance =  new UserInterface();

	
	public static void main(String[] args) {
		ServerInterface resSys = ServerInterface.instance;
		Airports.instance = resSys.PopulateAirports();
		Airplanes.instance = resSys.PopulateAirplanes();

		UserInterface.instance.DisplaySearch();
	}
	
	public void DisplaySearch(){
		new SearchGui();
	}
	
	public void DisplaySearchResults(ArrayList<FlightPlans> flightList, SearchParams userParams){
		new SearchResultsGui(flightList, new ArrayList<FlightPlan>(),false, 0, userParams);
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
}
