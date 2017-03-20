package user;

import flight.Flights;
import airport.Airports;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.Reservation;
import plans.SearchParams;
import utils.Date;
import utils.Time;

public class UserInterface {
	public void DisplaySearch(){
		System.out.println("The valid airport codes are as follows: ");
		for(int i = 0; i< airport.Airports.instance.size(); i++){
			System.out.print(airport.Airports.instance.get(i).getCode());
			if((i+1) == airport.Airports.instance.size()){
				System.out.print(".\n");
			}
			else if((i+1) % 5 == 0 && i != 0){
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
	
	public void DisplaySearchResultsProto(SearchParams userParams){
		// temp, just verifies the values were recorded		
		System.out.println(String.valueOf(userParams.getDepartureAirportCode()));
		System.out.println(userParams.getDepartureDate().getMonth() + "/" 
					+ userParams.getDepartureDate().getDay() + "/" 
					+ userParams.getDepartureDate().getYear());
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
		String input1 = System.console().readLine();
		String input2 = System.console().readLine();
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
