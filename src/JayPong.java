import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.awt.PopupMenu;
import java.io.*;
import javax.sound.sampled.*;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;

import com.apple.eawt.Application;

public class JayPong
{
	private EventHandler events;
	private String[] locales;
	private final int width = 800;
	private final int height = 600;
	private ArrayList<Unit> units;
	private Player pOne, pTwo;
	private Ball ball;
	private Unit bottomBar, topBar;
	
	Clip playerHitSound;
	Clip ballOutSound;
	Clip wallHitSound;
	
	public static void main(String[] args)
	{
		JayPong game = new JayPong();
		game.startEventHandler();
		game.renderLoop();
	}
	
	public JayPong()
	{	
		PopupMenu popupMenu = new PopupMenu();
		Application application = com.apple.eawt.Application.getApplication();
		application.setDockMenu(popupMenu);
		if (this.loadLocales() && this.initAudio() && this.initOpenGL(width, height, true))
		{
			this.initUnits();
			javax.swing.JOptionPane.showMessageDialog(null, locales[1], locales[0], 1);
		}
		else
		{
			System.out.println("Exiting!");
			System.exit(-1);
		}
	}
	
	private boolean loadLocales()
	{
		try
		{
			File localesFile = new File("locales" + File.separator + System.getProperty("user.language") + ".xml");
			File alternativeLocalesFile = new File("locales" + File.separator + "en.xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = null;
			
			if (localesFile.exists())
			{
				doc = dBuilder.parse(localesFile);
			}
			else if (alternativeLocalesFile.exists())
			{
				doc = dBuilder.parse(alternativeLocalesFile);
			}
			else
			{
				javax.swing.JOptionPane.showMessageDialog(null, "Seems like the locales are missing.\n" +
																"Please make sure that the 'locales' folder isn't empty.", "No locales", 0);
				System.exit(1);
			}
			
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("locales");
			
			for (int i = 0; i < nList.getLength(); i++)
			{
				Node nNode = nList.item(i);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
					String[] theLocales =
					{
						getTagValue("welcomeTitle", eElement),
						getTagValue("welcomeMessage", eElement),
						getTagValue("firstPlayerWonTitle", eElement),
						getTagValue("firstPlayerWonMessage", eElement),
						getTagValue("secondPlayerWonTitle", eElement),
						getTagValue("secondPlayerWonMessage", eElement),
						getTagValue("pOne", eElement),
						getTagValue("pTwo", eElement),
						getTagValue("scores", eElement),
					};
					locales = theLocales;
				}
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage() + "\n" + ex.getStackTrace(), "IOException", 0);
			return false;
		}
		catch (ParserConfigurationException ex)
		{
			ex.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage() + "\n" + ex.getStackTrace(), "ParserConfigurationException", 0);
			return false;
		}
		catch (SAXException ex)
		{
			ex.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage() + "\n" + ex.getStackTrace(), "SAXException", 0);
			return false;
		}
		
		return true;
	}
	
	private boolean initOpenGL(int width, int height, boolean fullscreen)
	{
		try
		{
			if (fullscreen)
			{
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				for (DisplayMode mode : modes)
				{
					if (mode.getWidth() > Display.getDisplayMode().getWidth())
					{
						Display.setDisplayMode(mode);
					}
				}
				Display.setFullscreen(true);
			}
			else
			{
				Display.setDisplayMode(new DisplayMode(width, height));
			}
			
			Display.setTitle("JayPong");
			Display.create();
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, 800, 600, 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}
		catch (LWJGLException ex)
		{
			ex.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage() + "\n" + ex.getStackTrace(), "LWJGLException", 0);
			return false;
		}
		
		return true;
	}
	
	private boolean initAudio()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("audio" + File.separator + "playerhit.wav"));
			AudioFormat audioFormat = audioInputStream.getFormat();
			int size = (int) (audioFormat.getFrameSize() * audioInputStream.getFrameLength());
			byte[] audio = new byte[size];
			DataLine.Info info = new DataLine.Info(Clip.class, audioFormat, size);
			audioInputStream.read(audio, 0, size);
			playerHitSound = (Clip) AudioSystem.getLine(info);
			playerHitSound.open(audioFormat, audio, 0, size);
			
			audioInputStream = AudioSystem.getAudioInputStream(new File("audio" + File.separator + "wallhit.wav"));
			audioFormat = audioInputStream.getFormat();
			size = (int) (audioFormat.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, audioFormat, size);
			audioInputStream.read(audio, 0, size);
			wallHitSound = (Clip) AudioSystem.getLine(info);
			wallHitSound.open(audioFormat, audio, 0, size);
			
			audioInputStream = AudioSystem.getAudioInputStream(new File("audio" + File.separator + "ballout.wav"));
			audioFormat = audioInputStream.getFormat();
			size = (int) (audioFormat.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, audioFormat, size);
			audioInputStream.read(audio, 0, size);
			ballOutSound = (Clip) AudioSystem.getLine(info);
			ballOutSound.open(audioFormat, audio, 0, size);
		}
		catch (UnsupportedAudioFileException ex)
		{
			ex.printStackTrace();
			return false;
		}
		catch (LineUnavailableException ex)
		{
			ex.printStackTrace();
			return false;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void initUnits()
	{
		units = new ArrayList<Unit>();
		
		pOne = new Player(locales[6], 10, height / 2 - 35, 10, 60);
		pTwo = new Player(locales[7], width - 20, height / 2 - 35, 10, 60);
		ball = new Ball(width / 2 - 10, height / 2 - 10, 10, 10, 1, 0);
		topBar = new Unit(0, 0, width, 10);
		bottomBar = new Unit(0, height - 10, width, 10);
		
		units.add(pOne);
		units.add(pTwo);
		units.add(ball);
		units.add(topBar);
		units.add(bottomBar);
		
		Unit split1 = new Unit (width / 2 - 15, 0, 15, 50);
		Unit split2 = new Unit (width / 2 - 15, 80, 15, 50);
		Unit split3 = new Unit (width / 2 - 15, 160, 15, 50);
		Unit split4 = new Unit (width / 2 - 15, 240, 15, 50);
		Unit split5 = new Unit (width / 2 - 15, 320, 15, 50);
		Unit split6 = new Unit (width / 2 - 15, 400, 15, 50);
		Unit split7 = new Unit (width / 2 - 15, 480, 15, 50);
		Unit split8 = new Unit (width / 2 - 15, 560, 15, 50);
		
		units.add(split1);
		units.add(split2);
		units.add(split3);
		units.add(split4);
		units.add(split5);
		units.add(split6);
		units.add(split7);
		units.add(split8);
	}
	
	private String getTagValue(String sTag, Element eElement)
	{
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
	
	public void startEventHandler()
	{
		events = new EventHandler(width, height, locales, pOne, pTwo, ball, bottomBar, topBar, playerHitSound, ballOutSound, wallHitSound);
		events.start();
	}
	
	public void renderLoop()
	{
		while (!Display.isCloseRequested())
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			GL11.glColor3f(0.9f, 0.9f, 0.9f);
			
			for (Unit u : units)
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
		
		events.interrupt();
		playerHitSound.close();
		wallHitSound.close();
		ballOutSound.close();
		Display.destroy();
		System.exit(0);
	}
}
