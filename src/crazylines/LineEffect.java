package crazylines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import engine.Framework;

public class LineEffect extends Effect
{
	ArrayList<Circle> circles = new ArrayList<Circle>();

	public LineEffect()
	{
		for (int i = 0; i < 150; i++)
		{
			Vector2 velocity = new Vector2();
			velocity.x = Math.random() * (100 - -100) + -100;
			velocity.y = Math.random() * (100 - -100) + -100;
			Circle circle = new Circle(new Point(Framework.frameWidth / 2 + (int) velocity.x, Framework.frameHeight / 2 + (int) velocity.y));
			circle.setVelocity(velocity);
			circle.setSpeed((int) (Math.random() * (12 - 4) + 4));
			circles.add(circle);
		}
		for (Circle circle : circles)
		{
			Vector2 velocity = new Vector2();
			velocity.x = Math.random() * (100 - -100) + -100;
			velocity.y = Math.random() * (100 - -100) + -100;
			circle.setVelocity(velocity);
		}

	}

	public void update()
	{
		if (Framework.mouseButtonState(MouseEvent.BUTTON1))
		{
			for (Circle circle : circles)
			{
				Vector2 velocity = new Vector2();
				velocity.x = Math.random() * (100 - -100) + -100;
				velocity.y = Math.random() * (100 - -100) + -100;
				circle.setVelocity(velocity);
			}
		}
		for (Circle circle : circles)
		{
			circle.update();
		}
	}
	
	public void shuffle()
	{
		for (Circle circle : circles)
		{
			Vector2 velocity = new Vector2();
			velocity.x = Math.random() * (100 - -100) + -100;
			velocity.y = Math.random() * (100 - -100) + -100;
			circle.setVelocity(velocity);
		}
	}

	public void draw(Graphics2D g2d)
	{
		g2d.setStroke(new BasicStroke(2));
		//g2d.setColor(Color.black);
		for (Circle circle : circles)
		{
			circle.draw(g2d);
			for (Circle circle2 : circles)
			{
				if (circle2.getPosition().distanceSq(circle.getPosition()) <= 220 * 220)
				{
					g2d.drawLine(circle.getPosition().x, circle.getPosition().y, circle2.getPosition().x, circle2.getPosition().y);
				}
			}
		}
	}
}
