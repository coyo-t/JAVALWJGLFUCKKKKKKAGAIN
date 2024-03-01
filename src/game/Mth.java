package game;

public class Mth
{

	public static
	float clampf (float x, float min, float max)
	{
		return Math.max(min, Math.min(x, max));
	}
	
	public static
	double clampd (double x, double min, double max)
	{
		return Math.max(min, Math.min(x, max));
	}
	
	public static
	double clamp_symd (double x, double radius)
	{
		return clampd(x, -radius, +radius);
	}
	
}
