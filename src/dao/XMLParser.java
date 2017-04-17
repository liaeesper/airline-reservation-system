package dao;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import airport.Airplane;
import airport.Airplanes;
import airport.Airport;
import airport.Airports;
import flight.Flight;
import flight.Flights;
import utils.Price;
import utils.Time;
import utils.Date;
import utils.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class XMLParser {
	
	/**
	 * Builds collection of airports from airports described in XML
	 * 
	 * Parses an XML string to read each of the airports and adds each valid airport 
	 * to the collection. The method uses Java DOM (Document Object Model) to convert
	 * from XML to Java primitives.
	 * 
	 * @param xmlAirports XML string containing set of airports 
	 * @return [possibly empty] collection of Airports in the xml string
	 * @throws NullPointerException included to keep signature consistent with other addAll methods
	 * 
	 * @pre the xmlAirports string adheres to the format specified by the server API
	 * @post the [possibly empty] set of Airports in the XML string are added to collection
	 */
	public static Airports addAll (String xmlAirports) throws NullPointerException {
		Airports airports = Airports.instance;
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airport to our collection
		Document docAirports = buildDomDoc (xmlAirports);
		NodeList nodesAirports = docAirports.getElementsByTagName("Airport");
		
		for (int i = 0; i < nodesAirports.getLength(); i++) {
			Element elementAirport = (Element) nodesAirports.item(i);
			Airport airport = buildAirport (elementAirport);
			
			if (airport.isValid()) {
				airports.add(airport);
			}
		}
		
		return airports;
	}

	/**
	 * Creates an Airport object from a DOM node
	 * 
	 * Processes a DOM Node that describes an Airport and creates an Airport object from the information
	 * @param nodeAirport is a DOM Node describing an Airport
	 * @return Airport object created from the DOM Node representation of the Airport
	 * 
	 * @pre nodeAirport is of format specified by CS509 server API
	 */
	static private Airport buildAirport (Node nodeAirport) {
		/**
		 * Instantiate an empty Airport object
		 */

		String name;
		String code;
		Float latitude;
		Float longitude;
		
		// The airport element has attributes of Name and 3 character airport code
		Element elementAirport = (Element) nodeAirport;
		name = elementAirport.getAttributeNode("Name").getValue();
		code = elementAirport.getAttributeNode("Code").getValue();

		
		// The latitude and longitude are child elements
		Element elementLatLng;
		elementLatLng = (Element)elementAirport.getElementsByTagName("Latitude").item(0);
		latitude = Float.parseFloat(getCharacterDataFromElement(elementLatLng));
		
		elementLatLng = (Element)elementAirport.getElementsByTagName("Longitude").item(0);
		longitude = Float.parseFloat(getCharacterDataFromElement(elementLatLng));

		/**
		 * Update the Airport object with values from XML node
		 */
		
		Airport airport = new Airport(name, code, latitude, longitude);
		airport.setName(name);
		airport.setCode(code);
		airport.setLatitude(latitude);
		airport.setLongitude(longitude);
		
		return airport;
	}
	
	//return a list of flights
	public static Flights addAllFlights (String xmlFlights, String departureairportcode) throws NullPointerException {
		
		ArrayList<Flight> flightlist = new ArrayList<Flight>();
		Airports airports = Airports.instance;
		Airplanes airplanes = Airplanes.instance;
		
		
		Airport DepartureAirport = airports.getAirport(departureairportcode);
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airport to our collection
		Document docFlights = buildDomDoc (xmlFlights);
		NodeList nodesFlights = docFlights.getElementsByTagName("Flight");
		
		for (int i = 0; i < nodesFlights.getLength(); i++) {
			Element elementFlight = (Element) nodesFlights.item(i);
			Flight flight = buildFlight (elementFlight, airports, airplanes, DepartureAirport);
			
			flightlist.add(flight);
		}
		
		return new Flights(DepartureAirport, null, flightlist);
	}
	
	public static Flights addAllArrivingFlights (String xmlFlights, String arrivalairportcode) throws NullPointerException {
		
		ArrayList<Flight> flightlist = new ArrayList<Flight>();
		Airports airports = Airports.instance;
		Airplanes airplanes = Airplanes.instance;
		
		
		Airport ArrivalAirport = airports.getAirport(arrivalairportcode);
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airport to our collection
		Document docFlights = buildDomDoc (xmlFlights);
		NodeList nodesFlights = docFlights.getElementsByTagName("Flight");
		
		for (int i = 0; i < nodesFlights.getLength(); i++) {
			Element elementFlight = (Element) nodesFlights.item(i);
			Flight flight = buildArrivingFlight (elementFlight, airports, airplanes, ArrivalAirport);
			
			flightlist.add(flight);
		}
		
		return new Flights(ArrivalAirport, null, flightlist);
	}

	
	/**
	 * Creates an Flight object from a DOM node
	 */
	static private Flight buildFlight (Node nodeFlight, Airports airports, Airplanes airplanes, Airport departureairport) {
		/**
		 * Instantiate all necessary variables
		 */
		Flight flight;
		int FlightNumber;
		String PlaneType;
		int FlightTime;
		DateTime DepartureTime = new DateTime(null,null);
		DateTime ArrivalTime = new DateTime(null,null);
		Airport ArrivalAirport = null;
		Airplane AirplaneUsed;
		int SeatFc;
		int SeatC;
		Price PriceFc;
		Price PriceC;

		//get next Flights element from xml string
		Element elementFlight = (Element) nodeFlight;
		//get Airplane element
		PlaneType = elementFlight.getAttributeNode("Airplane").getValue();
		//get Flight Time element
		FlightTime = Integer.parseInt(elementFlight.getAttributeNode("FlightTime").getValue());
		//get Flight Number element
		FlightNumber = Integer.parseInt(elementFlight.getAttributeNode("Number").getValue());
		
		//get Airplane info for given plane
		AirplaneUsed = airplanes.getAirplane(PlaneType);
		
		
		// Extract departure element
		Element dep;
		dep = (Element)elementFlight.getElementsByTagName("Departure").item(0);
		Element deptime;
		
		//Extract departure time
		deptime = (Element)dep.getElementsByTagName("Time").item(0);
		String [] departuretime = getCharacterDataFromElement(deptime).split(" ");
		Date depardate = new Date(Integer.parseInt(departuretime[2]), utils.Date.findmonth(departuretime[1]), Integer.parseInt(departuretime[0]));
		Time departime = new Time(Integer.parseInt(departuretime[3].substring(0,2)), Integer.parseInt(departuretime[3].substring(3,5)));
		DepartureTime.setDate(depardate);
		DepartureTime.setTime(departime);

		//Extract arrival element
		Element arr;
		Element arrcode;
		arr = (Element)elementFlight.getElementsByTagName("Arrival").item(0);
		//Extract arrival airport code
		arrcode = (Element)arr.getElementsByTagName("Code").item(0);
		ArrivalAirport = airports.getAirport(getCharacterDataFromElement(arrcode));
		Element arrtime;
		
		//Extract arrival time
		arrtime = (Element)arr.getElementsByTagName("Time").item(0);
		String [] arrivaltime = getCharacterDataFromElement(arrtime).split(" ");
		Date arrvdate = new Date(Integer.parseInt(arrivaltime[2]), utils.Date.findmonth(arrivaltime[1]), Integer.parseInt(arrivaltime[0]));
		Time arrvtime = new Time(Integer.parseInt(arrivaltime[3].substring(0,2)), Integer.parseInt(arrivaltime[3].substring(3,5)));
		ArrivalTime.setDate(arrvdate);
		ArrivalTime.setTime(arrvtime);
		
		// Extract remaining first class and coach seats
		Element seat;
		Element firstclass;
		Element coach;
		seat = (Element)elementFlight.getElementsByTagName("Seating").item(0);
		firstclass = (Element)seat.getElementsByTagName("FirstClass").item(0);
		PriceFc = new Price(new BigDecimal(firstclass.getAttributeNode("Price").getValue().substring(1)));
		SeatFc = AirplaneUsed.getFCSeats() - Integer.parseInt(XMLParser.getCharacterDataFromElement(firstclass));
		coach = (Element)seat.getElementsByTagName("Coach").item(0);
		PriceC = new Price(new BigDecimal(coach.getAttributeNode("Price").getValue().substring(1)));
		SeatC = AirplaneUsed.getCSeats() - Integer.parseInt(XMLParser.getCharacterDataFromElement(coach));
		
		
		/**
		 * Update the Airport object with values from XML node
		 */
		flight = new Flight(departureairport, FlightNumber, PlaneType, FlightTime, DepartureTime, ArrivalAirport, ArrivalTime, SeatFc, SeatC, PriceFc, PriceC);
		
		return flight;
	}
	
	static private Flight buildArrivingFlight (Node nodeFlight, Airports airports, Airplanes airplanes, Airport arrivalairport) {
		/**
		 * Instantiate all necessary variables
		 */
		Flight flight;
		int FlightNumber;
		String PlaneType;
		int FlightTime;
		DateTime DepartureTime = new DateTime(null,null);
		DateTime ArrivalTime = new DateTime(null,null);
		Airport DepartureAirport = null;
		Airplane AirplaneUsed;
		int SeatFc;
		int SeatC;
		Price PriceFc;
		Price PriceC;

		//get next Flights element from xml string
		Element elementFlight = (Element) nodeFlight;
		//get Airplane element
		PlaneType = elementFlight.getAttributeNode("Airplane").getValue();
		//get Flight Time element
		FlightTime = Integer.parseInt(elementFlight.getAttributeNode("FlightTime").getValue());
		//get Flight Number element
		FlightNumber = Integer.parseInt(elementFlight.getAttributeNode("Number").getValue());
		
		//get Airplane info for given plane
		AirplaneUsed = airplanes.getAirplane(PlaneType);
		
		
		// Extract departure element
		Element dep;
		Element depcode;
		dep = (Element)elementFlight.getElementsByTagName("Departure").item(0);
		depcode = (Element)dep.getElementsByTagName("Code").item(0);
		DepartureAirport = airports.getAirport(getCharacterDataFromElement(depcode));
		Element deptime;
		
		//Extract departure time
		deptime = (Element)dep.getElementsByTagName("Time").item(0);
		String [] departuretime = getCharacterDataFromElement(deptime).split(" ");
		Date depardate = new Date(Integer.parseInt(departuretime[2]), utils.Date.findmonth(departuretime[1]), Integer.parseInt(departuretime[0]));
		Time departime = new Time(Integer.parseInt(departuretime[3].substring(0,2)), Integer.parseInt(departuretime[3].substring(3,5)));
		DepartureTime.setDate(depardate);
		DepartureTime.setTime(departime);

		//Extract arrival element
		Element arr;
		arr = (Element)elementFlight.getElementsByTagName("Arrival").item(0);
		//Extract arrival airport code
		Element arrtime;
		
		//Extract arrival time
		arrtime = (Element)arr.getElementsByTagName("Time").item(0);
		String [] arrivaltime = getCharacterDataFromElement(arrtime).split(" ");
		Date arrvdate = new Date(Integer.parseInt(arrivaltime[2]), utils.Date.findmonth(arrivaltime[1]), Integer.parseInt(arrivaltime[0]));
		Time arrvtime = new Time(Integer.parseInt(arrivaltime[3].substring(0,2)), Integer.parseInt(arrivaltime[3].substring(3,5)));
		ArrivalTime.setDate(arrvdate);
		ArrivalTime.setTime(arrvtime);
		
		// Extract remaining first class and coach seats
		Element seat;
		Element firstclass;
		Element coach;
		seat = (Element)elementFlight.getElementsByTagName("Seating").item(0);
		firstclass = (Element)seat.getElementsByTagName("FirstClass").item(0);
		PriceFc = new Price(new BigDecimal(firstclass.getAttributeNode("Price").getValue().substring(1)));
		SeatFc = AirplaneUsed.getFCSeats() - Integer.parseInt(XMLParser.getCharacterDataFromElement(firstclass));
		coach = (Element)seat.getElementsByTagName("Coach").item(0);
		PriceC = new Price(new BigDecimal(coach.getAttributeNode("Price").getValue().substring(1)));
		SeatC = AirplaneUsed.getCSeats() - Integer.parseInt(XMLParser.getCharacterDataFromElement(coach));

		/**
		 * Update the Airport object with values from XML node
		 */
		flight = new Flight(DepartureAirport, FlightNumber, PlaneType, FlightTime, DepartureTime, arrivalairport, ArrivalTime, SeatFc, SeatC, PriceFc, PriceC);
		
		return flight;
	}
	
	//return a list of flights
	public static Airplanes addAllAirplanes (String xmlAirplanes) throws NullPointerException {
		Airplanes airplanes = Airplanes.instance;
		
		Document docAirplanes = buildDomDoc (xmlAirplanes);
		NodeList nodesAirplanes = docAirplanes.getElementsByTagName("Airplane");
		
		for (int i = 0; i < nodesAirplanes.getLength(); i++) {
			Element elementAirplane = (Element) nodesAirplanes.item(i);
			Airplane airplane = buildAirplane (elementAirplane);
			if (airplane.isValid()){
				airplanes.add(airplane);
			}
		}
		
		return airplanes;
	
	}
	
	static private Airplane buildAirplane (Node nodeAirplane) {
		/**
		 * Instantiate an empty Airplane object
		 */
		Airplane airplane;

		String Manufacturer;
		String Model;
		int FCSeats;
		int CSeats;

		
		Element elementAirplane = (Element) nodeAirplane;
		Manufacturer = elementAirplane.getAttributeNode("Manufacturer").getValue();
		Model = elementAirplane.getAttributeNode("Model").getValue();
		
		
		Element firstclass;
		firstclass = (Element)elementAirplane.getElementsByTagName("FirstClassSeats").item(0);
		FCSeats = Integer.parseInt(XMLParser.getCharacterDataFromElement(firstclass));
		
		Element coach;
		coach = (Element)elementAirplane.getElementsByTagName("CoachSeats").item(0);
		CSeats = Integer.parseInt(XMLParser.getCharacterDataFromElement(coach));
		
	

		airplane = new Airplane(Manufacturer, Model, FCSeats, CSeats);

		return airplane;
	}
	
	/**
	 * Builds a DOM tree form an XML string
	 * 
	 * Parses the XML file and returns a DOM tree that can be processed
	 * 
	 * @param xmlString XML String containing set of objects
	 * @return DOM tree from parsed XML or null if exception is caught
	 */
	static private Document buildDomDoc (String xmlString) {
		/**
		 * load the xml string into a DOM document and return the Document
		 */
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(xmlString));
			
			return docBuilder.parse(inputSource);
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieve character data from an element if it exists
	 * 
	 * @param e is the DOM Element to retrieve character data from
	 * @return the character data as String [possibly empty String]
	 */
	private static String getCharacterDataFromElement (Element e) {
		Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	        CharacterData cd = (CharacterData) child;
	        return cd.getData();
	      }
	      return "";
	}
}
