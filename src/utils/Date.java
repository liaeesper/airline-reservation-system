package utils;

/**
 * The Date class is a utility used to store date information (day, month, year) for flights, and search criteria.
 * Additionally, the Date class contains methods to map a month string to an integer, and to increment a the date by one day. 
 * @author Team G
 */

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
	
	public Date() {
		this.Day = 0;
		this.Month = 0;
		this.Year = 0;
	}

	public Date IncrementDate() {
		int day = this.Day;
		int month = this.Month;
		int year = this.Year;

		
		if(day == 31 && month == 12){
			return new Date(1, 1, year+1);
		}
		else if(day == 31 && (month == 1 || month == 3 || month == 5  || month == 7 || month == 8 || month == 10)){
			return new Date(1, month+1, year);
		}
		else if(day == 28 && month == 2){
			return new Date(1, month+1, year);
		}
		else if(day == 30 && (month == 4 || month == 6 || month == 9|| month == 11)){
			return new Date(1, month+1, year);
		}
		else{
			return new Date(day+1, month, year);
		}
		
	}
	
	
	public Date DecrementDate() {
		int day = this.Day;
		int month = this.Month;
		int year = this.Year;

		
		if(day == 1 && month == 1){
			return new Date(31, 12, year-1);
		}
		else if(day == 1 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)){
			return new Date(31, month-1, year);
		}
		else if(day == 1 && month == 3){
			return new Date(28, month-1, year);
		}
		else if(day == 1 && (month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)){
			return new Date(30, month-1, year);
		}
		else{
			return new Date(day-1, month, year);
		}
		
	}
	
	
	
}
