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

		UserInterface userInt = UserInterface.instance;
		SearchParams userParams;
		ServerInterface resSys = ServerInterface.instance;
		resSys.PopulateAirports();

		userInt.DisplaySearch();
		
		//FlightPlansGenerator plansGenerator = new FlightPlansGenerator();
		
		//FlightPlans flightList = plansGenerator.GenerateFlightPlans(userParams);
		//userInt.DisplaySearchResults(flightList);
		
		//userParams = userInt.HandleSearch();
		//Flights flightList = resSys.GetDepartingFlights(userParams);
		//userInt.DisplaySearchResultsProto(flightList);//resSys.GetFlights(userParams));
		
		
		/*
		UserInterface userInt = new UserInterface();
		//SearchParams userParams;
		SearchParams userParams = new SearchParams();
		ServerInterface resSys = new ServerInterface();
		FlightPlansGenerator plansGenerator = new FlightPlansGenerator();
		resSys.PopulateAirports();

		//userParams = userInt.HandleSearch();
		
		userParams.setDepartureAirportCode("BOS".toCharArray());
		userParams.setArrivalAirportCode("TPA".toCharArray());
		userParams.setDepartureDate(new Date(5, 5, 2017));
		Time time[] = {new Time(4, 50), new Time(18, 30)};
		userParams.setDepartureTime(time);
		userParams.setSeatType('C');
		
		
		FlightPlans flightList = plansGenerator.GenerateFlightPlans(userParams);
		userInt.DisplaySearchResults(flightList);
		
		//Flights flightList = resSys.GetDepartingFlights(userParams);
		//userInt.DisplaySearchResultsProto(flightList);//resSys.GetFlights(userParams));
		*/
		}

		
		
		
	
	
}
