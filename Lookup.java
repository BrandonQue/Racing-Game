

public class Lookup
{
	public final static double[] degsToRads = calculateDegreesToRadians();
	public final static double[] sin = calculateSin();
	public final static double[] cos = calculateCos();
	
	
	private static double[] calculateDegreesToRadians() {
		
		double[] degsToRads = new double[360];
		
		for(int i = 0; i < degsToRads.length; i++)
			degsToRads[i] = i * Math.PI / 180;
		
		return degsToRads;
	}
	
	public static double[] calculateSin()
	{
		double[] sin = new double[360];
		
		for(int i = 0; i < sin.length; i++)
			sin[i] = Math.sin(degsToRads[i]);
		
		return sin;
	}

	public static double[] calculateCos()
	{
		double[] cos = new double[360];
		
		for(int i = 0; i < cos.length; i++)
			cos[i] = Math.cos(degsToRads[i]);
		
		return cos;
	}
}