package airport;

import java.util.ArrayList;

public class Airports extends ArrayList<Airport> {
	private static final long serialVersionUID = 1L;
	
	public Airport getAirport(String code){

		for (int i=0; i < this.size(); i++) {
			if(this.get(i).getCode().equals(code)) {
				return this.get(i);
			}
		}
		return null;
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
