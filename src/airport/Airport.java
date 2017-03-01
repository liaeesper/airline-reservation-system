package airport;

public class Airport {
	private String Name;
	private char[] Code;
	private float Latitude;
	private float Longitude;
	
	public void setName(String name){
		this.Name = name;
	}
	
	public void setCode(char[] code){
		this.Code = code;
	}
	
	public void setLatitude(float latitude){
		this.Latitude = latitude;
	}
	
	public void setLongitude(float longitude){
		this.Longitude = longitude;
	}
	
	
	public String getName(){
		return this.Name;
	}
	
	public char[] getCode(){
		return this.Code;
	}
	
	public float getLatitude(){
		return this.Latitude;
	}
	
	public float getLongitutde(){
		return this.Longitude;
	}
	
	public Airport(){
		
	}
	
	public Airport(String name, char[] code, float latitude, float longitude){
		this.Name = name;
		this.Code = code;
		this.Latitude = latitude;
		this.Longitude = longitude;
	}
}
