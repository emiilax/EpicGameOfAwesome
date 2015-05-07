package controller;

import java.util.HashMap;

import view.GameState;
import view.Level;
import view.LevelFinished;
import view.MenuState;
import lombok.Data;
import model.Content;
import model.MyInput;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxNativesLoader;

import event.EventSupport;
import event.TheChangeListener;
import event.TheEvent;


@Data
public class EGA implements ApplicationListener, TheChangeListener{
	
	public static final String TITLE= "The game";
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGTH = 720;
	public static final int SCALE = 1;
	
	public static final float STEP = 1/ 60f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	private GameState theLevel;
	
	private HashMap<Integer, Texture> finishedBgr;
	
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
		
		EventSupport.getInstance().addListner(this);
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		gsm = new GameStateManager(this);
		
		initHashMap();
		
		theLevel = new MenuState(gsm);
		gsm.pushState(theLevel);
	}
	
	private void initHashMap(){
		finishedBgr = new HashMap<Integer, Texture>();
		finishedBgr.put(1,  new Texture("res/menu/lol.jpg"));
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
	
	public void setLevelFinished(int i){
		LevelFinished state = new LevelFinished(gsm, finishedBgr.get(1));
		setLevel(state);
	}
	
	public void setLevel(GameState state){
		theLevel = state;
		gsm.setState(theLevel);
	}
	
	/**
	 * Handles the input from the user
	 */
	public void handleInput() {
	
		if(MyInput.isPressed(MyInput.BUTTON_JUMP)){
			
			theLevel.handleInput(MyInput.BUTTON_JUMP);
		
		}
		if(MyInput.isPressed(MyInput.BUTTON_DOWN)){

			theLevel.handleInput(MyInput.BUTTON_DOWN);

		}

		if(MyInput.isDown(MyInput.BUTTON_FORWARD)){

			theLevel.handleInput(MyInput.BUTTON_FORWARD);

		}else if(MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			theLevel.handleInput(MyInput.BUTTON_BACKWARD);
			
		}else if(MyInput.isDown(MyInput.BUTTON_ENTER)){

			theLevel.handleInput(MyInput.BUTTON_ENTER);

		}else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			theLevel.handleInput(-1);

		}
	}
	
	public void dispose() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}
	public void pause() {}

	public void eventRecieved(TheEvent evt) {
		if(evt.getNameOfEvent().equals("spikehit")){
			setLevel(new Level(gsm));
		}
		
	}
}
