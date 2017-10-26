package com.dylanscode.main;

import com.dylanscode.engine.GameEngine;
import com.dylanscode.engine.IGameLogic;

class Main {

	public static void main(String[] args) {
		try {
			IGameLogic logic = new BasicGame();
			GameEngine engine = new GameEngine("First Game",600,600,true,logic);
			engine.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}