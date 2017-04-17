package driver;

import java.util.ArrayList;

import airport.Airport;
import flight.Flights;
import flight.Flight;
import airport.Airports;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.Reservation;
import plans.SearchParams;
import plans.Ticket;
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
		//Date departureDate, char[] departureAirportCode

		SearchParams userParams = new SearchParams();
		Date Departuredate = new Date(5,5,2017);
		userParams.setArrivalAirportCode("BOS".toCharArray());
		userParams.setArrivalDate(Departuredate);
		userParams.setIsRoundTrip(false);
		ServerInterface resSys = ServerInterface.instance;
		resSys.PopulateAirports();
		resSys.PopulateAirplanes();


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
		Time time[] = {new Time(16, 50), new Time(22, 30)};
		userParams.setDepartureTime(time);
		userParams.setIsRoundTrip(true);
		userParams.setRDepartureDate(new Date(7, 5, 2017));
		Time time2[] = {new Time(16, 50), new Time(22, 30)};
		userParams.setRDepartureTime(time2);
		userParams.setSeatType('C');
		
		
		ArrayList<FlightPlans> flightLists = plansGenerator.GeneratorManager(userParams);
		userInt.DisplaySearchResults(flightLists.get(0));

		
		//Flights flightList = resSys.GetDepartingFlights(userParams);
		//userInt.DisplaySearchResultsProto(flightList);//resSys.GetFlights(userParams));
		*/
		}

		
		
		
	
	
}
