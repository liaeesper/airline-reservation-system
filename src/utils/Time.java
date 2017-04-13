package utils;

public class Time {
	private int Hours;
	private int Minutes;
	
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
	
	public int getTimeInMinutes(){
		int minutes = 0;
		minutes += 60*this.Hours;
		minutes += this.Minutes;
		
		
		return minutes;
	}
	
}
