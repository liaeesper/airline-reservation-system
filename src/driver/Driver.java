package driver;

import airport.Airport;
import airport.Airports;
import dao.ServerInterface;

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
		System.out.println("done");
	}
	
}