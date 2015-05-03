package controller;

import java.util.Stack;

import com.badlogic.gdx.utils.GdxNativesLoader;

import view.GameState;
import view.Level;
import view.Menu;
import lombok.Data;


@Data
public class GameStateManager {
	
	
	private EGA game;
	
	private Stack<GameState> gameStates;
	
	public static final int MENU = 123123;
	public static final int PLAY = 912837;
	
	public GameStateManager(EGA game){
		
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(PLAY);
	}
	
	private GameState getState(int state){
	
		if(state == MENU) return new Menu(this);
		if(state == PLAY) return new Level(this);
		return null;
	}
	
	public void setState(int state){
		popState();
		pushState(state);
	}
	
	public void pushState(int state){
		gameStates.push(getState(state));
	}
	
	public void popState(){
		GameState g = gameStates.pop(); 
	}
	
	public void update(float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
}
