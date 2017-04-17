package airport;

import java.util.ArrayList;

public class Airplanes extends ArrayList<Airplane> {
	private static final long serialVersionUID = 1L; 
	//create static instance for accessibility
	public static Airplanes instance =  new Airplanes();
	
	private ArrayList<Airplane> AirplaneList;
	

	//get full array of airplanes
	public ArrayList<Airplane> getAirplaneList(){
		return this.AirplaneList;
	}
	//take in model, return corresponding airplane from array
	public Airplane getAirplane(String model){
		for (int i=0; i < this.size(); i++) {
			if(this.get(i).getModel().equals(model)) {
				return this.get(i);
			}
		}
		return null;
	}
	
	public Airplanes(){
	}
}
