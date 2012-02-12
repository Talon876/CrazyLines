package crazylines;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import engine.Framework;

public class Circle
{
	Point position = new Point();
	int radius = 1;
	Vector2 velocity = new Vector2();
	int speed = 2;
	ValueInterpolator radiusChanger = new ValueInterpolator(100, 250);

	public Circle()
	{
		position = new Point(0, 0);
		radiusChanger = new ValueInterpolator(15, 15);
		velocity = new Vector2(0, 0);
	}

	public Circle(Point position)
	{
		this.position = position;
		radiusChanger = new ValueInterpolator(15, 15);
		velocity = new Vector2(0, 0);
	}

	public Circle(Point position, Vector2 velocity)
	{
		this.position = position;
		radiusChanger = new ValueInterpolator(15, 15);
		this.velocity = velocity;
	}

	public void update()
	{
		if (velocity.length() != 0)
			velocity.normalize();

		position.x += velocity.x * speed;
		position.y += velocity.y * speed;

		if (position.x - radius < 0)
		{
			velocity.x *= -1;
			position.x = 0 + radius;
		}
		if (position.x + radius > Framework.frameWidth)
		{
			velocity.x *= -1;
			position.x = Framework.frameWidth - radius;
		}

		if (position.y - radius < 0)
		{
			velocity.y *= -1;
			position.y = 0 + radius;
		}
		if (position.y + radius > Framework.frameHeight)
		{
			velocity.y *= -1;
			position.y = Framework.frameHeight - radius;
		}

		radiusChanger.update();
		radius = radiusChanger.getValue();
	}

	public void draw(Graphics2D g2d)
	{
		g2d.drawOval(position.x - radius, position.y - radius, radius * 2, radius * 2);
	}

	public Vector2 getVelocity()
	{
		return velocity;
	}

	public void setVelocity(Vector2 velocity)
	{
		this.velocity = velocity.normalize();
	}

	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public Point getPosition()
	{
		return position;
	}

	public int getRadius()
	{
		return radius;
	}

}
