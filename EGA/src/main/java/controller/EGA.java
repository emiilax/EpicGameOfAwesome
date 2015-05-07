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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
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
	
	public static Content res;
	
	// levels
	private TiledMap level1;
	private TiledMap level2;
	private TiledMap level3;
	private Array<TiledMap> levels;
	//end levels

	public void create() {
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		res = new Content();
		//load pictures
		res.loadTexture("res/tiles/bunny.png", "bunny");
		res.loadTexture("res/stars/star.png", "star");
		res.loadTexture("res/tiles/hud.png", "hud");
		res.loadTexture("res/characters/redball_small.png", "smallplayer");
		res.loadTexture("res/characters/redball_big.png", "bigPlayer");
		res.loadTexture("res/stars/bigStar.png", "bigStar");
		res.loadTexture("res/door/door2.jpg", "bigdoor");
		res.loadTexture("res/tiles/spikes_16x21.png", "spike");
		
		//load levels
		level1 = new TmxMapLoader().load("res/maps/testmap.tmx");
		level2 = new TmxMapLoader().load("res/maps/map2.tmx");
		level3 = new TmxMapLoader().load("res/maps/testmap.tmx");
		//add levels to the array levels
		levels = new Array<TiledMap>();
		levels.add(level1);
		levels.add(level2);
		levels.add(level3);
		
		
		
		EventSupport.getInstance().addListner(this);
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		gsm = new GameStateManager(this);
		
		theLevel = new MenuState(gsm);
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
	
	public void setLevel(GameState state){ //borde denna inte heta ngt annat?
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
			setLevel(new Level(gsm, gsm.getCurrentLevel()));
		}		
	}
	public TiledMap getLevel(int i){
		if(i==1){
			return level1;
		}
		if(i==2){
			return level2;
		}
		if(i==3){
			return level3;
		}
		return null;
//		return levels.get(i);
	}
}
