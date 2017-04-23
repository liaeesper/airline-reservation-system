package airport;

/**
 * Contains information about a given airplane including the manufacturer, model, number of first class seats, and number of coach seats.
 * @author Team G
 *
 */
public class Airplane {
	private String Manufacturer;
	private String Model;
	private int FCSeats; //# of first class seats
	private int CSeats; //# of coach seats
	
	public void setManufacturer(String manufacturer){
		this.Manufacturer = manufacturer;
	}
	
	public void setModel(String model){
		this.Model = model;
	}
	
	public void setFCSeats(int fcseats){
		this.FCSeats = fcseats;
	}
	
	public void setCSeats(int cseats){
		this.CSeats = cseats;
	}
	
	public String getManufacturer(){
		return this.Manufacturer;
	}
	
	public String getModel(){
		return this.Model;
	}
	
	public int getFCSeats(){
		return this.FCSeats;
	}
	
	public int getCSeats(){
		return this.CSeats;
	}
	
	/**
	 * Checks if an airplane object is valid. An airplane object is valid if it has a manufacturer, a model,
	 * and at least 1 seat of either seating type (coach or first class)
	 * @return True if the airplane is valid, else false
	 */
	public boolean isValid(){
		//airplane with no manufacturer is not valid
		if ((this.Manufacturer == null) || (this.Manufacturer == "")){
			return false;
		}
		//airplane with no model type is not valid
		if ((this.Model == null) ||(this.Model == "")){
			return false;
		}
		//airplane with no seats is not valid
		if (this.FCSeats == 0 && this.CSeats == 0){
			return false;
		}
		//airplane with negative seats is not valid
		if (this.FCSeats < 0){
			return false;
		}
		//airplane with negative seats is not valid
		if (this.CSeats < 0){
			return false;
		}
		//otherwise airplane is valid
		return true;
	}
	
	public Airplane(String manufacturer, String model, int fcseats, int cseats){
		this.Manufacturer = manufacturer;
		this.Model = model;
		this.FCSeats = fcseats;
		this.CSeats = cseats;
	}
}
