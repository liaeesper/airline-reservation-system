package user;

import flight.Flight;
import flight.Flights;
import airport.Airports;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.Reservation;
import plans.SearchParams;
import utils.Date;
import utils.Time;
import java.util.Scanner;

public class UserInterface {
	public void DisplaySearch(){
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
	}
	
	public void DisplaySearchResults(FlightPlans flightList){
		
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
	
	public SearchParams HandleSearch(){
		Scanner sc = new Scanner(System.in);
		String input1 = sc.nextLine();
		String input2 = sc.nextLine();
		char[] departureAirportCode = new char[3];
		Date departureDate;
		
		// parse input1, airport code
		input1.getChars(0, 3, departureAirportCode, 0);
		// parse input2, date
		int dmonth = Integer.valueOf(input2.substring(0, 2)); 
		int dday = Integer.valueOf(input2.substring(3, 5)); 
		int dyear = Integer.valueOf(input2.substring(6)); 

		departureDate = new Date(dday, dmonth, dyear);

		SearchParams userParams = new SearchParams(departureDate, departureAirportCode);
		return userParams;
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
