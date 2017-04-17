package plans;


import java.util.ArrayList;

public class FlightPlans {
	private ArrayList<FlightPlan> FlightPlansList;
	private ArrayList<FlightPlan> FlightPlansListSortedPLeast;
	private ArrayList<FlightPlan> FlightPlansListSortedPMost;
	private ArrayList<FlightPlan> FlightPlansListSortedTLeast;
	private ArrayList<FlightPlan> FlightPlansListSortedTMost;
	
	public void setFlightPlansList(ArrayList<FlightPlan> flightPlansList){
		this.FlightPlansList = flightPlansList;
	}
	
	public ArrayList<FlightPlan> getFlightPlansList(int mode){
		switch (mode) {
			case 0:
				return this.FlightPlansList;
			case 1:
				return this.FlightPlansListSortedPLeast;
			case 2:
				return this.FlightPlansListSortedPMost;
			case 3:
				return this.FlightPlansListSortedTLeast;
			case 4:
				return this.FlightPlansListSortedTMost;
		}
		return FlightPlansList;
	}
	
	public FlightPlans(ArrayList<FlightPlan> flightPlansList){
		this.FlightPlansList = flightPlansList;
	}
	
	public void sortByLowestPrice(){
		FlightPlan flightPlan;
		FlightPlansListSortedPLeast = new ArrayList<FlightPlan>(FlightPlansList);
		
		for(int i = 0; i < FlightPlansListSortedPLeast.size(); i++){
			for(int j = 0; j < FlightPlansListSortedPLeast.size(); j++){
				if(FlightPlansListSortedPLeast.get(i).getTotalPrice().getMoney().compareTo(FlightPlansListSortedPLeast.get(j).getTotalPrice().getMoney()) == -1) {
					flightPlan = FlightPlansListSortedPLeast.get(i);
					FlightPlansListSortedPLeast.set(i, FlightPlansListSortedPLeast.get(j));
					FlightPlansListSortedPLeast.set(j, flightPlan);
				}
			}
		}
	}
	
	public void sortByHighestPrice(){
		FlightPlan flightPlan;
		FlightPlansListSortedPMost = new ArrayList<FlightPlan>(FlightPlansList);
		
		for(int i = 0; i < FlightPlansListSortedPMost.size(); i++){
			for(int j = 0; j < FlightPlansListSortedPMost.size(); j++){
				if(FlightPlansListSortedPMost.get(i).getTotalPrice().getMoney().compareTo(FlightPlansListSortedPMost.get(j).getTotalPrice().getMoney()) == 1) {
					flightPlan = FlightPlansListSortedPMost.get(i);
					FlightPlansListSortedPMost.set(i, FlightPlansListSortedPMost.get(j));
					FlightPlansListSortedPMost.set(j, flightPlan);
				}
					
			}
			
		}
	}
	
	public void sortByLeastTime(){
		FlightPlan flightPlan;
		FlightPlansListSortedTLeast = new ArrayList<FlightPlan>(FlightPlansList);
		
		for(int i = 0; i < FlightPlansListSortedTLeast.size(); i++){
			for(int j = 0; j < FlightPlansListSortedTLeast.size(); j++){
				if(FlightPlansListSortedTLeast.get(i).getTotalTime() < FlightPlansListSortedTLeast.get(j).getTotalTime()) {
					flightPlan = FlightPlansListSortedTLeast.get(i);
					FlightPlansListSortedTLeast.set(i, FlightPlansListSortedTLeast.get(j));
					FlightPlansListSortedTLeast.set(j, flightPlan);
				}
					
			}
			
		}
	}
	
	public void sortByMostTime(){
		FlightPlan flightPlan;
		FlightPlansListSortedTMost = new ArrayList<FlightPlan>(FlightPlansList);
		
		for(int i = 0; i < FlightPlansListSortedTMost.size(); i++){
			for(int j = 0; j < FlightPlansListSortedTMost.size(); j++){
				if(FlightPlansListSortedTMost.get(i).getTotalTime() > FlightPlansListSortedTMost.get(j).getTotalTime()) {
					flightPlan = FlightPlansListSortedTMost.get(i);
					FlightPlansListSortedTMost.set(i, FlightPlansListSortedTMost.get(j));
					FlightPlansListSortedTMost.set(j, flightPlan);
				}
					
			}
			
		}
	}
	
	public String toString(int mode){
		int count = 1;
		String flightPlansString = "";
		sortByLeastTime();
		for(FlightPlan flightPlan: getFlightPlansList(mode)){
			flightPlansString += String.valueOf(count) + ". " + flightPlan.toString() + "\n--------------------------------------\n";
			count++;
		}
		return flightPlansString;
	}
	
}
