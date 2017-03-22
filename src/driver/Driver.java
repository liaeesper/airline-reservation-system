package driver;

import airport.Airport;
import airport.Airports;
import dao.ServerInterface;
import plans.SearchParams;
import utils.Date;
import utils.Time;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create ServerInterface object
		ServerInterface Server = new ServerInterface();

		// Try to get a list of airports. 
		Airports airports = Server.PopulateAirports();
		for (Airport airport : airports) {
			System.out.println(airport);
		}
		System.out.println("done");
		
		//Create search parameters to test flights query
		Date departureDate = new Date(05, 05, 2017);
		Time[] departureTime = null;
		char[] departureAirportCode = {'B','O','S'};
		char[] arrivalAirportCode = {'B','O','S'};
		Date arrivalDate = null;
		Time[] arrivalTime = null;
		boolean isRoundTrip = true;
		Date rDepartureDate = null;
		Time[] rDepartureTime = null;
		Date rArrivalDate = null;
		Time[] rArrivalTime = null;
		char seatType = 'N';
		SearchParams Params = new SearchParams(departureDate, departureTime, departureAirportCode, arrivalDate, arrivalTime,
				arrivalAirportCode, isRoundTrip, rDepartureDate, rDepartureTime, rArrivalDate, rArrivalTime, seatType);
		
		//Test Get departing flights
		String xmlAirports = Server.GetDepartingFlights(Params);
		System.out.println(xmlAirports);
		
	}
	
}