package controller;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import model.GameData;
import model.MyInput;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import controller.menus.IMenu;
import controller.menus.LevelFinished;
import controller.menus.MainMenu;
import event.EventSupport;
import event.TheChangeListener;
import event.TheEvent;

/**
 * This class controls the inputs from  the user
 */
@Data
public class EGA implements ApplicationListener, TheChangeListener{

	/** The name that will be shown in the game frame */
	public static final String TITLE= "Epic Game of Awesome";

	/** The width of the game */
	public static final int V_WIDTH = 1280;
	/** The heigth of the game */
	public static final int V_HEIGTH = 720;

	/** How the game should be scaled (normally 1) */
	public static final int SCALE = 1;

	/** In which rate the game should be updated*/
	public static final float STEP = 1/ 60f;
	/** Used to keep a steady updating-rate*/
	private float accum;

	/** Keep the info on keys etc.*/
	private GameData gameData;

	/** The spritebatch that are used to draw on screen*/
	private SpriteBatch sb;

	/** The camera */
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	/** Manages the gamestates */
	private GameStateManager gsm;

	/** The current gamestate */
	private GameState theLevel;

	/** Map with maps */
	private Map<Integer, TiledMap> maps;

	/** Map with menu backgrounds */
	private Map<Integer, Texture> finishedBgr;

	/** Map with level backgrounds */
	private Map <Integer, Texture> levelBgr;

	private Boolean debug = false; 
	
	/**
	 * Setups the parts necessary for the game.
	 */
	public void create() {

		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		SaveHandler.load();

		EventSupport.getInstance().addListner(this);
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		gsm = new GameStateManager(this);
		initHashMap();
		initLevelBgr();

		theLevel = new MainMenu(gsm);
		gsm.pushState(theLevel);

	}
	
	public void toggleDebug(){
		debug = !debug;
	}
	
	public Boolean isDebug(){
		return debug;
	}
	
	public String getDebugStatus(){
		if(debug){
			return "on";
		}else{
			return "off";
		}
	}

	/**
	 * This method is looped continuously. Updates the input and 
	 * handles it.
	 */
	public void render() {
		accum+=Gdx.graphics.getDeltaTime();
		while (accum >= STEP){
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			handleInput();
			MyInput.update();
		}

	}
	
	public void setTheState(GameState gs){
		MyInput.setAllKeysFalse();
		theLevel = gs;
	}
	
	
	/**
	 * 
	 * @param state
	 */
	/*
	public void setLevel(GameState state){
		MyInput.setAllKeysFalse();
		theLevel = state;

		gsm.setState(theLevel);
	}*/



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
		
		if(theLevel instanceof Level){
			if(MyInput.isDown(MyInput.BUTTON_FORWARD)){
				theLevel.handleInput(MyInput.BUTTON_FORWARD);
			} else if(MyInput.isDown(MyInput.BUTTON_BACKWARD)){
				theLevel.handleInput(MyInput.BUTTON_BACKWARD);
			} else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){
				theLevel.handleInput(-1);
			}
		} else {
			if(MyInput.isPressed(MyInput.BUTTON_FORWARD)){
				theLevel.handleInput(MyInput.BUTTON_FORWARD);
			}
			if(MyInput.isPressed(MyInput.BUTTON_BACKWARD)){
				theLevel.handleInput(MyInput.BUTTON_BACKWARD);
			}
		}

		if(MyInput.isPressed(MyInput.BUTTON_ENTER)){

			theLevel.handleInput(MyInput.BUTTON_ENTER);

		}else if(MyInput.isDown(MyInput.BUTTON_RESTART)){

			theLevel.handleInput(MyInput.BUTTON_RESTART);

		}else if(MyInput.isDown(MyInput.BUTTON_ESCAPE)){

			theLevel.handleInput(MyInput.BUTTON_ESCAPE);

		}else if(MyInput.isPressed(MyInput.BUTTON_PAUSE)){
			theLevel.handleInput(MyInput.BUTTON_PAUSE);
		}else {
			theLevel.handleInput(-2);
		}
	}

	public void dispose() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}
	public void pause() {}

	public void eventRecieved(TheEvent evt) {
		if(theLevel instanceof Level){	
			if(evt.getNameOfEvent().equals("spikehit")){
				//setLevel(new Level(gsm, gsm.getCurrentTiledMap()));
			}
			if(evt.getNameOfEvent().equals("pause")){
				theLevel.handleInput(MyInput.BUTTON_PAUSE);
			}
		}
		if(theLevel instanceof IMenu){
			if(evt.getNameOfEvent().equals("selectMenuItem")){
				((IMenu) theLevel).select(evt.getX(), evt.getY());
			}
			if(evt.getNameOfEvent().equals("currentMenuItem")){
				((IMenu) theLevel).setCurrentItem(evt.getX(), evt.getY());
			}

			if(evt.getNameOfEvent().equals("resumegame")){
				//setLevel(evt.getGame());
			}
		}


	}

	public void setLevelFinished(int i){
		LevelFinished state = new LevelFinished(gsm, i);
		gsm.setState(state);
	}

	private void initHashMap(){ // this class should be in Content
		finishedBgr = new HashMap<Integer, Texture>();
		finishedBgr.put(1,  new Texture("res/menu/lol.jpg"));
		finishedBgr.put(2,  new Texture("res/menu/lol.jpg"));
		finishedBgr.put(3,  new Texture("res/menu/lol.jpg"));
		finishedBgr.put(4,  new Texture("res/menu/lol.jpg"));
	}
	
	private void initLevelBgr(){ // this class should be in Content
		levelBgr = new HashMap<Integer, Texture>();
		levelBgr.put(1,  new Texture("res/menu/domo.jpg"));
	}
}
