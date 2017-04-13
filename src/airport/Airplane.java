package airport;

public class Airplane {
	private String Manufacturer;
	private String Model;
	private int FCSeats;
	private int CSeats;
	
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
	//is the airplane valid?
	public boolean isValid(){
		//airplane with no manufacturer is not valid
		if ((this.Manufacturer == null) || (this.Manufacturer == "")){
			return false;
		}
		//airplane with no model type is not valid
		if ((this.Model == null) ||(this.Model == "")){
			return false;
		}
		//airplane with no first class seats is not valid
		if (this.FCSeats == 0){
			return false;
		}
		//airplane with no coach seats is not valid
		if (this.CSeats == 0){
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
