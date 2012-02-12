package crazylines;

import java.awt.Graphics2D;

public class ValueInterpolator
{
	private int minValue = 0;
	private int maxValue = 100;
	private int stepAmount = 1;

	private int value = 0;

	private boolean increasing = true;
	
	public ValueInterpolator(int min, int max)
	{
		this.minValue = min;
		this.maxValue = max;
		value = (int) (Math.random()*(max-min)+min);
	}
	
	public void update()
	{
		if(increasing)
		{
			value = value + stepAmount;
			if(value >= maxValue)
			{
				value = maxValue;
				increasing = false;
			}
		}
		else //decreasing
		{
			value = value - stepAmount;
			if(value <= minValue)
			{
				value = minValue;
				increasing = true;
			}
		}
	}
	
	/**
	 * Used for debugging
	 * @param g2d
	 */
	public void draw(Graphics2D g2d)
	{
		g2d.drawString(""+value, 50, 50);
	}
	
	public int getValue()
	{
		return value;
	}
}
