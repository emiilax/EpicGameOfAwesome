package controller;

import io.Content;

import java.util.EmptyStackException;
import java.util.Stack;

import com.badlogic.gdx.maps.tiled.TiledMap;

import controller.superstate.GameState;
import lombok.Data;

/**
 * Class with a stack that handles what gamestate 
 * that should be updated
 * 
 * @author Emil Axelsson
 *
 */
@Data
public class GameStateManager {
	
	/** The gamecontroller*/
	private EGA game;
	
	/** The stack with gamestates */
	private Stack<GameState> gameStates;
	
	//public static final int MENU = 123123;
	//public static final int PLAY = 912837;
	
	/** the number of the current level*/
	private int currentLevel;
	
	/** 
	 * Initates the stack and set the camecontroller
	 * @param game
	 */
	public GameStateManager(EGA game){
		
		this.game = game;
		gameStates = new Stack<GameState>();
		currentLevel = 1;
	}
	
	/*
	@SuppressWarnings("unused")
	private GameState getState(int state){
	
		if(state == MENU) return new MainMenu(this);
		if(state == PLAY) {
			return new Level(this, Content.getInstance().getTiledMap(currentLevel));
		}
		return null;
	}*/
	
	/** 
	 * clear the stack and push a new gamestate
	 * to the top of the stack
	 * 
	 * @param state, the GameState that you want to set 
	 */
	public void setState(GameState state){
		gameStates.clear();
		game.setTheLevel(state);
		pushState(state);
		
	}
	
	/**
	 * Checks what type the current state is and returns it
	 * @return The current state
	 */
	public GameState getCurrentState(){
		return gameStates.get(gameStates.size()-1);
	}
	
	/**
	 * Push a new GameState to the stack without 
	 * erasing the prev state
	 * @param state, the GameState you want to push
	 */
	public void pushState(GameState state){
		game.setTheLevel(state);
		gameStates.push(state);
	}
	
	/**
	 * Removes the top element of the stack
	 */
	public void popState(){
		@SuppressWarnings("unused")
		GameState g = gameStates.pop();
		try{
			game.setTheLevel(gameStates.peek());
		}catch(EmptyStackException e){}
		
	}
	
	/**
	 * Updates the gamestate at the top of the stack
	 * @param dt
	 */
	public void update(float dt){
		
		gameStates.peek().update(dt);
	}
	
	/**
	 * Render the gamestate at the top of the stack
	 */
	public void render(){
		gameStates.peek().render();
	}
	
	/**
	 * 
	 * @param i
	 */
	public void setCurrentLevel(int i){
		currentLevel = i;
	}
	
	public TiledMap getNextTiledMap(){
		currentLevel++;
		return Content.getInstance().getTiledMap(currentLevel);
	}
	public TiledMap getCurrentTiledMap(){
		return Content.getInstance().getTiledMap(currentLevel);
	}
	public TiledMap getLevel(int i){ // this is the exact same method as in EGA
		return Content.getInstance().getTiledMap(i);
	}
	
	public int getCurrentLevel(){
		return currentLevel;
	}
	
}
