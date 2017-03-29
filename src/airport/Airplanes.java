package airport;

import java.util.ArrayList;

public class Airplanes extends ArrayList<Airplane> {
	private static final long serialVersionUID = 1L; 
	
	private ArrayList<Airplane> AirplaneList;
	
	public void setAirplaneList(ArrayList<Airplane> airplanelist){
		this.AirplaneList = airplanelist;
	}
	
	public ArrayList<Airplane> getAirplaneList(){
		return this.AirplaneList;
	}
	
	public Airplanes(ArrayList<Airplane> airplanelist){
		this.AirplaneList = airplanelist;
	}
}
