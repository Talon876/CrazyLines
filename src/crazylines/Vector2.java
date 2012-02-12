package crazylines;

public class Vector2
{
	public static Vector2 ZERO = new Vector2(0, 0);
	public double x = 0.0;
	public double y = 0.0;

	public Vector2()
	{
	}

	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}

	// BASIC ASSIGNMENT METHODS
	// -------------------------------
	//
	void setTo(Vector2 vec)
	{
		x = vec.x;
		y = vec.y;
	}

	void setTo(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public String toString()
	{
		return new String("[" + x + "," + y + "]");
	}

	// MATHEMATICAL OPERATIONS
	// -------------------------------
	// The core functionality of
	// geometrical vectors
	//

	// 1. Vector length and normalization
	// -----------------------------------------
	//
	public double length()
	{
		return Math.sqrt((x * x) + (y * y));
	}

	public double lengthSquared()
	{
		return (x * x) + (y * y);
	}

	public Vector2 normalize()
	{
		double len = length();

		if (len != 0.0)
		{
			x /= len;
			y /= len;
		} else
		{
			x = 0.0;
			y = 0.0;
		}

		return new Vector2(this);
	}

	// 2. Addition methods
	// -------------------
	//
	public Vector2 add(Vector2 vec)
	{
		this.x += vec.x;
		this.y += vec.y;

		return new Vector2(this);
	}

	public Vector2 add(double x, double y)
	{
		this.x += x;
		this.y += y;

		return new Vector2(this);
	}

	// 3. Subtraction methods
	// -----------------------
	//
	public Vector2 subtract(Vector2 vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;

		return new Vector2(this);
	}

	public Vector2 subtract(double x, double y)
	{
		this.x -= x;
		this.y -= y;

		return new Vector2(this);
	}

	// 4. Multiplication methods
	// -------------------------
	//
	public Vector2 multiply(Vector2 vec)
	{
		this.x *= vec.x;
		this.y *= vec.y;

		return new Vector2(this);
	}

	public Vector2 multiply(double x, double y)
	{
		this.x += x;
		this.y += y;

		return new Vector2(this);
	}

	public Vector2 multiply(double scalar)
	{
		x *= scalar;
		y *= scalar;

		return new Vector2(this);
	}

	/**
	 * Returns a Vector2 representation of the angle.
	 * 
	 * @param angle
	 *            The angle to be converted (in radians)
	 * @return
	 */
	public static Vector2 convertAngle(double angle)
	{
		return new Vector2(Math.sin(angle), Math.cos(angle));
	}
}
