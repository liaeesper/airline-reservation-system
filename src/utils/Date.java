package utils;

public class Date {
	private int Day, Month, Year;
	
	public void setDay(int day){
		this.Day = day;
	}
	
	public void setMonth(int month){
		this.Month = month;
	}
	
	public void setYear(int year){
		this.Year = year;
	}
	
	
	public int getDay(){
		return this.Day;
	}
	
	public int getMonth(){
		return this.Month;
	}
	
	public int getYear(){
		return this.Year;
	}
	
	public static int findmonth(String somemonth){
		switch(somemonth.substring(0,3)){
			case "Jan" : return 1;
			case "Feb" : return 2;
			case "Mar" : return 3;
			case "Apr" : return 4;
			case "May" : return 5;
			case "Jun" : return 6;
			case "Jul" : return 7;
			case "Aug" : return 8;
			case "Sep" : return 9;
			case "Oct" : return 10;
			case "Nov" : return 11;
			case "Dec" : return 12;
		}
		return 0;
	}
	
	public Date(int day, int month, int year){
		this.Day = day;
		this.Month = month;
		this.Year = year;
	}
}
