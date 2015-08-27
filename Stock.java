// Stock class: implements a stock

import java.awt.Color;
import java.util.Random;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

public class Stock 
{
	Random generator = new Random();
	String name = "";
	double s0 = 0;
	double drift = 0;
	double vol = 0;
	
	public Stock(String name,double s0,double drift,double vol)
	{
		this.name = name;
		this.s0 = s0;
		this.drift = drift;
		this.vol = vol;
		System.out.println("Stock "+name+" loaded.\n");
	}
	
	public double stochasticIntegral(int periods)
	{
		double w = this.generator.nextGaussian();
		double price = this.s0;
		
		for(int i=0; i<periods; i++)
		{
			price += price*this.drift + price*this.vol*w;
			w = this.generator.nextGaussian();
		}
		return price;
	}
	
	public void randomWalkPlot(int N,int days)
	{
		Plot2DPanel plt = new Plot2DPanel();
		Random generator = new Random();
		
		double x[];
		x = new double[days];
		double y[];
		y = new double[days];
		double t = this.s0;
		double temp = this.s0;
		double w = generator.nextGaussian();
		
		for(int i=0;i<N;i++)
		{
			for(int k=0;k<days;k++)
			{
				t += temp*this.drift + temp*this.vol*w;
				temp = t;
				w = generator.nextGaussian();
				y[k] = t;
				x[k] = k;
			}
			plt.addLinePlot(null,x, y);
		}
		
		double p0 = this.s0;
		double y_[];
		y_ = new double[days];
		for(int k=0; k < days; k++)
		{
			p0 += p0*this.drift;
			y_[k] = p0;
		}
		plt.addLinePlot(null,x,y_);
		
		//Plot name and frame settings
		JFrame frame = new JFrame("Plot");
		frame.setContentPane(plt);
		frame.setVisible(true);
	}
}
