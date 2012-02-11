package engine;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.LineMetrics;

import crazylines.BaseGame;


/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas
{

	/**
	 * Width of the frame.
	 */
	public static int frameWidth;
	/**
	 * Height of the frame.
	 */
	public static int frameHeight;

	/**
	 * Time of one second in nanoseconds.
	 * 1 second = 1 000 000 000 nanoseconds
	 */
	public static final long secInNanosec = 1000000000L;

	/**
	 * Time of one millisecond in nanoseconds.
	 * 1 millisecond = 1 000 000 nanoseconds
	 */
	public static final long milisecInNanosec = 1000000L;

	/**
	 * FPS - Frames per second
	 * How many times per second the game should update?
	 */
	private final int GAME_FPS = 60;
	/**
	 * Pause between updates. It is in nanoseconds.
	 */
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

	/**
	 * Possible states of the game
	 */
	public static enum GameState
	{
		STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED
	}

	/**
	 * Current state of the game
	 */
	public static GameState gameState;

	/**
	 * Elapsed game time in nanoseconds.
	 */
	private long gameTime;
	// It is used for calculating elapsed time.
	private long lastTime;

	// The actual game
	private BaseGame game;

	public Framework()
	{
		super();

		gameState = GameState.VISUALIZING;
		System.out.println("State: " + gameState.toString());
		// We start game in new thread.
		Thread gameThread = new Thread()
		{
			@Override
			public void run()
			{
				gameLoop();
			}
		};
		gameThread.start();
	}

	/**
	 * Set variables and objects.
	 * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
	 */
	private void initialize()
	{
		System.out.println("framework init");
		System.out.println("State: " + gameState.toString());
		periodCount = 0;
		loadingMessage = "Loading";
	}

	/**
	 * Load files - images, sounds, ...
	 * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
	 */
	private void loadContent()
	{
		System.out.println("framework load");
		System.out.println("State: " + gameState.toString());
	}

	/**
	 * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
	 */
	private void gameLoop()
	{
		// This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
		long visualizingTime = 0;
		long lastVisualizingTime = System.nanoTime();

		// This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
		long beginTime;
		long timeTaken;
		long timeLeft;

		while (true)
		{
			beginTime = System.nanoTime();
			switch (gameState)
			{
			case PLAYING:
				gameTime += System.nanoTime() - lastTime;

				game.updateGame(gameTime, mousePosition());

				lastTime = System.nanoTime();
				break;
			case GAMEOVER:
				// ...
				break;
			case MAIN_MENU:
				// ...
				break;
			case OPTIONS:
				// ...
				break;
			case GAME_CONTENT_LOADING:
				updateLoadingScreen();
				break;
			case STARTING:
				// Sets variables and objects.
				initialize();
				// Load files - images, sounds, ...
				loadContent();

				// When all things that are called above finished, we change game status to main menu.
				// gameState = GameState.MAIN_MENU;
				newGame();
				break;
			case VISUALIZING:
				// On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and
				// at last 798px).
				// So we wait one second for the window/frame to be set to its correct size. Just in case we
				// also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
				// so that we although get approximately size.
				if (this.getWidth() > 1 && visualizingTime > secInNanosec * .01)
				{
					frameWidth = this.getWidth();
					frameHeight = this.getHeight();

					// When we get size of frame we change status.
					gameState = GameState.STARTING;
				} else
				{
					visualizingTime += System.nanoTime() - lastVisualizingTime;
					lastVisualizingTime = System.nanoTime();
				}
				break;
			}

			// Repaint the screen.
			repaint();

			// Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
			// If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
			if (timeLeft < 10)
				timeLeft = 10; // set a minimum
			try
			{
				// Provides the necessary delay and also yields control so that other thread can do work.
				Thread.sleep(timeLeft);
			} catch (InterruptedException ex)
			{
			}
		}
	}

	/**
	 * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
	 */
	@Override
	public void draw(Graphics2D g2d)
	{
		switch (gameState)
		{
		case PLAYING:
			game.draw(g2d, mousePosition());
			break;
		case GAMEOVER:
			// ...
			break;
		case MAIN_MENU:
			drawMainMenu(g2d);
			break;
		case OPTIONS:
			// ...
			break;
		case GAME_CONTENT_LOADING:
			drawLoadingScreen(g2d);
			break;
		}
	}

	private void drawMainMenu(Graphics2D g2d)
	{
		g2d.setColor(Color.white);

		g2d.setFont(new Font("monospaced", Font.BOLD, 24));
		String instructions = "Press Space or Enter to begin";
		FontMetrics metrics = g2d.getFontMetrics();
		Point stringBounds = new Point(metrics.stringWidth(instructions), metrics.getAscent() - metrics.getDescent());
		g2d.drawString(instructions, (frameWidth - stringBounds.x) / 2, frameHeight - stringBounds.y);

		g2d.setFont(new Font("monospaced", Font.BOLD, 48));
		metrics = g2d.getFontMetrics();
		stringBounds = new Point(metrics.stringWidth(Window.gameTitle), metrics.getAscent() - metrics.getDescent());
		g2d.drawString(Window.gameTitle, (frameWidth - stringBounds.x) / 2, (frameHeight - stringBounds.y) / 2);
	}

	int periodCount = 0;
	String loadingMessage;
	String finalLoadingMessage;
	long loadTime = 0;
	long lastLoadTime = 0;
	long elapsedTimeWhileLoading = 0;

	private void updateLoadingScreen()
	{
		loadTime += System.nanoTime() - lastLoadTime;
		elapsedTimeWhileLoading += loadTime / secInNanosec;
		periodCount = (int) (elapsedTimeWhileLoading / 2500000);
		String periods = "";
		periodCount %= 6;
		for (int i = 0; i < periodCount; i++)
		{
			periods += ".";
		}
		finalLoadingMessage = loadingMessage + periods;
		lastLoadTime = System.nanoTime();
	}

	private void drawLoadingScreen(Graphics2D g2d)
	{
		g2d.setColor(Color.white);
		g2d.setFont(new Font("monospaced", Font.BOLD, 36));
		FontMetrics metrics = g2d.getFontMetrics();

		// Point stringBounds = new Point(metrics.stringWidth(finalLoadingMessage), metrics.getAscent() - metrics.getDescent());
		if (finalLoadingMessage != null)
		{
			Point stringBounds = new Point(metrics.stringWidth(finalLoadingMessage), metrics.getAscent() - metrics.getDescent());
			g2d.drawString(finalLoadingMessage, (frameWidth - stringBounds.x) / 2, frameHeight - stringBounds.y);
		}
	}

	/**
	 * Starts new game.
	 */
	private void newGame()
	{
		// We set gameTime to zero and lastTime to current time for later calculations.
		gameTime = 0;
		lastTime = System.nanoTime();

		game = new BaseGame();
	}

	/**
	 * Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
	 */
	private void restartGame()
	{
		// We set gameTime to zero and lastTime to current time for later calculations.
		gameTime = 0;
		lastTime = System.nanoTime();

		game.restartGame();

		// We change game status so that the game can start.
		gameState = GameState.PLAYING;
	}

	/**
	 * Returns the position of the mouse pointer in game frame/window.
	 * If mouse position is null than this method return 0,0 coordinate.
	 * 
	 * @return Point of mouse coordinates.
	 */
	private Point mousePosition()
	{
		try
		{
			Point mp = this.getMousePosition();

			if (mp != null)
				return this.getMousePosition();
			else
				return new Point(0, 0);
		} catch (Exception e)
		{
			return new Point(0, 0);
		}
	}

	/**
	 * This method is called when keyboard key is released.
	 * 
	 * @param e
	 *            KeyEvent
	 */
	@Override
	public void keyReleasedFramework(KeyEvent e)
	{
		switch (gameState)
		{

		case MAIN_MENU:
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				System.exit(0);
			else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
				newGame();
		}
	}

	/**
	 * This method is called when mouse button is clicked.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{

	}
}
