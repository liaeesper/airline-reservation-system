package user;

import flight.Flight;
import flight.Flights;
import airport.Airports;
import dao.FlightPlansGenerator;
import dao.ServerInterface;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.Reservation;
import plans.SearchParams;
import utils.Date;
import utils.Time;
import gui.LoadingGui;
import gui.SearchGui;
import gui.SearchResultsGui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
	public static UserInterface instance =  new UserInterface();

	public void DisplaySearch(){
		SearchGui search = new SearchGui();
		/*
		String content = "<html>"
				+ "<title>Search</title>"
				+ "<body>"
				+ "<form action=\"searchServlet\" method=\"post\">"
				+ "<input type=\"text\" name=\"foo\" />"
				+ "<input type=\"text\" name=\"bar\" />"
				+ "<input type=\"submit\">"
				+ "</form>"
				+ "</body>"
				+ "</html>";
		File file = new File("search.jsp");
		try {
			// opens with system default browser
		    Files.write(file.toPath(), content.getBytes());
		    Desktop.getDesktop().browse(file.toURI());
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		}
		*/
		
		
		/*
		System.out.println("The valid airport codes are as follows: ");
		for(int i = 0; i< airport.Airports.instance.size(); i++){
			System.out.print(airport.Airports.instance.get(i).getCode());
			if((i+1) == airport.Airports.instance.size()){
				System.out.print(".\n");
			}
			else if((i+1) % 5 == 0){
				System.out.print(",\n");
			}
			else{
				System.out.print(", ");
			}
		}
		System.out.println("Please enter a departure airport and departure date.\n"
							+"Enter a code of the form 'XXX' for the airport, click enter once,"
							+"enter a date in the form MM/DD/YYYY, and hit enter again.");
		*/
		//System.out.println("Please enter a departure airport and arrival airport, then a departure date and time window.\n"
		//					+"Enter a code of the form 'XXX' for the airport, click enter once,"
		//					+"Enter a date in the form MM/DD/YYYY, and hit enter again.");
	}
	
	public void DisplaySearchResults(ArrayList<FlightPlans> flightList){
		SearchResultsGui searchResults = new SearchResultsGui(flightList, new ArrayList<FlightPlan>(),false);
		/*
		int count = 1;
		flightList.sortByLeastTime();
		for(FlightPlan flightPlan: flightList.getFlightPlansList()){
			System.out.print(String.valueOf(count) + ". " + flightPlan.toString() + "\n--------------------------------------\n");
			count++;
		}
		*/
	}
	
	public void DisplaySearchResultsProto(Flights flightList){
		System.out.print("\n");
		if(flightList.getFlightList().size() == 0){
			System.out.print("No results :(\n");
		}
		int count = 1;
		for(Flight flight: flightList.getFlightList()){
			System.out.print(String.valueOf(count) + ".\n");
			count++;
			System.out.print(flight.toString());
			System.out.print("\n");
		}
		
		
	}
	
	public void DisplayAlternativeSeating(FlightPlan flight){
		
	}
	
	public void DisplayNoFlights(){
		
	}
	
	public void DisplayConfirmation(Reservation reservation){
		
	}
	
	public void DisplayFinalConfirmation(){
		
	}
	
	/**
	 * HandleSearch()
	 * takes user search parameters as input and passes them to the server interface,
	 * then calls display flights
	 */
	public void HandleSearch(SearchParams userParams, LoadingGui loadingPage){
		// TODO
		// convert times to GMT here
		
		ServerInterface resSys = ServerInterface.instance;
		//Flights flightList = resSys.GetDepartingFlights(userParams);
		//DisplaySearchResultsProto(flightList);
		FlightPlansGenerator plansGenerator = new FlightPlansGenerator();
		
		// TODO
		//userParams.convertToLocal();
		
		ArrayList<FlightPlans> flightList = plansGenerator.GeneratorManager(userParams);
		
		loadingPage.dispose();
		
		DisplaySearchResults(flightList);
		return;
		
		/*
		Scanner sc = new Scanner(System.in);
		//String input1 = sc.nextLine();
		//String input2 = sc.nextLine();
		char[] departureAirportCode = new char[3];
		char[] arrivalAirportCode = new char[3];
		Date departureDate;
		Time[] departureTime = new Time[2];
		char seatType;
		
		DisplaySearch();
		
		
		System.out.println("Enter a code of the form 'XXX' for the departure airport, click enter once");
		
		// parse input1, airport code
		sc.nextLine().getChars(0, 3, departureAirportCode, 0);
		
		System.out.println("Enter a code of the form 'XXX' for the arrival airport, click enter once");
		
		sc.nextLine().getChars(0, 3, arrivalAirportCode, 0);
		
		
		System.out.println("Enter a date in the form MM/DD/YYYY for departure date, and hit enter again.");
		
		String input2 = sc.nextLine();
		
		// parse input2, date
		int dmonth = Integer.valueOf(input2.substring(0, 2)); 
		int dday = Integer.valueOf(input2.substring(3, 5)); 
		int dyear = Integer.valueOf(input2.substring(6)); 

		departureDate = new Date(dday, dmonth, dyear);
		
		System.out.println("Enter a date in the form HH:MM military time for earliest departure time, and hit enter again.");
		
		String input3 = sc.nextLine();
		
		int dhour1 = Integer.valueOf(input3.substring(0,2));
		int dminutes1 = Integer.valueOf(input3.substring(3,5));
		
		System.out.println("Enter a date in the form HH:MM military time for latest departure time, and hit enter again.");
		
		
		String input4 = sc.nextLine();
		
		int dhour2 = Integer.valueOf(input4.substring(0,2));
		int dminutes2 = Integer.valueOf(input4.substring(3,5));
		
		departureTime[0] = new Time(dhour1, dminutes1);
		departureTime[1] = new Time(dhour2, dminutes2);
		
		
		System.out.println("Enter a 'C' for coach ticket or 'F' for first class ticket, and hit enter again.");
		
		
		String input5 = sc.nextLine();
		seatType = input5.substring(0,1).charAt(0);
		
		
		//SearchParams userParams = new SearchParams(departureDate, departureAirportCode);
		
		SearchParams userParams = new SearchParams(departureDate, departureTime, departureAirportCode, arrivalAirportCode, seatType);
		
		sc.close();
		
		return userParams;
		*/
	}
	
	public FlightPlan HandleSelection(){
		return null;
	}
	
	public void HandleConfirmation(Reservation reservation){
		
	}
	
	public void HandleSortByTime(FlightPlans flightList){
		
	}
	
	public void HandleSortByPrice(FlightPlans flightList){
		
	}
}
