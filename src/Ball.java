
public class Ball extends Unit
{
	private int xSpeed;
	private int ySpeed;
	
	public Ball()
	{
		posX = 0;
		posY = 0;
		rectWidth = 0;
		rectHeight = 0;
		xSpeed = 0;
		ySpeed = 0;
	}
	
	public Ball(int positionX, int positionY, int width, int height, int speedX, int speedY)
	{
		posX = positionX;
		posY = positionY;
		rectWidth = width;
		rectHeight = height;
		xSpeed = speedX;
		ySpeed = speedY;
	}
	
	public void move()
	{
		posX += xSpeed;
		posY += ySpeed;
	}
	
	public void setSpeed(int speedX, int speedY)
	{
		xSpeed = speedX;
		ySpeed = speedY;
	}
	
	public void addToSpeed(int speedX, int speedY)
	{
		xSpeed += speedX;
		ySpeed += speedY;
	}
	
	public void multiplySpeed(int speedX, int speedY)
	{
		xSpeed *= speedX;
		ySpeed *= speedY;
	}
	
	public int getSpeedX()
	{
		return xSpeed;
	}
	
	public int getSpeedY()
	{
		return ySpeed;
	}
}
