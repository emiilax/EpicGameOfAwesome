package states;

import handlers.GameStateManager;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxNativesLoader;

import lombok.*;
import main.Game;

@Data
public abstract class GameState {
	
	protected GameStateManager gsm;
	protected Game game;
	
	protected SpriteBatch sb;
	protected OrthographicCamera cam; 
	protected OrthographicCamera hudCam; 
	
	protected GameState(GameStateManager gsm){

		this.gsm = gsm;
		game = gsm.getGame();
		sb = game.getSb();
		
		cam = game.getCam();
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGTH);
		
		
		//hudCam = game.getHudCam();
		//hudCam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGTH);
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();
	
	
}
