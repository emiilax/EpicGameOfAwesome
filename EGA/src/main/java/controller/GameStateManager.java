package controller;

import java.util.EmptyStackException;
import java.util.Stack;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.GdxNativesLoader;

import controller.menus.MenuState;
import view.menus.PauseMenu;
import lombok.Data;


@Data
public class GameStateManager {
	
	private EGA game;
	private GameState theState;
	private Stack<GameState> gameStates;
	
	public static final int MENU = 123123;
	public static final int PLAY = 912837;
	
	private int currentLevel;
	
	public GameStateManager(EGA game){
		
		this.game = game;
		gameStates = new Stack<GameState>();
		currentLevel = 1;
	}
	
	private GameState getState(int state){
	
		if(state == MENU) return new MenuState(this);
		if(state == PLAY) {
			return new Level(this, game.getTiledMap(currentLevel));
		}
		return null;
	}
	
	public void setState(GameState state){
		gameStates.clear();
		game.setTheLevel(state);
		pushState(state);
		
	}
	
	public void pushState(GameState state){
		game.setTheLevel(state);
		gameStates.push(state);
	}
	
	public void popState(){
		GameState g = gameStates.pop();
		try{
			game.setTheLevel(gameStates.peek());
		}catch(EmptyStackException e){}
		
	}
	
	public void update(float dt){
		
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	public void setCurrentLevel(int i){
		currentLevel = i;
	}
	
	public TiledMap getNextTiledMap(){
		currentLevel++;
		return game.getTiledMap(currentLevel);
	}
	public TiledMap getCurrentTiledMap(){
		return game.getTiledMap(currentLevel);
	}
	public TiledMap getLevel(int i){ // this is the exact same method as in EGA
		return game.getTiledMap(i);
	}
	
	public int getCurrentLevel(){
		return currentLevel;
	}
	
}
