package driver;

import airport.Airport;
import flight.Flights;
import flight.Flight;
import airport.Airports;
import dao.ServerInterface;
import plans.SearchParams;
import utils.Date;
import utils.Price;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServerInterface resSys = new ServerInterface();

		// Try to get a list of airports
		Airports airports = resSys.PopulateAirports();
		for (Airport airport : airports) {
			System.out.println(airport.toString());
		}
		//Try to get a list of flights
		//SearchParams nothing = new SearchParams();
		Flights flights = resSys.GetFlights();
		for (Flight flight : flights.getFlightList()) {
			System.out.println("Departure airport " + flight.getDepartureAirport());
			System.out.println("Flight Number  " + Integer.toString(flight.getFlightNumber()));
			System.out.println("Arrival airport " + flight.getArrivalAirport());
			System.out.println("Plane type " + flight.getPlaneType());
			System.out.println("Flight Time " + Integer.toString(flight.getFlightTime()));
			System.out.println("First class price " + flight.getPriceFc().getMoney().toString());
			System.out.println("First class seats " + Integer.toString(flight.getSeatFc()));
			System.out.println("Coach seat price " + flight.getPriceC().getMoney().toString());
			System.out.println("Get coach seats " + Integer.toString(flight.getSeatC()));
			System.out.println();
			
		}
		System.out.println("done");
	}
	
}