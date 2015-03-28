package main;

import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;
import lombok.Data;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

@Data
public class Game implements ApplicationListener{
	
	public static final String TITLE= "The game";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGTH = 240;
	public static final int SCALE = 2;
	
	public static final float STEP = 1/ 60f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	
	public void create() {
		
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		//hudCam = new OrthographicCamera();
		gsm = new GameStateManager(this);		
	}
	
	public void render() {
		accum+=Gdx.graphics.getDeltaTime();
		while (accum >= STEP){
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			MyInput.update();
		}
	}
	
	public void dispose() {}

	

	

	public void resize(int arg0, int arg1) {}
	public void resume() {}
	public void pause() {}
}
