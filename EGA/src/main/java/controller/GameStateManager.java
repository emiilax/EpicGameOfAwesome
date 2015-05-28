package controller;

import io.Content;

import java.util.EmptyStackException;
import java.util.Stack;

import com.badlogic.gdx.maps.tiled.TiledMap;

import controller.menus.MainMenu;
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
	
	/*
	@SuppressWarnings("unused")
	private GameState getState(int state){
	
		if(state == MENU) return new MainMenu(this);
		if(state == PLAY) {
			return new Level(this, Content.getInstance().getTiledMap(currentLevel));
		}
		return null;
	}*/
	
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
		@SuppressWarnings("unused")
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
