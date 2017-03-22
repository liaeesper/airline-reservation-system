package driver;

import airport.Airport;
import flight.Flights;
import flight.Flight;
import airport.Airports;
import plans.SearchParams;
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
		resSys.PopulateAirports();

		userInt.DisplaySearch();
		userParams = userInt.HandleSearch();
		Flights flightList = resSys.GetDepartingFlights(userParams);
		userInt.DisplaySearchResultsProto(flightList);//resSys.GetFlights(userParams));
		
		
		}
		
		
	
	
}
