package plans;

import java.util.ArrayList;

import utils.Price;

public class FlightPlan {
	private int NumberLegs;
	private Price TotalPrice;
	private int TotalTime;
	//private Ticket[] Legs;
	private ArrayList<Ticket> Legs;
	
	public void setNumberLegs(int numberLegs){
		this.NumberLegs = numberLegs;
	}
	
	public void setTotalPrice(Price totalPrice){
		this.TotalPrice = totalPrice;
	}
	
	public void setTotalTime(int totalTime){
		this.TotalTime = totalTime;
	}
	
	public void setLegs(ArrayList<Ticket> legs){
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
	
	public ArrayList<Ticket> getLegs(){
		return this.Legs;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("Number of Legs " + this.NumberLegs).append(" |  $");
		sb.append(String.valueOf(this.TotalPrice.getMoney()) + " | ");
		
		int minutes = this.TotalTime;
		
		
		if(minutes >= 60){
			sb.append(String.valueOf(minutes / 60) + " hours ");
			minutes = minutes % 60;
		}
		
		
		sb.append(String.valueOf(minutes) + " minutes\n");
		
		int lTime, dTime, aTime;
		for(int i = 0; i < NumberLegs; i++){
			if(i >= 1){
				lTime = 0;
				aTime = this.Legs.get(i-1).getForFlight().getArrivalTime().getTime().getTimeInMinutes();
				dTime = this.Legs.get(i).getForFlight().getDepartureTime().getTime().getTimeInMinutes();
				
				if(dTime <= aTime){
					lTime = ((24*60) - aTime) + dTime;
				}
				else{
					lTime = dTime - aTime;
				}
				
				sb.append("\nLayover Time ");
				
				
				if(lTime >= 60){
					sb.append(String.valueOf(lTime / 60) + " hours ");
					lTime = lTime % 60;
				}
				
				
				sb.append(String.valueOf(lTime) + " minutes\n\n");
				
			}
			
			sb.append(this.Legs.get(i).getForFlight().toString(this.Legs.get(i).getSeatType()) + "Seat Type " + this.Legs.get(i).getSeatType() + "\n");
		}
		
		
		return sb.toString();
	}
	
	
	public FlightPlan(int numberLegs, Price totalPrice, int totalTime, ArrayList<Ticket> legs){
		this.NumberLegs = numberLegs;
		this.TotalPrice = totalPrice;
		this.TotalTime = totalTime;
		this.Legs = legs;
	}
}
