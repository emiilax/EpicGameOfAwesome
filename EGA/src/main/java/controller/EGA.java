package controller;

import view.GameState;
import view.Level;
import view.MenuState;
import lombok.Data;
import model.Content;
import model.MyInput;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxNativesLoader;


@Data
public class EGA implements ApplicationListener{
	
	public static final String TITLE= "The game";
	public static final int V_WIDTH = 1080;
	public static final int V_HEIGTH = 900;
	public static final int SCALE = 1;
	
	public static final float STEP = 1/ 60f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	private GameState theLevel;
	
	public static Content res;

	public void create() {
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		res = new Content();
		res.loadTexture("res/tiles/bunny.png", "bunny");
		res.loadTexture("res/stars/star.png", "star");
		res.loadTexture("res/tiles/hud.png", "hud");
		res.loadTexture("res/characters/redball_small.png", "smallplayer");
		res.loadTexture("res/characters/redball_big.png", "bigPlayer");
		res.loadTexture("res/stars/bigStar.png", "bigStar");
		res.loadTexture("res/door/door2.jpg", "bigdoor");
		res.loadTexture("res/tiles/spikes_16x21.png", "spike");
		
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		gsm = new GameStateManager(this);
		
		theLevel = new Level(gsm);
		gsm.pushState(theLevel);
	}
	
	public void render() {
		accum+=Gdx.graphics.getDeltaTime();
		
		////handleInput();
		while (accum >= STEP){
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			handleInput();
			MyInput.update();
		}

	}
	
	/**
	 * Handles the input from the user
	 */
	public void handleInput() {
	
		if(MyInput.isPressed(MyInput.BUTTON_JUMP)){
			
			theLevel.handleInput(MyInput.BUTTON_JUMP);
		
		}

		if(MyInput.isDown(MyInput.BUTTON_FORWARD)){

			theLevel.handleInput(MyInput.BUTTON_FORWARD);

		}else if(MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			((Level)theLevel).playerMoveBackward();

		}else if(MyInput.isDown(MyInput.BUTTON_ENTER)){

			System.out.println("Do nothing");

		}else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			((Level)theLevel).playerStop();

		}
	}
	
	public void dispose() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}
	public void pause() {}
}
