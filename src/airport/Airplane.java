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
	
	public Airplane(String manufacturer, String model, int fcseats, int cseats){
		this.Manufacturer = manufacturer;
		this.Model = model;
		this.FCSeats = fcseats;
		this.CSeats = cseats;
	}
}
