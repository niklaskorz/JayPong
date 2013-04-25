import javax.sound.sampled.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class EventHandler extends Thread
{
	private boolean pOneUp, pOneDown;
	private boolean pTwoUp, pTwoDown;
	private Player pOne, pTwo;
	private Ball _ball;
	private Unit _bottomBar, _topBar;
	private String[] _locales;
	private int _width, _height;
	
	Clip _playerHitSound;
	Clip _ballOutSound;
	Clip _wallHitSound;
	
	public EventHandler(int width, int height, String[] locales, Player playerOne, Player playerTwo, Ball ball, Unit bottomBar, Unit topBar, Clip playerHitSound, Clip ballOutSound, Clip wallHitSound)
	{
		_width = width;
		_height = height;
		pOne = playerOne;
		pTwo = playerTwo;
		_ball = ball;
		_bottomBar = bottomBar;
		_topBar = topBar;
		_locales = locales;
		_playerHitSound = playerHitSound;
		_ballOutSound = ballOutSound;
		_wallHitSound = wallHitSound;
	}
	
	public void run()
	{
		resetBall(_ball);
		
		while (!this.isInterrupted())
		{	
			try
			{
				if (Display.isActive())
				{
					while (Keyboard.next())
					{
						if (Keyboard.getEventKeyState() == true)
						{
							this.onKeyDown(Keyboard.getEventKey());
						}
						else
						{
							this.onKeyUp(Keyboard.getEventKey());
						}
					}
					
					eventHandler();
					Display.sync(60);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void eventHandler()
	{
		if (!checkCollision(pOne, 0, _width, 0, 10))
		{
			if (pOneUp)
			{
				pOne.addToPosition(0, -4);
			}
		}
		
		if (!checkCollision(pOne, 0, _width, _height - 10, _height))
		{
			if (pOneDown)
			{
				pOne.addToPosition(0, 4);
			}
		}
		
		if (!checkCollision(pTwo, 0, _width, 0, 10))
		{
			if (pTwoUp)
			{
				pTwo.addToPosition(0, -4);
			}
		}
		
		if (!checkCollision(pTwo, 0, _width, _height - 10, _height))
		{
			if (pTwoDown)
			{
				pTwo.addToPosition(0, 4);
			}
		}
		
		if (checkCollision(pOne, _ball) && _ball.getSpeedX() < 0)
		{
			_playerHitSound.stop();
			_playerHitSound.start();
			_ball.addToSpeed(-1, 0);
			_ball.multiplySpeed(-1, 1);
		}
		if (checkCollision(pTwo, _ball) && _ball.getSpeedX() > 0)
		{
			_playerHitSound.stop();
			_playerHitSound.start();
			_ball.addToSpeed(1, 0);
			_ball.multiplySpeed(-1, 1);
		}
			
		if (!checkCollision(_ball, 0, _width, 0, _height) && _ball.getPosX() < pOne.getPosX())
		{
			pTwo.addToScore(1);
			Display.setTitle("JayPong - " + _locales[8] + ": " + pOne.getName() + ": " + pOne.getScore() + " - " + pTwo.getName() + ": " + pTwo.getScore());
			_ballOutSound.stop();
			_ballOutSound.start();
			resetBall(_ball);
		}
		else if (!checkCollision(_ball, 0, _width, 0, _height) && _ball.getPosX() > pTwo.getPosX())
		{
			pOne.addToScore(1);
			Display.setTitle("JayPong - " + _locales[8] + ": " + pOne.getName() + ": " + pOne.getScore() + " - " + pTwo.getName() + ": " + pTwo.getScore());
			_ballOutSound.stop();
			_ballOutSound.start();
			resetBall(_ball);
		}
		
		if (pOne.getScore() == 8)
		{
			pOne.resetScore();
			pTwo.resetScore();
			javax.swing.JOptionPane.showMessageDialog(null, _locales[3], _locales[2], 1);
			Display.setTitle("JayPong - " + _locales[8] + ": " + pOne.getName() + ": " + pOne.getScore() + " - " + pTwo.getName() + ": " + pTwo.getScore());
		}
		else if (pTwo.getScore() == 8)
		{
			pOne.resetScore();
			pTwo.resetScore();
			javax.swing.JOptionPane.showMessageDialog(null, _locales[5], _locales[4], 1);
			Display.setTitle("JayPong - " + _locales[8] + ": " + pOne.getName() + ": " + pOne.getScore() + " - " + pTwo.getName() + ": " + pTwo.getScore());
		}
		
		if (checkCollision(_ball, _topBar) || checkCollision(_ball, _bottomBar))
		{
			_ball.multiplySpeed(1, -1);
			_wallHitSound.stop();
			_wallHitSound.start();
		}
		
		_ball.move();
	}
	
	private void resetBall(Ball _ball)
	{
		_ball.setPosition(_width / 2 - 10, _height / 2 - 10);
		
		double random = Math.random() * 100;
		
		if (random < 10)
		{
			_ball.setSpeed(1, 1);
		}
		else if (random >= 10 && random < 20)
		{
			_ball.setSpeed(1, -1);
		}
		else if (random >= 20 && random < 30)
		{
			_ball.setSpeed(-1, 1);
		}
		else if (random >= 30 && random < 40)
		{
			_ball.setSpeed(-1, -1);
		}
		else if (random >= 40 && random < 50)
		{
			_ball.setSpeed(1, 2);
		}
		else if (random >= 50 && random < 60)
		{
			_ball.setSpeed(1, -2);
		}
		else if (random >= 60 && random < 70)
		{
			_ball.setSpeed(-1, 2);
		}
		else if (random >= 70 && random < 80)
		{
			_ball.setSpeed(-1, -2);
		}
		else if (random >= 70 && random < 90)
		{
			_ball.setSpeed(-2, 1);
		}
		else if (random >= 90 && random < 100)
		{
			_ball.setSpeed(2, -1);
		}
		else
		{
			_ball.setSpeed(-2, -1);
		}
	}
	
/*	private boolean checkCollision(int left1, int right1, int top1, int bottom1, int left2, int right2, int top2, int bottom2)
	{
		if (bottom1 < top2)
			return false;
		if (top1 > bottom2)
			return false;
		if (right1 < left2)
			return false;
		if (left1 > right2)
			return false;
		
		return true;
	} */
	
	private boolean checkCollision(Unit a, int left2, int right2, int top2, int bottom2)
	{
		if (a == null)
		{
			return false;
		}
		
		int left1 = a.getPosX();
		int right1 = a.getPosX() + a.getWidth();
		int top1 = a.getPosY();
		int bottom1 = a.getPosY() + a.getHeight();
		
		if (bottom1 < top2)
			return false;
		if (top1 > bottom2)
			return false;
		if (right1 < left2)
			return false;
		if (left1 > right2)
			return false;
		
		return true;
	}
	
	private boolean checkCollision(Unit a, Unit b)
	{
		if (a == null || b == null)
		{
			return false;
		}
		
		int left1 = a.getPosX();
		int right1 = a.getPosX() + a.getWidth();
		int top1 = a.getPosY();
		int bottom1 = a.getPosY() + a.getHeight();
		
		int left2 = b.getPosX();
		int right2 = b.getPosX() + b.getWidth();
		int top2 = b.getPosY();
		int bottom2 = b.getPosY() + b.getHeight();
		
		if (bottom1 < top2)
			return false;
		if (top1 > bottom2)
			return false;
		if (right1 < left2)
			return false;
		if (left1 > right2)
			return false;
		
		return true;
	}
	
	private void onKeyDown(int keyCode)
	{
		switch (keyCode)
		{
			case Keyboard.KEY_ESCAPE:
					Display.destroy();
					System.exit(0);
					break;
			case Keyboard.KEY_W:
				pOneUp = true;
				break;
			case Keyboard.KEY_S:
				pOneDown = true;
				break;
			case Keyboard.KEY_UP:
				pTwoUp = true;
				break;
			case Keyboard.KEY_DOWN:
				pTwoDown = true;
				break;
			default:
				break;
		}
	}
	
	private void onKeyUp(int keyCode)
	{
		switch (keyCode)
		{
			case Keyboard.KEY_W:
				pOneUp = false;
				break;
			case Keyboard.KEY_S:
				pOneDown = false;
				break;
			case Keyboard.KEY_UP:
				pTwoUp = false;
				break;
			case Keyboard.KEY_DOWN:
				pTwoDown = false;
				break;
			default:
				break;
		}
	}
}
