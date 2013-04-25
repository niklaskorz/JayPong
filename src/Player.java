
public class Player extends Unit
{
	protected int score;
	protected String name;
	
	public Player()
	{
		name = "Anonymous";
		posX = 0;
		posY = 0;
		rectWidth = 0;
		rectHeight = 0;
		score = 0;
	}
	
	public Player(String playerName, int positionX, int positionY, int width, int height)
	{
		name = playerName;
		posX = positionX;
		posY = positionY;
		rectWidth = width;
		rectHeight = height;
		score = 0;
	}
	
	public void resetScore()
	{
		score = 0;
	}
	
	public void addToScore(int addScore)
	{
		score += addScore;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setName(String playerName)
	{
		name = playerName;
	}
	
	public String getName()
	{
		return name;
	}
}
