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
	private GameState theState;
	private Stack<GameState> gameStates;
	
	public static final int MENU = 123123;
	public static final int PLAY = 912837;
	
	public GameStateManager(EGA game){
		
		this.game = game;
		//this.theState = theState;
		gameStates = new Stack<GameState>();
		//pushState(this.theState);
		//pushState(PLAY);
	}
	
	private GameState getState(int state){

	
		if(state == MENU) return new MenuState(this);
		if(state == PLAY) return new Level(this);
		return null;
	}
	
	public void setState(GameState theState){
		popState();
		pushState(theState);
	}
	
	public void pushState(GameState state){
		gameStates.push(state);
		//gameStates.push(getState(state));
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
