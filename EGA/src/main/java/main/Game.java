package main;

import handlers.Content;
import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;
import lombok.Data;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxNativesLoader;


@Data
public class Game implements ApplicationListener{
	
	public static final String TITLE= "The game";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGTH = 640;
	public static final int SCALE = 2;
	
	public static final float STEP = 1/ 60f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	
	public static Content res;

	public void create() {
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		res = new Content();
		res.loadTexture("res/tiles/bunny.png", "bunny");
		res.loadTexture("res/tiles/crystal.png", "crystal");
		res.loadTexture("res/tiles/hud.png", "hud");
		res.loadTexture("res/characters/redball.png", "player");
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
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
