package com.dylanscode.engine;


import com.dylanscode.engine.engine.game.MouseHandler;

public interface IGameLogic
{
	void init(Window window) throws Exception;
	
	void input(Window window, MouseHandler mouseHandler);
	
	void update(float interval, MouseHandler mouseHandler);
	
	void render(Window window);
	
	void clean();
	
}
