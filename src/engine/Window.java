package engine;


import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
 * 
 * @author www.gametutorial.net
 */

public class Window extends JFrame
{
	public static String gameTitle = "Crazy Lines";
	private boolean isFullscreen = !false;
	private Point resolution = new Point(1920, 1080);

	private Window()
	{
		// Sets the title for this frame.
		this.setTitle(gameTitle);

		// Sets size of the frame.
		if (isFullscreen) // Full screen mode
		{
			// Disables decorations for this frame.
			this.setUndecorated(true);
			// Puts the frame to full screen.
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else
		// Window mode
		{
			// Size of the frame.
			this.setSize(resolution.x, resolution.y);
			// Puts frame to center of the screen.
			this.setLocationRelativeTo(null);
			// So that frame cannot be resized by the user.
			this.setResizable(false);
		}

		// Exit the application when user close frame.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creates the instance of the Framework.java that extends the Canvas.java and puts it on the frame.
		this.setContentPane(new Framework());

		this.setVisible(true);
		
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new Window();
			}
		});
	}
}
