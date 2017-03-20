package airport;

import java.util.ArrayList;

public class Airports extends ArrayList<Airport> {
	private static final long serialVersionUID = 1L;
	public static Airports instance =  new Airports();
	
	private Airports(){
		// is this necessary?
	}
}

/*
public class Airports {
	
	private ArrayList<Airport> AirportList;
	
	public void setAirportList(ArrayList<Airport> airportList){
		this.AirportList = airportList;
	}
	
	public ArrayList<Airport> getAirportList(){
		return this.AirportList;
	}
	
	public Airports(){
		AirportList = new ArrayList<Airport>();
	}
	
	public Airports(ArrayList<Airport> airportList){
		this.AirportList = airportList;
	}
	
	public void add(Airport airport){
		AirportList.add(airport);
	}
	
}
*/
