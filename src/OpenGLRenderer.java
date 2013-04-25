import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;

public class OpenGLRenderer extends Thread
{
	private ArrayList<Unit> renderUnits;
	
	public OpenGLRenderer(int width, int height, ArrayList<Unit> units)
	{	
		try
		{
			/* DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (DisplayMode mode : modes)
			{
				if (mode.getWidth() > Display.getDisplayMode().getWidth())
				{
					Display.setDisplayMode(mode);
				}
			} */
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("JayPong");
			Display.setFullscreen(true);
			Display.create();
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, 800, 600, 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			
			renderUnits = units;
		}
		catch (LWJGLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void run()
	{
		while (!Display.isCloseRequested())
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			GL11.glColor3f(0.9f, 0.9f, 0.9f);
			
			for (Unit u : renderUnits)
			{
				GL11.glPushMatrix();
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2f(u.getPosX(), u.getPosY());
					GL11.glVertex2f(u.getPosX() + u.getWidth(), u.getPosY());
					GL11.glVertex2f(u.getPosX() + u.getWidth(), u.getPosY() + u.getHeight());
					GL11.glVertex2f(u.getPosX(), u.getPosY() + u.getHeight());
				GL11.glEnd();
				GL11.glPopMatrix();
			}
			
			Display.update();
		}
	}
}
