package plans;

import utils.Price;

public class FlightPlan {
	private int NumberLegs;
	private Price TotalPrice;
	private int TotalTime;
	private Ticket[] Legs;
	
	public void setNumberLegs(int numberLegs){
		this.NumberLegs = numberLegs;
	}
	
	public void setTotalPrice(Price totalPrice){
		this.TotalPrice = totalPrice;
	}
	
	public void setTotalTime(int totalTime){
		this.TotalTime = totalTime;
	}
	
	public void setLegs(Ticket[] legs){
		this.Legs = legs;
	}
	
	
	public int getNumberLegs(){
		return this.NumberLegs;
	}
	
	public Price getTotalPrice(){
		return this.TotalPrice;
	}
	
	public int getTotalTime(){
		return this.TotalTime;
	}
	
	public Ticket[] getLegs(){
		return this.Legs;
	}
	
	
	
	public FlightPlan(int numberLegs, Price totalPrice, int totalTime, Ticket[] legs){
		this.NumberLegs = numberLegs;
		this.TotalPrice = totalPrice;
		this.TotalTime = totalTime;
		this.Legs = legs;
	}
}
