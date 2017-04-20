package airport;

import java.util.ArrayList;

/**
 * Contains a collection (ArrayList) of the full set of Airport objects retrieved from WPI's server
 * @author Team G
 *
 */
public class Airports extends ArrayList<Airport> {
	private static final long serialVersionUID = 1L;
	public static Airports instance =  new Airports();
	
	//return airport corresponding to airport code
	public Airport getAirport(String code){

		for (int i=0; i < this.size(); i++) {
			if(this.get(i).getCode().equals(code)) {
				return this.get(i);
			}
		}
		return null;
	}

	public Airports(){
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
