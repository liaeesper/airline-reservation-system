package dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import airport.Airports;
import flight.Flights;
import flight.AllFlights;
import plans.FlightPlans;
import plans.Reservation;
import plans.SearchParams;
import utils.QueryFactory;
import dao.XMLParser;
import flight.Flight;


public class ServerInterface {
	private final String ServerLocation = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	private String TeamName = "TeamG";
	

	public String getURL(){
		return this.ServerLocation;
	}
	
	public Airports PopulateAirports(){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		String xmlAirports;
		Airports airports;

		try {
			/**
			 * Create an HTTP connection to the server for a GET 
			 */
			url = new URL(ServerLocation + QueryFactory.getAirports(TeamName));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", TeamName);

			/**
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
		airports = XMLParser.addAll(xmlAirports);
		return airports;
	}
	
	
	/**
	 * Lock the database for updating by the specified team. The operation will fail if the lock is held by another team.
	 * 
	 * @param teamName is the name of team requesting server lock
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
	 * 
	 * The server interface to unlock the server interface uses HTTP POST protocol
	 * 
	 * @param teamName is the name of the team holding the lock
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

			
	//this is an impossible function, because it returns Flights which 		
	public AllFlights GetFlights(){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		
		String testdate = "2017_5_5";
		Airports currair = PopulateAirports();
		
		String xmlFlights;
		String xmlAirplanes;
		
		//construct empty AllFlights 
		AllFlights allFlights = new AllFlights();
		
		URL urltwo;
		HttpURLConnection connectiontwo;
		BufferedReader readertwo;
		String linetwo;
		StringBuffer resulttwo = new StringBuffer();
		/**
		* Create an HTTP connection to the server for a GET 
		*/
		try{
			urltwo = new URL(ServerLocation + QueryFactory.getAirplanes(TeamName));
			connectiontwo = (HttpURLConnection) urltwo.openConnection();
			connectiontwo.setRequestMethod("GET");
			connectiontwo.setRequestProperty("User-Agent", TeamName);


			int responseCodetwo = connectiontwo.getResponseCode();
			if (responseCodetwo >= HttpURLConnection.HTTP_OK) {
			InputStream inputStreamtwo = connectiontwo.getInputStream();
			String encodingtwo = connectiontwo.getContentEncoding();
			encodingtwo = (encodingtwo == null ? "UTF-8" : encodingtwo);
	
			readertwo = new BufferedReader(new InputStreamReader(inputStreamtwo));
			while ((linetwo = readertwo.readLine()) != null) {
				resulttwo.append(linetwo);
			}
			readertwo.close();
			}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}


		xmlAirplanes = resulttwo.toString();
		
		for(int i=0; i < currair.size(); i++)		
			try {
				/**
				* Create an HTTP connection to the server for a GET 
				*/
				url = new URL(ServerLocation + QueryFactory.getsomeFlights(TeamName, currair.get(i).getCode(), testdate));
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent", TeamName);
				StringBuffer result = new StringBuffer();
				/**
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
					xmlFlights = result.toString();
					
					allFlights.addAll(XMLParser.addAllFlights(xmlFlights, currair));
					
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		return allFlights;
	}
	
	public FlightPlans MakeFlightPlans(SearchParams userParams){
		return null;
	}
	
	public boolean ReserveTicket(Reservation plan){
		return false;
	}
	
	public FlightPlans ToLocal(FlightPlans rawTimePlans){
		return null;
	}
	
	public void FailReserve(){
	
	}
	
	
	
	
}
