package utils;
import java.math.BigDecimal;

//idk which format to use so here's code for both
public class Price {
	//weird that we have floats in some places and doubles in others.
	//maybe use bigdecimal
	private BigDecimal Money;
	
	public void setMoney(BigDecimal money){
		this.Money = money;
	}
	
	public BigDecimal getMoney(){
		return this.Money;
	}
	
	public Price(BigDecimal money){
		this.Money = money;
	}
	
	/*
	private int Dollars;
	private int Cents;
	
	public void setDollars(int dollars){
		this.Dollars = dollars;
	}
	
	public void setCents(int cents){
		this.Cents = cents;
	}
	
	
	public int getDollars(){
		return this.Dollars;
	}
	
	public int getCents(){
		return this.Cents;
	}
	
	public Price(int dollars, int cents){
		this.Dollars = dollars;
		this.Cents = cents;
	}
	*/
}
