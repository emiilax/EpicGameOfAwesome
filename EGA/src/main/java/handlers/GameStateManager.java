package handlers;

import java.util.Stack;

import states.GameState;
import states.Play;
import lombok.Data;
import lombok.Getter;
import main.Game;


@Data
public class GameStateManager {
	
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 912837;
	
	public GameStateManager(Game game){
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(PLAY);
		
	}
	
	private GameState getState(int state){
		if(state == PLAY) return new Play(this);
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
