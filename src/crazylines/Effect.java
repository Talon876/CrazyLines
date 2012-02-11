package crazylines;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import engine.Framework;

/**
 * A class for drawing effects. This one picks a center point and has an outer point rotating around it in a circle and it's drawing a gradient from the center point to the outer point. This creates
 * the rotating gradient in the background of the program.
 * 
 * @author Talon
 * 
 */
public class Effect
{
	Point center = new Point();
	Point circlePoint = new Point();
	long c = 0;
	int radius = 150;
	GradientPaint gradient;
	private Color color1 = Color.blue;
	private Color color2 = Color.yellow;
	private Color transparentBlack = new Color(0, 0, 0, 128);

	public Effect()
	{
		radius = Framework.frameWidth / 2;
		center = new Point(Framework.frameWidth / 2, Framework.frameHeight / 2);
		gradient = new GradientPaint(center, Color.black, circlePoint, Color.white);
	}

	public void update()
	{
		c++;
		if (c > 180)
		{
			c = 0;
		}

		float pctAroundCircle = 1 - ((float) c / 180);
		float fullCircle = (float) (Math.PI * 2);

		float angle = fullCircle * pctAroundCircle;
		int x = (int) (Math.sin(angle) * radius);
		int y = (int) (Math.cos(angle) * radius);
		circlePoint = new Point(x + center.x, y + center.y);
	}

	public void draw(Graphics2D g2d)
	{
		g2d.setColor(transparentBlack);

		g2d.fillRect(0, 0, Framework.frameWidth, Framework.frameHeight);
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, Framework.frameWidth, Framework.frameHeight);
		gradient = new GradientPaint(center, color1, circlePoint, color2);
	}

	/**
	 * Takes in a color and sets it with 64alpha to color1
	 * @param c The new color
	 */
	public void setColor1(Color c)
	{
		color1 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 64);
	}

	/**
	 * Takes in a color and sets it with 64alpha to color2
	 * @param c The new color
	 */
	public void setColor2(Color c)
	{
		color2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 64);
	}
}
