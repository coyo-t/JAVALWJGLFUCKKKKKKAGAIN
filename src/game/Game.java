package game;

public interface Game
{
	void resize (long window, int newWide, int newTall);
	void init ();
	void step (long window);
	void draw (long window);
	
}
