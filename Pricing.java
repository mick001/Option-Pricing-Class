//Main class

public class Pricing 
{
	public static void main(String[] args)
	{
		Stock xyz = new Stock("XYZ",114.64,0.0016273,0.088864);
		Option_ call_ = new Option_(xyz,"Call",100,2,0);
		
		System.out.println("Option price: "+call_.monteCarloPrice(10000,0.033)+"\n");
		System.out.println("BS price: "+call_.black_scholes(0.033)+"\n");
		call_.summary();
		//call_.plot(300,0.033);
		aapl.randomWalkPlot(2, 365);
	}
}
