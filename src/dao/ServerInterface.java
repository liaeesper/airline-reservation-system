package dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import airport.Airplanes;
import airport.Airports;
import flight.Flights;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.FlightPlan;
import plans.Reservation;
import plans.SearchParams;
import user.UserInterface;
import plans.Ticket;
import utils.Date;
import utils.DateTime;
import utils.QueryFactory;
import utils.Time;
import dao.XMLParser;
import flight.Flight;

/**
 * ServerInterface queries WPI's server for information about airports, airplanes, and flights.
 * All information is returned from the server as an XML string.
 * Additionally, ServerInterface requests server lock and unlock modes as well as flight reservations.
 * @author Team G
 */
public class ServerInterface {
	private final String ServerLocation = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	private String TeamName = "TeamG";
	public static ServerInterface instance =  new ServerInterface();

	

	public String getURL(){
		return this.ServerLocation;
	}
	
	/**
	 * Queries WPI's server for a list of airports using HTTP GET request. Calls XML parsing methods to parse XML string returned from server.
	 * @return An Airports class containing a list of airports retrieved from WPI's server
	 */
	public Airports PopulateAirports(){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		String xmlAirports;
		Airports airports;

		try {
			//Create an HTTP connection to the server for a GET 
			url = new URL(ServerLocation + QueryFactory.getAirports(TeamName));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", TeamName);

			/*
			 * If response code of SUCCESS read the XML string returned
			 * line by line to build the full return string
			 */
			int responseCode = connection.getResponseCode();
			if (responseCode >= HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "UTF-8" : encoding);

				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		xmlAirports = result.toString();
		
		//call XML parser to generate airports class
		airports = XMLParser.addAllAirports(xmlAirports);
		return airports;
	}
	
	
	/**
	 * Lock the database for updating by the specified team. The operation will fail if the lock is held by another team.
	 * @return true if the server was locked successfully, else false
	 */
	public boolean lock () {
		URL url;
		HttpURLConnection connection;

		try {
			url = new URL(ServerLocation);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", TeamName);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String params = QueryFactory.lock(TeamName);
			
			connection.setDoOutput(true);
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to lock database");
			System.out.println(("\nResponse Code : " + responseCode));
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			
			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			in.close();
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Unlock the database previous locked by specified team. The operation will succeed if the server lock is held by the specified
	 * team or if the server is not currently locked. If the lock is held be another team, the operation will fail.
	 * <p>
	 * The server interface to unlock the server interface uses HTTP POST protocol
	 * @return true if the server was successfully unlocked.
	 */
	public boolean unlock () {
		URL url;
		HttpURLConnection connection;

		try {
			url = new URL(ServerLocation);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			
			String params = QueryFactory.unlock(TeamName);
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
		    
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to unlock database");
			System.out.println(("\nResponse Code : " + responseCode));

			if (responseCode >= HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();

			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Queries WPI's server for a list of flights departing from a specific airport using HTTP GET request.
	 * Calls XML parsing methods to parse XML string returned from server.
	 * @param searchParams is a SearchParams object containing user specified data necessary for requesting flight information from the server. (Airport code, departure date)
	 * @return An Flights class containing a list of flights departing from an airport specified in searchParams and retrieved from WPI's server
	 */
	public Flights GetDepartingFlights(SearchParams searchParams){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		String xmlFlights;
		
		//Parse searchParams
		String airportCode = new String(searchParams.getDepartureAirportCode());
		String day = String.valueOf(searchParams.getDepartureDate().getYear()) + "_" + String.valueOf(searchParams.getDepartureDate().getMonth()) + "_" + String.valueOf(searchParams.getDepartureDate().getDay());
		
		try {
			/*
			 * Create an HTTP connection to the server for a GET 
			 */
			url = new URL(ServerLocation + QueryFactory.getDepartingFlights(TeamName, airportCode, day));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", TeamName);
			
			int responseCode = connection.getResponseCode();
			if (responseCode >= HttpURLConnection.HTTP_OK){
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "UTF-8" : encoding);

				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				reader.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		xmlFlights = result.toString();
		
		//call XML parser to generate Flights class
		Flights flights = XMLParser.addAllDepartingFlights(xmlFlights, airportCode); //need to parse xmlAirports string into Flights object
		return flights;
	}
	
	/**
	 * Queries WPI's server for a list of flights arriving at specific airport using HTTP GET request.
	 * Calls XML parsing methods to parse XML string returned from server.
	 * @param searchParams is a SearchParams object containing user specified data necessary for requesting flight information from the server. (Airport code, arrival date)
	 * @return An Flights class containing a list of flights arriving at an airport specified in searchParams and retrieved from WPI's server
	 */
	public Flights GetArrivingFlights(SearchParams searchParams){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		String xmlFlights;
		
		//Parse searchParams
		String airportCode = new String(searchParams.getArrivalAirportCode());
		String day = String.valueOf(searchParams.getArrivalDate().getYear()) + "_" + String.valueOf(searchParams.getArrivalDate().getMonth()) + "_" + String.valueOf(searchParams.getArrivalDate().getDay());
		
		try {
			/*
			 * Create an HTTP connection to the server for a GET 
			 */
			url = new URL(ServerLocation + QueryFactory.getArrivingFlights(TeamName, airportCode, day));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", TeamName);
			
			int responseCode = connection.getResponseCode();
			if (responseCode >= HttpURLConnection.HTTP_OK){
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "UTF-8" : encoding);

				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				reader.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		xmlFlights = result.toString();
		
		//call XML parser to generate Flights class
		Flights flights = XMLParser.addAllArrivingFlights(xmlFlights, airportCode); //need to parse xmlAirports string into Flights object
		return flights;
	}
	
	/**
	 * Queries WPI's server for a list of airplanes using HTTP GET request.
	 * Calls XML parsing methods to parse XML string returned from server.
	 * @return An Airplanes class containing a list of airplanes retrieved from WPI's server
	 */
	public Airplanes PopulateAirplanes(){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		String xmlAirplanes;
		Airplanes airplanes;
		
		try {
			/*
			 * Create an HTTP connection to the server for a GET 
			 */
			url = new URL(ServerLocation + QueryFactory.getAirplanes(TeamName));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", TeamName);

			/*
			 * If response code of SUCCESS read the XML string returned
			 * line by line to build the full return string
			 */
			int responseCode = connection.getResponseCode();
			if (responseCode >= HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "UTF-8" : encoding);

				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		xmlAirplanes = result.toString();
		//call XML parser to generate Airplanes class
		airplanes = XMLParser.addAllAirplanes(xmlAirplanes);
		return airplanes;
	}
	
	/**
	 * Requests flight reservations from WPI's server for a specific flightPlan using an HTTP POST request. The method considers all legs of a given flightPlan and attempts to reserve a seat for each of the legs.
	 * @param plan contains information about flightPlan that the user has selected for reservation
	 * @return True if the flightPlan was reserved successfully
	 * <p>
	 * False if there are no seats of the chosen seat type left on at least one leg of the flightPlan, or if the database was not locked before request
	 */
	public boolean ReserveTicket(Reservation plan){
		URL url;
		HttpURLConnection connection;
		FlightPlan outgoing = plan.getOutgoingFlight();
		String xmlflights = "<Flights>";
		String seattype;
		
		String flightnum="";
		
		//create xml string for flight reservation - <Flights> <Flight number=DDDDD seating=SEAT_TYPE/> <Flight number=DDDDD seating=SEAT_TYPE/> </Flights>
	    //add all outgoing legs to xml string for flight reservation
		for (int i=0; i < outgoing.getNumberLegs(); i++){
			//if coach seating was selected for this leg, specify "Coach"
			if ((outgoing.getLegs().get(i).getSeatType() == 'C') | (outgoing.getLegs().get(i).getSeatType() == 'c') ){
				seattype = "Coach";
			}
			else{
				//if first class seating was selected, specify "firstclass"
				seattype = "FirstClass";
			}
			//pad the flight number on the left with zeros if it is less than four digits long (for formatting)
			flightnum = Integer.toString(outgoing.getLegs().get(i).getForFlight().getFlightNumber());
			while (flightnum.length() < 4){
				flightnum = "0" + flightnum;
			}
			//<Flight number=DDDDD seating=SEAT_TYPE/>
			xmlflights = xmlflights + "<Flight number=\"" + flightnum + "\" seating=\"" +  seattype + "\"/>";
			
		}
		//add all incoming legs to xml string for flight reservation
		if (plan.getIsRoundTrip()){
			System.out.println("Is round trip");
			FlightPlan incoming = plan.getReturningFlight();
			for (int i=0; i < incoming.getNumberLegs(); i++){
				if ((incoming.getLegs().get(i).getSeatType() == 'c') | (incoming.getLegs().get(i).getSeatType() == 'C') ){
					seattype = "Coach";
				}
				else{
					seattype = "FirstClass";
				}
				
				flightnum = Integer.toString(incoming.getLegs().get(i).getForFlight().getFlightNumber());
				while (flightnum.length() < 4){
					flightnum = "0" + flightnum;
				}
				xmlflights = xmlflights + "<Flight number=\"" + flightnum + "\" seating=\"" +  seattype+ "\"/>";
			}
		}
		xmlflights = xmlflights + "</Flights>";
		
		System.out.print(xmlflights);
		
		try {
			url = new URL(ServerLocation);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", TeamName);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String params = QueryFactory.reserveSeat(TeamName, xmlflights);
			
			//System.out.println("Parameters " + params);
			connection.setDoOutput(true);
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to reserve ticket");
			System.out.println(("\nResponse Code : " + responseCode));
			
			if (responseCode >=300) {
				if (responseCode == 304){
					System.out.println("Your seat could not be reserved because there are no seats of that seat type left on at least one leg of the flight.");
					return false;
				}
				else if (responseCode == 413) {
					System.out.println("Your seat could not be reserved because of an error. The database was not locked before request.");
					return false;
				}
				else {
					System.out.println("Your seat could not be reserved because of an error.");
					return false;
				}
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			
			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			
			System.out.println("Your ticket has been reserved!");
			in.close();
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;

	}
	
	public FlightPlans ToLocal(FlightPlans rawTimePlans){
		return null;
	}
	
	public void FailReserve(){
	
	}
	
	
	
	
}
