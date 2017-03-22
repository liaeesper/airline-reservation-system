package driver;

import airport.Airport;
import flight.Flights;
import flight.Flight;
import airport.Airports;
import dao.ServerInterface;
import plans.SearchParams;
import utils.Date;
<<<<<<< HEAD
import utils.Time;
=======
import utils.Price;
>>>>>>> origin/Z

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
<<<<<<< HEAD
		// Create ServerInterface object
		ServerInterface Server = new ServerInterface();

		// Try to get a list of airports. 
		Airports airports = Server.PopulateAirports();
		for (Airport airport : airports) {
			System.out.println(airport);
=======
		ServerInterface resSys = new ServerInterface();
>>>>>>> origin/Z
		}
		System.out.println("done");
		
		
		//Test Get departing flights
		String xmlAirports = Server.GetDepartingFlights(Params);
		System.out.println(xmlAirports);
		
	}
	
}
