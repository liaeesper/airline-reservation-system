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

import airport.Airport;
import airport.Airports;
import flight.Flight;
import flight.Flights;
import utils.Price;
import utils.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
		
		Airport DepartureAirport = airports.getAirport(departureairportcode);
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airport to our collection
		Document docFlights = buildDomDoc (xmlFlights);
		NodeList nodesFlights = docFlights.getElementsByTagName("Flight");
		
		for (int i = 0; i < nodesFlights.getLength(); i++) {
			Element elementFlight = (Element) nodesFlights.item(i);
			Flight flight = buildFlight (elementFlight, airports, DepartureAirport);
			
			flightlist.add(flight);
		}
		
		return new Flights(DepartureAirport, null, flightlist);
	}
	
	/**
	 * Creates an Flight object from a DOM node
	 */
	static private Flight buildFlight (Node nodeFlight, Airports airports, Airport departureairport) {
		/**
		 * Instantiate an empty Airport object
		 */

		Flight flight;
		int FlightNumber;
		String PlaneType;
		int FlightTime;
		Date DepartureTime = null;
		Airport ArrivalAirport = null;
		Date ArrivalTime = null;
		//int FlightLength;
		int SeatFc;
		int SeatC;
		Price PriceFc;
		Price PriceC;
		Airport DepartureAirport;
		//construct date/time parser
		DateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm z");
		
		Element elementFlight = (Element) nodeFlight;
		PlaneType = elementFlight.getAttributeNode("Airplane").getValue();
		FlightTime = Integer.parseInt(elementFlight.getAttributeNode("FlightTime").getValue());
		FlightNumber = Integer.parseInt(elementFlight.getAttributeNode("Number").getValue());
		
		
		// The code and time are child elements
		Element dep;
		//Element depcode;
		dep = (Element)elementFlight.getElementsByTagName("Departure").item(0);
		//depcode = (Element)dep.getElementsByTagName("Code").item(0);
		//DepartureAirport = airports.getAirport(getCharacterDataFromElement(depcode));
		DepartureAirport = departureairport;
		Element deptime;
		try{	
			deptime = (Element)dep.getElementsByTagName("Time").item(0);
			DepartureTime = sdf.parse(getCharacterDataFromElement(deptime));
		}
		catch(ParseException e){
			System.out.println(e.toString());
		}
		
		Element arr;
		Element arrcode;
		arr = (Element)elementFlight.getElementsByTagName("Arrival").item(0);
		arrcode = (Element)arr.getElementsByTagName("Code").item(0);
		ArrivalAirport = airports.getAirport(getCharacterDataFromElement(arrcode));
		Element arrtime;
		
		try{
			arrtime = (Element)arr.getElementsByTagName("Time").item(0);
			ArrivalTime = sdf.parse(getCharacterDataFromElement(arrtime));
		}
		catch(ParseException e){
			System.out.println(e.toString());
		}
		
		Element seat;
		Element firstclass;
		Element coach;
		seat = (Element)elementFlight.getElementsByTagName("Seating").item(0);
		firstclass = (Element)seat.getElementsByTagName("FirstClass").item(0);
		PriceFc = new Price(new BigDecimal(firstclass.getAttributeNode("Price").getValue().substring(1)));
		SeatFc = Integer.parseInt(XMLParser.getCharacterDataFromElement(firstclass));
		coach = (Element)seat.getElementsByTagName("Coach").item(0);
		PriceC = new Price(new BigDecimal(coach.getAttributeNode("Price").getValue().substring(1)));
		SeatC = Integer.parseInt(XMLParser.getCharacterDataFromElement(coach));
		
		/**
		 * Update the Airport object with values from XML node
		 */
		flight = new Flight(DepartureAirport, FlightNumber, PlaneType, FlightTime, DepartureTime, ArrivalAirport, ArrivalTime, SeatFc, SeatC, PriceFc, PriceC);
		
		return flight;
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
