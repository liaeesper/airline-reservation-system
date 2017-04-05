package utils;
import java.util.*;

public class Time {
	private int Hours;
	private int Minutes;
	private Map<String, Integer> localConversion = new HashMap<String, Integer>(){{
		put("ATL", -4); //Atlanta
		put("ANC", -8); //Alaska
		put("AUS", -5); //Texas
		put("BWI", -4); //Washington
		put("BOS", -4); //Boston
		put("CLT", -4); //Charlotte
		put("MDW", -5); //Chicago
		put("ORD", -5); //Chicago
		put("CVG", -4); //Cincinnati
		put("CLE", -4); //Cleveland
		put("CMH", -4); //Columbus
		put("DFW", -5); //Dallas-fort worth
		put("DEN", -6); //Denver
		put("DTW", -4); //Detroit
		put("FLL", -4); //Fort Lauderdale
		put("RSW", -4); //Southwest Florida
		put("BDL", -4); //Hartford
		put("HNL", -10); //Honolulu
		put("IAH", -5); //Houston
		put("HOU", -5); //Houston
		put("IND", -4); //Indianapolis
		put("MCI", -5); //Kansas
		put("LAS", -7); //Vegas
		put("LAX", -7); //LA
		put("MEM", -5); //Memphis
		put("MIA", -4); //Miami
		put("MSP", -5); //Minneapolis
		put("BNA", -5); //Nashville
		put("MSY", -5); //New Orleans
		put("JFK", -4); //New York
		put("LGA", -4); //New York
		put("EWR", -4); //Newark
		put("OAK", -7); //Oakland
		put("ONT", -7); //Ontario
		put("MCO", -4); //Orlando
		put("PHL", -7); //Philidelphia
		put("PHX", -7); //Phoenix
		put("PIT", -4); //Pittsburgh
		put("PDX", -7); //Portland
		put("RDU", -4); //Raleigh
		put("SMF", -7); //Sacramento
		put("SLC", -6); //Salt Lake City
		put("SAT", -5); //San Antonio
		put("SAN", -7); //San Diego
		put("SFO", -7); //San Francisco
		put("SJC", -7); //San Jose
		put("SNA", -7); //Santa Ana
		put("SEA", -7); //Seattle
		put("STL", -5); //St Louis
		put("TPA", -4); //Tampa
		put("IAD", -4); //Washington DC
		put("DCA", -4); //Washington DC
	}};
	
	private Map<Integer, String> timeZones = new HashMap<Integer, String>(){{
		put(-4, "EST");
		put(-5, "CDT");
		put(-6, "MDT");
		put(-7, "PDT");
		put(-8, "AKDT");
		put(-10, "HAST");
	}};
	
	public void setHours(int hours){
		this.Hours = hours;
	}
	
	public void setMinutes(int minutes){
		this.Minutes = minutes;
	}
	
	
	public int getHours(){
		return this.Hours;
	}
	
	public int getMinutes(){
		return this.Minutes;
	}
	
	public Time(int hours, int minutes){
		this.Hours = hours;
		this.Minutes = minutes;
	}
	
	public int getLocalHours(String code){
		return this.Hours + this.localConversion.get(code);
	}
	
	public String getTimeZone(String code){
		return this.timeZones.get(this.localConversion.get(code));
	}
	
}
