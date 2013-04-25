
public class Unit
{
	protected int posX;
	protected int posY;
	protected int rectWidth;
	protected int rectHeight;
	
	public Unit()
	{
		posX = 0;
		posY = 0;
		rectWidth = 0;
		rectHeight = 0;
	}
	
	public Unit(int positionX, int positionY, int width, int height)
	{
		posX = positionX;
		posY = positionY;
		rectWidth = width;
		rectHeight = height;
	}
	
	public void addToPosition(int x, int y)
	{
		posX += x;
		posY += y;
	}
	
	public void multiplyPosition(int x, int y)
	{
		posX *= x;
		posY *= y;
	}
	
	public int getPosX()
	{
		return posX;
	}
	
	public int getPosY()
	{
		return posY;
	}
	
	public void setPosition(int x, int y)
	{
		posX = x;
		posY = y;
	}
	
	public void setSize(int width, int height)
	{
		rectWidth = width;
		rectHeight = height;
	}
	
	public int getWidth()
	{
		return rectWidth;
	}
	
	public int getHeight()
	{
		return rectHeight;
	}
}
