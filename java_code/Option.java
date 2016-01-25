// This class implements the concept of an option

import javax.swing.JFrame;
import org.apache.commons.math3.distribution.*;
import org.math.plot.*;

public class Option_ 
{
	String type = "";
	double d_y = 365.0;
	double strike = 0.0;
	double fees = 0.0;
	int exp_days = 0;
	Stock s;
	
	//Class Constructor
	public Option_(Stock s,String type,double strike,int exp_days,double fees)
	{
		this.s = s;
		this.strike = strike;
		this.exp_days = exp_days;
		this.fees = fees;
		
		if(type.contentEquals("Call")){this.type = "Call";}
		else if(type.contentEquals("Put")){this.type = "Put";}
		else {this.type = "Straddle";}
		
		System.out.println(type+" option loaded!\n");
	}
	
	//Prints out a summary
	public void summary()
	{
		System.out.println("PLAIN VANILLA EUROPEAN "+this.type.toUpperCase()+" OPTION:");
		System.out.println("Underlying stock: "+this.s.name);
		System.out.println("Stock initial price: "+this.s.s0);
		System.out.println("Option strike price: "+this.strike);
		System.out.println("Days before expiration: "+this.exp_days);
		System.out.println("Expected daily drift: "+this.s.drift);
		System.out.println("Expected daily volatility: "+this.s.vol);
		System.out.println("Fees: "+this.fees);
	}
	
	//r = yearly risk free rate
	double monteCarloPrice(int N,double r)
	{
		double avg = 0.0;
		double temp = 0.0;
		double t =0.0;
		for(int i=0; i < N; i++)
		{
			temp = this.s.stochasticIntegral(this.exp_days);
			switch(this.type)
			{
				case "Call":
					t = temp - this.strike - this.fees;
					break;
				case "Put":
					t = this.strike - temp - this.fees;
					break;
				case "Straddle":
					t = Math.max(temp - this.strike,0) + Math.max(this.strike - temp,0) -this.fees;
					break;
			}
			
			if(t>0)
			{
				avg += t*Math.exp(-r*(double)this.exp_days/this.d_y);
			}
		}
		avg = avg/(double)N;
		return avg;
	}
	
	//Black and Scholes formula (It only supports call options right now)
	
	double black_scholes(double r)
	{
									// defaults 0,1
		NormalDistribution N = new NormalDistribution();
		double price = 0.0;
		
		switch(this.type)
		{
		case "Call":
			double d1 = (Math.log(this.s.s0/this.strike)+(r+Math.pow(this.s.vol,2)/2)*this.exp_days)/(this.s.vol*Math.sqrt(this.exp_days));
			double d2 = d1-this.s.vol * Math.sqrt(this.exp_days);
			price = this.s.s0 * N.cumulativeProbability(d1) - this.strike*Math.exp(-r/this.d_y*this.exp_days)*N.cumulativeProbability(d2);
			break;
		default:
			//Add all the other cases
			price = 0;
			
		}
		return price;
	}
	
	//Plot payoffs
	public void plot(int N,double r)
	{
		if(N < 1000)
		{
			Plot2DPanel plt = new Plot2DPanel();
			
			double t = 0.0;
			
			double x[];
			x = new double[N];
			double y[];
			y = new double[N];
			
			for(int i=0;i<N;i++)
			{
				t = Math.max(this.s.stochasticIntegral(1)-this.strike,0)*Math.exp(-r*this.exp_days/this.d_y);
				x[i] = i;
				y[i] = t;
			}
			
			plt.addScatterPlot("Payoffs", x, y);
			//Plot name and frame settings
			JFrame frame = new JFrame("Scatterplot");
			frame.setContentPane(plt);
			frame.setVisible(true);
		}
	}
}
