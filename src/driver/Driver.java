package driver;

import airport.Airport;
import airport.Airports;
import plans.SearchParams;
import dao.ServerInterface;
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
		userInt.DisplaySearchResultsProto(userParams);//resSys.GetFlights(userParams));
		
		
		/*
		// Try to get a list of airports
		Airports airports = resSys.PopulateAirports();
		for (Airport airport : airports) {
			System.out.println(airport.toString());
		}
		System.out.println("done");
		*/
	}
	
}