package airport;

public class Airport {
	
	private String Name;
	private String Code;
	private float Latitude;
	private float Longitude;
	
	public void setName(String name){
		if(isValidName(name))
			this.Name = name;
		else
			throw new IllegalArgumentException (name);
	}
	
	public void setCode(String code){
			
		if (isValidCode(code))
			this.Code = code;
		else
			throw new IllegalArgumentException (code);
	}
	
	public void setLatitude(float latitude){
		if (isValidLatitude(latitude))
			this.Latitude = latitude;
		else
			throw new IllegalArgumentException (Float.toString(latitude));
	}
	
	public void setLatitude (String latitude) {
		if (isValidLatitude(latitude))
			Latitude = Float.parseFloat(latitude);
		else
			throw new IllegalArgumentException (latitude);
	}
	
	public void setLongitude (float longitude) {
		if (isValidLongitude(longitude))
			this.Longitude = longitude;
		else
			throw new IllegalArgumentException (Float.toString(longitude));
	}
	
	public void setLongitude (String longitude) {
		if (isValidLongitude(longitude))
			Longitude = Float.parseFloat(longitude);
		else
			throw new IllegalArgumentException (longitude);
	}
	
	
	public String getName(){
		return this.Name;
	}
	
	public String getCode(){
		return this.Code;
	}
	
	public float getLatitude(){
		return this.Latitude;
	}
	
	public float getLongitude(){
		return this.Longitude;
	}
	
	/**
	 * Default constructor
	 * 
	 * Constructor without params. Requires object fields to be explicitly
	 * set using setter methods
	 * 
	 * @pre None
	 * @post member attributes are initialized to invalid default values
	 */	
	public Airport(){
		this.Name = "";
		this.Latitude = (float)90.00;
		this.Longitude = (float)180.00;
	}
	
	/**
	 * Initializing constructor.
	 * 
	 * All attributes are initialized with input values
	 *  
	 * @param name The human readable name of the airport
	 * @param code The 3 letter code for the airport
	 * @param latitude The north/south coordinate of the airport 
	 * @param longitude The east/west coordinate of the airport
	 * 
	 * @pre code is a 3 character string, name is not empty, latitude and longitude are valid values
	 * @post member attributes are initialized with input parameter values
	 * @throws IllegalArgumentException is any parameter is invalid
	 */
	public Airport(String name, String code, float latitude, float longitude){
		if (!isValidName(name))
			throw new IllegalArgumentException(name);
		if (!isValidCode(code)) 
			throw new IllegalArgumentException(code);
		if (!isValidLatitude(latitude))
			throw new IllegalArgumentException(Float.toString(latitude));
		if (!isValidLongitude(longitude))
			throw new IllegalArgumentException(Float.toString(longitude));
		
		this.Name = name;
		this.Code = code;
		this.Latitude = latitude;
		this.Longitude = longitude;
	}
	
	/**
	 * Initializing constructor with all params as type String. Converts latitude and longitude
	 * values to required float format.
	 * 
	 * @param name The human readable name of the airport
	 * @param code The 3 letter code for the airport
	 * @param latitude is the string representation of latitude decimal format 
	 * @param longitude is the String representation of the longitude in decimal format
	 * 
	 * @pre the latitude and longitude are valid String representations of valid lat/lon values
	 * @post member attributes are initialized with input parameter values
	 * @throws IllegalArgumentException is any parameter is invalid
	 */
	public Airport (String name, String code, String latitude, String longitude) {
		float tmpLatitude, tmpLongitude;
		try {
			tmpLatitude = Float.parseFloat(latitude);
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException ("Latitude must be between -90.0 and +90.0", ex);
		}
		
		try {
			tmpLongitude = Float.parseFloat(latitude);
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException ("Longitude must be between -180.0 and +180.0", ex);
		}
		
		this.Name = name;
		Code = code;
		Latitude = tmpLatitude;
		Longitude = tmpLongitude;
	}
	
	/**
	 * Convert object to printable string of format "Code, (lat, lon), Name"
	 * 
	 * @return the object formatted as String to display
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(Code).append(", ");
		sb.append("(").append(String.format("%1$.3f", Latitude)).append(", ");
		sb.append(String.format("%1$.3f", Longitude)).append("), ");
		sb.append(Name);

		return sb.toString();
	}
	
	@Override
	public boolean equals (Object obj) {
		// every object is equal to itself
		if (obj == this)
			return true;
		
		// null not equal to anything
		if (obj == null)
			return false;
		
		// can't be equal if obj is not an instance of Airport
		if (!(obj instanceof Airport)) 
			return false;
		
		// if all fields are equal, the Airports are the same
		Airport rhs = (Airport) obj;
		if ((rhs.Name.equals(Name)) &&
				(rhs.Code.equals(Code)) &&
				(rhs.Latitude == Latitude) &&
				(rhs.Longitude == Longitude)) {
			return true;
		}
		
		return false;	
	}
	
	public boolean isValid() {
		
		// If the name isn't valid, the object isn't valid
		if ((Name == null) || (Name == ""))
			return false;
		
		// If we don't have a 3 character code, object isn't valid
		if ((Code == null) || (Code.length() != 3))
			return false;
		
		// Verify latitude and longitude are within range
		if ((Latitude > (float)90.0) || (Latitude < (float)-90.0) ||
			(Longitude > (float)180.0) || (Longitude < (float)-180.0)) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Check for invalid airport name.
	 * 
	 * @param name is the name of the airport to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidName (String name) {
		// If the name is null or empty it can't be valid
		if ((name == null) || (name == ""))
			return false;
		return true;
	}
	
	/**
	 * Check for invalid 3 character airport code
	 * 
	 * @param code is the airport code to validate
	 * @return false if null or not 3 characters in length, else assume valid and return true
	 */
	public boolean isValidCode (String code) {
		// If we don't have a 3 character code it can't be valid valid
		if ((code == null) || (code.length() != 3))
			return false;
		return true;
	}
	
	/**
	 * Check if latitude is valid
	 * 
	 * @param latitude is the latitude to validate
	 * @return true if within valid range for latitude
	 */
	public boolean isValidLatitude (float latitude) {
		// Verify latitude is within valid range
		if ((latitude > (float)90.0) || (latitude < (float)-90.0))
			return false;
		return true;
	}
	
	/**
	 * Check if latitude is valid.
	 * 
	 * @param latitude is the latitude to validate represented as a String
	 * @return true if within valid range for latitude
	 */
	public boolean isValidLatitude (String latitude) {
		float lat;
		try {
			lat = Float.parseFloat(latitude);
		} catch (NullPointerException | NumberFormatException ex) {
			return false;
		}
		return isValidLatitude (lat);
	}

	/**
	 * Check if longitude is valid
	 * 
	 * @param longitude is the longitude to validate
	 * @return true if within valid range for longitude
	 */
	public boolean isValidLongitude (float longitude) {
		// Verify longitude is within valid range
		if ((longitude > (float)180.0) || (longitude < (float)-180.0))
			return false;
		return true;
	}
	
	/**
	 * Check if longitude is valid
	 * 
	 * @param longitude is the longitude to validate represented as a String
	 * @return true if within valid range for longitude
	 */
	public boolean isValidLongitude (String longitude) {
		float lon;
		try {
			lon = Float.parseFloat(longitude);
		} catch (NullPointerException | NumberFormatException ex) {
			return false;
		}
		return isValidLongitude (lon);
	}
}
