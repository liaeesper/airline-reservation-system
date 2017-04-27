package utils;
/**
 * DateTime bundles the Time and Date utility classes into a single class for convenience.
 * Arrival and departure dates and times for all flights are stored using the DateTime class.
 * @author Team G
 */
public class DateTime {
	private Date date;
	private Time time;
	
	public void setDate(Date inDate){
		this.date = inDate;
	}
	
	public void setTime(Time inTime){
		this.time = inTime;
	}
	
	
	public Date getDate(){
		return this.date;
	}
	
	public Time getTime(){
		return this.time;
	}
	
	
	public DateTime(Date inDate, Time inTime){
		this.date = inDate;
		this.time = inTime;
	}
	
	/**
	 * Checks that a "previous" flight has already landed by the time the next departure occurs
	 * Assumes the DateTimes are within the same month and year
	 * @prevArrival DateTime of the previous flight leg's arrival time
	 * @nextDeparture DateTime of the next flight leg's departure time
	 * @return true if prevArrival occurred before nextDeparture, false if not
	 */
	public boolean HappenedAfter(DateTime nextDeparture){
		
		if(nextDeparture.getDate().getDay() > date.getDay()){
			return true;
		}
		else if(nextDeparture.getDate().getDay() < date.getDay()){
			return false;
		}
		else{
			if(nextDeparture.getTime().getHours() > time.getHours()){
				return true;
			}
			else if(nextDeparture.getTime().getHours() < time.getHours()){
				return false;
			}
			else{
				if(nextDeparture.getTime().getMinutes() > time.getMinutes()){
					return true;
				}
				else if(nextDeparture.getTime().getMinutes() < time.getMinutes()){
					return false;
				}
			}
		}
		return true;//dates are equal
	}
}
