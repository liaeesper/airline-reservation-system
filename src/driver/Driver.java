package driver;

import java.math.BigDecimal;
import java.util.ArrayList;

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
import plans.FlightPlans;
import plans.Reservation;
import plans.FlightPlan;
import plans.Ticket;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		UserInterface userInt = new UserInterface();
		SearchParams userParams;
		ServerInterface resSys = new ServerInterface();
		resSys.PopulateAirports();
		resSys.PopulateAirplanes();

		userInt.DisplaySearch();
		userParams = userInt.HandleSearch();
		Flights flightList = resSys.GetDepartingFlights(userParams);
		Flight myflight = flightList.getFlightList().get(1);
		//userInt.DisplaySearchResultsProto(userParams);//resSys.GetFlights(userParams));

		Ticket myticket = new Ticket('c',myflight);
		
		//Ticket[] mytickets = null;
		Ticket[] mytickets = new Ticket[] {myticket};
		
		
		FlightPlan myflightplan = new FlightPlan(1, myflight.getPriceC(), myflight.getFlightTime(), mytickets);
		Reservation newreserve = new Reservation(false, false, myflightplan, null);
		resSys.lock();
		resSys.ReserveTicket(newreserve);
		resSys.unlock();
	}
	
}
