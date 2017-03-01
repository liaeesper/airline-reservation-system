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
	
	public boolean isValid() {
		
		// If the name isn't valid, the object isn't valid
		if ((Name == null) || (Name == ""))
			return false;
		
		// If we don't have a 3 character code, object isn't valid
		if ((Code == null) || (Code.toString() == ""))
			return false;
		
		// Verify latitude and longitude are within range
		if ((Latitude > 90) || (Latitude < -90) ||
			(Longitude > 180) || (Longitude < -180)) {
			return false;
		}
		
		return true;
	}
}
