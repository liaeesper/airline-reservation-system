package driver;

import airport.Airport;
import flight.Flights;
import flight.Flight;
import airport.Airports;
import plans.SearchParams;
import dao.ServerInterface;
<<<<<<< HEAD
import plans.SearchParams;
import utils.Date;
<<<<<<< HEAD
import utils.Time;
=======
import utils.Price;
>>>>>>> origin/Z
=======
import user.UserInterface;
>>>>>>> R

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
<<<<<<< HEAD
<<<<<<< HEAD
		// Create ServerInterface object
		ServerInterface Server = new ServerInterface();

		// Try to get a list of airports. 
		Airports airports = Server.PopulateAirports();
=======
		UserInterface userInt = new UserInterface();
		SearchParams userParams;
		ServerInterface resSys = new ServerInterface();
		resSys.PopulateAirports();

		userInt.DisplaySearch();
		userParams = userInt.HandleSearch();
		userInt.DisplaySearchResultsProto(userParams);//resSys.GetFlights(userParams));
		
		
		/*
		// Try to get a list of airports
		Airports airports = resSys.PopulateAirports();
>>>>>>> R
		for (Airport airport : airports) {
			System.out.println(airport);
=======
		ServerInterface resSys = new ServerInterface();
>>>>>>> origin/Z
		}
		System.out.println("done");
<<<<<<< HEAD
		
		
		//Test Get departing flights
		String xmlAirports = Server.GetDepartingFlights(Params);
		System.out.println(xmlAirports);
		
=======
		*/
>>>>>>> R
	}
	
}
