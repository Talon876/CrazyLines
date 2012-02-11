package crazylines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import javax.swing.border.StrokeBorder;


import engine.Framework;

/**
 * Keeps track of a grid of points and does DFS through them. During the DFS it just picks a random neighbor and moves to it. 
 * While it's doing this DFS it builds a path of points that it should draw lines between. This path is the one that is drawn.
 * @author Talon
 *
 */
public class CrazyLine
{
	public static final int UNVISITED = 0;
	public static final int VISITED = 1;

	int LINE_THICKNESS = 3; //thickness of the lines, 3 default
	int ITERATIONS_PER_FRAME = 3; //increase this to increase how many segments it does in one update, 3 default
	static int SCALE = 40; //lower number = more grid points. 40 default
	double JITTERINESS = .001d; //number between 0 and 1. 0 is completely rigid, 1 is more jittery, .001 default. (can make more than 1 for extreme jitter)
	boolean JITTER = false; //whether or not it jitters as it draws; false default
	int JITTER_SPEED = 1; //how fast it jitters. lower number = faster, 1 = fastest. 1 = default
	boolean DRAW_LINES = true; //whether or not to draw lines; true default
	boolean DRAW_POINTS = false; //whether or not to draw points; false default
	
	public static int gridWidth = Framework.frameWidth / SCALE;
	public static int gridHeight = Framework.frameHeight / SCALE;
	public static int tileWidth = gridWidth / SCALE;
	public static int tileHeight = gridHeight / SCALE;
	public static Random random = new Random();
	private Point gridOffsetP = new Point(SCALE / 2, SCALE / 2);
	public int[][] gridData = new int[gridWidth][gridHeight];
	public Point[][] gridOffsets = new Point[gridWidth][gridHeight];
	private HashMap<Integer, Point> directionMap = new HashMap<Integer, Point>();
	Point current = new Point(Framework.frameWidth / SCALE / 2, Framework.frameHeight / SCALE / 2);
	private Stack<Point> theStack = new Stack<Point>();
	private Stack<Point> thePath = new Stack<Point>();
	public int[][] size = new int[gridWidth][gridHeight];
	public Color color = Color.white;
	
	public BaseGame activator;

	public CrazyLine()
	{
		System.out.println("SCALE: " + SCALE);
		for (int y = 0; y < gridData[0].length; y++)
		{
			for (int x = 0; x < gridData.length; x++)
			{
				gridData[x][y] = 0;
				gridOffsets[x][y] = new Point(random.nextInt((int) (JITTERINESS * SCALE) + 1), random.nextInt((int) (JITTERINESS * SCALE) + 1));
				size[x][y] = 0;
			}
		}

		directionMap.put(0, Directions.UP);
		directionMap.put(1, Directions.RIGHT);
		directionMap.put(2, Directions.LEFT);
		directionMap.put(3, Directions.DOWN);

		setValueAtPoint(current, VISITED);

		theStack.push(current);
		thePath.push(current);
	}

	public Point getRandomPoint()
	{
		return new Point(random.nextInt(gridWidth), random.nextInt(gridHeight));
	}

	public Point getRandomDirection()
	{
		return directionMap.get(random.nextInt(directionMap.size()));
	}

	//returns a list of directions, not the neighbors
	public ArrayList<Point> getUnvisitedNeighbors(Point point)
	{
		ArrayList<Point> uneighbors = new ArrayList<Point>();
		for (int i = 0; i < directionMap.size(); i++)
		{
			Point temp = new Point();
			temp.x = point.x + directionMap.get(i).x;
			temp.y = point.y + directionMap.get(i).y;
			if (getValueAtPoint(temp) == UNVISITED)
			{
				uneighbors.add(directionMap.get(i));
			}
		}
		return uneighbors;
	}

	public int getValueAtPoint(Point p)
	{
		if (p.x >= 0 && p.x < gridWidth && p.y >= 0 && p.y < gridHeight)
		{
			return gridData[p.x][p.y];
		} else
			return -1;
	}

	public void setValueAtPoint(Point p, int value)
	{
		if (p.x >= 0 && p.x < gridWidth && p.y >= 0 && p.y < gridHeight)
		{
			gridData[p.x][p.y] = value;
		}
	}

	boolean update = true;

	public void update()
	{
		c++;
		setSize();
		if (update)
		{
			for (int i = 0; i < ITERATIONS_PER_FRAME; i++)
			{
				RDFS();
			}

			if (JITTER)
				jitter();
		}
	}

	private void setSize()
	{
		for (int y = 0; y < gridData[0].length; y++)
		{
			for (int x = 0; x < gridData.length; x++)
			{
				if (gridData[x][y] == VISITED)
				{
					if (size[x][y] >= 0 && size[x][y] < SCALE)
					{
						size[x][y] = size[x][y] + 1;
					} else
					{
						size[x][y] = SCALE;
					}
				}
			}
		}

	}

	long c = 0;

	private void jitter()
	{
		if (c % JITTER_SPEED == 0)
		{
			for (int y = 0; y < gridData[0].length; y++)
			{
				for (int x = 0; x < gridData.length; x++)
				{
					if (getValueAtPoint(new Point(x, y)) == VISITED)
					{
						gridOffsets[x][y] = new Point(random.nextInt((int) (JITTERINESS * SCALE)), random.nextInt((int) (JITTERINESS * SCALE)));
					}
				}
			}
		}

	}

	//when it finishes drawing a line, it calls this method. This resets everything, gives it a new color, and randomizes the parameters
	public void reset()
	{
		for (int y = 0; y < gridData[0].length; y++)
		{
			for (int x = 0; x < gridData.length; x++)
			{
				gridData[x][y] = 0;
				gridOffsets[x][y] = new Point(random.nextInt((int) (JITTERINESS * SCALE) + 1), random.nextInt((int) (JITTERINESS * SCALE) + 1));
				size[x][y] = 0;
			}
		}
		current = getRandomPoint();
		theStack.clear();
		thePath.clear();
		
	    randomizeParameters();
	}

	//changes the parameters every time it finishes
	private void randomizeParameters()
	{
		this.color = getRandomColor(255, 255, 255);
		activator.updateGradient(this, this.color); //calls the updateGradient method in the BaseGame class. This is so the gradient effect can update its colors
		//LINE_THICKNESS = 1;
		//ITERATIONS_PER_FRAME = (int)randomRange(3,8);
		//SCALE = 30;
	}

	private void RDFS()
	{
		ArrayList<Point> uneighbors = getUnvisitedNeighbors(current);

		if (uneighbors.size() > 0) //if the current cell has any neighbors which have not been visited
		{
			addCurrentPointToThePathIfItsANeighborOfPreviousPointThatWasAddedToTheStack(current);
			Point chosen = uneighbors.get(random.nextInt(uneighbors.size()));
			Point nextPoint = new Point(current.x + chosen.x, current.y + chosen.y);
			theStack.push(nextPoint);
			setValueAtPoint(nextPoint, VISITED);
			current = nextPoint;
		} else //stuck
		{
			if (theStack.size() > 1)
			{
				current = theStack.pop();
				addCurrentPointToThePathIfItsANeighborOfPreviousPointThatWasAddedToTheStack(current);
				current = theStack.peek();
				
			} else
			{
				reset();
			}
		}
	}

	private void addCurrentPointToThePathIfItsANeighborOfPreviousPointThatWasAddedToTheStack(Point current)
	{
		if (thePath.size() > 0)
		{
			Point lastAdded = thePath.peek();
			for (int i = 0; i < directionMap.size(); i++)
			{
				Point lastAddedPlusDir = new Point();
				lastAddedPlusDir.x = lastAdded.x + directionMap.get(i).x;
				lastAddedPlusDir.y = lastAdded.y + directionMap.get(i).y;
				if (lastAddedPlusDir.equals(current) || lastAdded.equals(current))
				{
					thePath.push(current);
					return;
				}
			}
		}
		thePath.push(current);
	}

	public void draw(Graphics2D g2d)
	{
		g2d.setStroke(new BasicStroke(LINE_THICKNESS));
		g2d.setColor(color);

		if (DRAW_LINES)
		{
			ArrayList<Point> points = new ArrayList<Point>(thePath);
			for (int i = 1; i < points.size(); i++)
			{
				Point adjustedA = new Point();
				adjustedA.x = points.get(i).x * Framework.frameWidth / gridWidth + gridOffsetP.x + gridOffsets[points.get(i).x][points.get(i).y].x;
				adjustedA.y = points.get(i).y * Framework.frameHeight / gridHeight + gridOffsetP.y + gridOffsets[points.get(i).x][points.get(i).y].y;

				Point adjustedB = new Point();
				adjustedB.x = points.get(i - 1).x * Framework.frameWidth / gridWidth + gridOffsetP.x + gridOffsets[points.get(i - 1).x][points.get(i - 1).y].x;
				adjustedB.y = points.get(i - 1).y * Framework.frameHeight / gridHeight + gridOffsetP.y + gridOffsets[points.get(i - 1).x][points.get(i - 1).y].y;

				g2d.drawLine(adjustedA.x, adjustedA.y, adjustedB.x, adjustedB.y); //draws the line

				int rSize = size[points.get(i).x][points.get(i).y];
				g2d.fillRect(adjustedA.x - (rSize) / 2, adjustedA.y - (rSize) / 2, rSize + 1, rSize + 1); //draws the expanding boxes
				

			}
		}

		if (DRAW_POINTS)
		{
			for (int y = 0; y < gridData[0].length; y++)
			{
				for (int x = 0; x < gridData.length; x++)
				{
					if (gridData[x][y] != -1)
					{
						Point point = new Point();
						point.x = x * Framework.frameWidth / gridWidth + gridOffsetP.x + gridOffsets[x][y].x;
						point.y = y * Framework.frameHeight / gridHeight + gridOffsetP.y + gridOffsets[x][y].y;

						g2d.drawRect(point.x - 2, point.y - 2, 4, 4); //draws points
					}
				}
			}

		}
	}

	public static float randomRange(float min, float max)
	{
		return (float) random.nextDouble() * (max - min) + min;
	}
	
	public Color getRandomColor(int maxR, int maxG, int maxB)
	{
		return new Color(random.nextInt(maxR+1), random.nextInt(maxG+1), random.nextInt(maxB), 128);
	}
}
