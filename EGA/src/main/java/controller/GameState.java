package controller;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lombok.*;

@Data
public abstract class GameState {
	
	protected GameStateManager gsm;
	protected EGA game;
	
	protected SpriteBatch sb;
	protected OrthographicCamera cam; 
	protected OrthographicCamera hudCam;
	
	protected GameState(GameStateManager gsm){
		this.gsm = gsm;
		game = gsm.getGame();
		sb = game.getSb();
		
		cam = game.getCam();
		cam.setToOrtho(false, EGA.V_WIDTH, EGA.V_HEIGTH);
		
		hudCam = game.getHudCam();
		hudCam.setToOrtho(false, EGA.V_WIDTH, EGA.V_HEIGTH);
	}
	
	public abstract void handleInput(int i);
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();
	
	
}
