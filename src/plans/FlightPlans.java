package plans;

import java.util.ArrayList;

public class FlightPlans {
	private ArrayList<FlightPlan> FlightPlansList;
	
	public void setFlightPlansList(ArrayList<FlightPlan> flightPlansList){
		this.FlightPlansList = flightPlansList;
	}
	
	public ArrayList<FlightPlan> getFlightPlansList(){
		return this.FlightPlansList;
	}
	
	public FlightPlans(ArrayList<FlightPlan> flightPlansList){
		this.FlightPlansList = flightPlansList;
	}
	
	public void sortByLowestPrice(){
		FlightPlan flightPlan;
		
		for(int i = 0; i < FlightPlansList.size(); i++){
			for(int j = 0; j < FlightPlansList.size(); j++){
				if(FlightPlansList.get(i).getTotalPrice().getMoney().compareTo(FlightPlansList.get(j).getTotalPrice().getMoney()) == -1) {
					flightPlan = FlightPlansList.get(i);
					FlightPlansList.set(i, FlightPlansList.get(j));
					FlightPlansList.set(j, flightPlan);
				}
					
			}
			
		}
	}
	
	public void sortByHighestPrice(){
		FlightPlan flightPlan;
		
		for(int i = 0; i < FlightPlansList.size(); i++){
			for(int j = 0; j < FlightPlansList.size(); j++){
				if(FlightPlansList.get(i).getTotalPrice().getMoney().compareTo(FlightPlansList.get(j).getTotalPrice().getMoney()) == 1) {
					flightPlan = FlightPlansList.get(i);
					FlightPlansList.set(i, FlightPlansList.get(j));
					FlightPlansList.set(j, flightPlan);
				}
					
			}
			
		}
	}
	
	public void sortByLeastTime(){
		FlightPlan flightPlan;
		
		for(int i = 0; i < FlightPlansList.size(); i++){
			for(int j = 0; j < FlightPlansList.size(); j++){
				if(FlightPlansList.get(i).getTotalTime() < FlightPlansList.get(j).getTotalTime()) {
					flightPlan = FlightPlansList.get(i);
					FlightPlansList.set(i, FlightPlansList.get(j));
					FlightPlansList.set(j, flightPlan);
				}
					
			}
			
		}
	}
	
	public void sortByMostTime(){
		FlightPlan flightPlan;
		
		for(int i = 0; i < FlightPlansList.size(); i++){
			for(int j = 0; j < FlightPlansList.size(); j++){
				if(FlightPlansList.get(i).getTotalTime() > FlightPlansList.get(j).getTotalTime()) {
					flightPlan = FlightPlansList.get(i);
					FlightPlansList.set(i, FlightPlansList.get(j));
					FlightPlansList.set(j, flightPlan);
				}
					
			}
			
		}
	}
	
}
