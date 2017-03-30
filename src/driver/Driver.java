package driver;

import airport.Airport;
import flight.Flights;
import flight.Flight;
import airport.Airports;
import plans.FlightPlans;
import plans.SearchParams;
import dao.FlightPlansGenerator;
import dao.ServerInterface;
import plans.SearchParams;
import utils.Date;
import utils.Time;
import utils.Price;
import user.UserInterface;


public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		UserInterface userInt = new UserInterface();
		SearchParams userParams;
		ServerInterface resSys = new ServerInterface();
		FlightPlansGenerator plansGenerator = new FlightPlansGenerator();
		resSys.PopulateAirports();

		//userInt.DisplaySearch();
		userParams = userInt.HandleSearch();
		
		FlightPlans flightList = plansGenerator.GenerateFlightPlans(userParams);
		userInt.DisplaySearchResults(flightList);
		
		//Flights flightList = resSys.GetDepartingFlights(userParams);
		//userInt.DisplaySearchResultsProto(flightList);//resSys.GetFlights(userParams));
		
		}

		
		
		
	
	
}
