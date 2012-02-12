package crazylines;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


import engine.Framework;

public class BaseGame
{
	GradientEffect gradientEffect = new GradientEffect();
	CrazyLine line = new CrazyLine();
	CrazyLine line2 = new CrazyLine();
	ArrayList<CrazyLine> lines = new ArrayList<CrazyLine>(); //used to hold all the lines
	LineEffect lineEffect = new LineEffect();
	
	public BaseGame()
	{
		Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
		System.out.println("State: " + Framework.gameState.toString());
		Thread threadForInitGame = new Thread()
		{
			@Override
			public void run()
			{
				initialize();
				loadContent();

				Framework.gameState = Framework.GameState.PLAYING;
				System.out.println("State: " + Framework.gameState.toString());
			}
		};
		threadForInitGame.start();
		
	}

	private void initialize()
	{
		System.out.println("game init");
	}

	private void loadContent()
	{
		System.out.println("game load");
		
		Color transRed = new Color(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), 128);
		line.color = transRed;
		line.activator = this; //giving an instance of CrazyLine a way to call updateGradient in this class
		gradientEffect.setColor1(line.color);
		
		Color transBlue = new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 128);
		line2.color = transBlue;
		line2.activator = this;
		gradientEffect.setColor2(line2.color);
		
		lines.add(line);
		lines.add(line2);
		
	}

	public void restartGame()
	{
		System.out.println("restart game");
	}

	public void updateGame(long gameTime, Point mousePosition)
	{
		checkForInput(mousePosition);
		gradientEffect.update();
		for(CrazyLine line : lines) //update all the lines
		{
			line.update();
		}
		lineEffect.update();
		
	}

	private void checkForInput(Point mousePosition)
	{
		if(Framework.keyboardKeyState(KeyEvent.VK_ESCAPE)) //exit if escape is pressed
		{
			System.exit(0);
		}
		
		if(mousePosition.x > Framework.frameWidth-16 && mousePosition.y < 16) //exit if mouse is moved to upper right corner
		{
			System.exit(0);
		}
		
	}

	public void draw(Graphics2D g2d, Point mousePosition)
	{
		gradientEffect.draw(g2d); //draw the rotating gradient effect
		lineEffect.draw(g2d);
		for(CrazyLine line : lines) //draw all the lines
		{
			line.draw(g2d);
		}
		
	}
	
	/**
	 * This is to be called from within an instance of CrazyLine
	 * @param sender The instance of CrazyLine that called this method
	 * @param color The color the instance of CrazyLine just changed to
	 */
	public void updateGradient(CrazyLine sender, Color color)
	{
		if(sender.equals(line)) //when line 1 changes colors, set the color1 in the effect to be the same color
		{
			gradientEffect.setColor1(color);
			lineEffect.shuffle();
		}
		else if(sender.equals(line2)) //when line 2 changes colors, set the color2 in the effect to be the same color
		{
			gradientEffect.setColor2(color);
		}
	}
}
