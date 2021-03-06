package utils;

/**
 * Contains methods that return a string to be appended to URL to form either HTTP GET or HTTP POST request 
 * in order to interact with WPI's server
 * @author Team G
 */
public class QueryFactory {
	
	/**
	 * Return a query string that can be passed to HTTP URL to request list of airports
	 * 
	 * @param teamName is the name of the team to specify the data copy on server
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getAirports(String teamName) {
		return "?team=" + teamName + "&action=list&list_type=airports";
	}
	
	/**
	 * Return a query string that can be passed to HTTP URL to request list of departing flights from a specific airport
	 * 
	 * @param teamName is the name of the team to specify the data copy on server
	 * @param airportCode is the 3 character airport code
	 * @param day is the date in GMT in the form of "yyyy_mm_dd
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getDepartingFlights(String teamName, String airportCode, String day){
		return "?team=" + teamName + "&action=list&list_type=departing&airport=" + airportCode + "&day=" + day; 
	}
	
	/**
	 * Return a query string that can be passed to HTTP URL to request list of flights arriving at a specific airport
	 * 
	 * @param teamName is the name of the team to specify the data copy on server
	 * @param airportCode is the 3 character airport code
	 * @param day is the date in GMT in the form of "yyyy_mm_dd
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getArrivingFlights(String teamName, String airportCode, String day){
		return "?team=" + teamName + "&action=list&list_type=arriving&airport=" + airportCode + "&day=" + day; 
	}
	
	/**
	 * Return a query string that can be passed to HTTP URL to request list of airplanes
	 * 
	 * @param teamName is the name of the team to specify the data copy on server
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getAirplanes(String teamName) {
		return "?team=" + teamName + "&action=list&list_type=airplanes";
	}
	
	/**
	 * Return a query string that can be passed to HTTP URL to reserve a seat
	 * 
	 * @param teamName is the name of the team to specify the data copy on server
	 * @param xmlFlights is the flight in which a seat is being reserved
	 * @return the query String which can be appended to URL to form HTTP POST request
	 */
	public static String reserveSeat(String teamName, String xmlFlights){
		return "team=" + teamName + "&action=buyTickets&flightData=" + xmlFlights;
	}
	
	/**
	 * Lock the server database so updates can be written
	 * 
	 * @param teamName is the name of the team to acquire the lock
	 * @return the String written to HTTP POST to lock server database 
	 */
	public static String lock (String teamName) {
		return "team=" + teamName + "&action=lockDB";
	}
	
	/**
	 * Unlock the server database after updates are written
	 * 
	 * @param teamName is the name of the team holding the lock
	 * @return the String written to the HTTP POST to unlock server database
	 */
	public static String unlock (String teamName) {
		return "team=" + teamName + "&action=unlockDB";
	}
	

}
