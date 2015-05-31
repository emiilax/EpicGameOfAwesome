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

import controller.factory.MenuFactory;
import controller.menus.ChangeControlMenu;
import controller.savehandler.SaveHandler;
import controller.superstate.GameState;
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
		gsm.pushState((new MenuFactory()).getMenu("main"));

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

	/**
	 * Sets the current level and resets the keys clicked.
	 * 
	 * @param gs, the cuurrent gamestate
	 */
	public void setTheState(GameState gs){
		MyInput.setAllKeysFalse();
		if(gs.getClass().equals(Level.class)){
			MyInputProcessor.setActive(false);
		}else{
			MyInputProcessor.setActive(true);
		}
		theLevel = gs;
	}

	/**
	 * Handles the input from the user.
	 * The method will process the input differently,
	 * depending on whether its a level or menu that is
	 * the current state. 
	 */
	public void handleInput() {

		if(theLevel.getClass().equals(Level.class)){
			if(MyInput.isDown(MyInput.BUTTON_FORWARD)){
				theLevel.handleInput(MyInput.BUTTON_FORWARD);
			} else if(MyInput.isDown(MyInput.BUTTON_BACKWARD)){
				theLevel.handleInput(MyInput.BUTTON_BACKWARD);
			} else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){
				theLevel.handleInput(-1);
			}
		}

		boolean isPressed = false;
		for(int i = 0; i < MyInput.keys.length; i++){
			if(theLevel.getClass().equals(Level.class)){
				if(MyInput.isPressed(i) && (i != MyInput.BUTTON_FORWARD || i != MyInput.BUTTON_BACKWARD)){
					theLevel.handleInput(i);
				}

			}else{
				if(MyInput.isPressed(i)){
					theLevel.handleInput(i);
					isPressed = true;
				}
			}
		}
		if(!isPressed) theLevel.handleInput(-2);
	}

	// Unused methods that belongs to ApplicationListener
	public void dispose() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}
	public void pause() {}

	/**
	 * This method is called when there has been an event. 
	 */
	public void eventRecieved(TheEvent evt) {
		String name = evt.getNameOfEvent();

		if(name.equalsIgnoreCase("level")){
			if(evt.getTheLevelNumber() != 0){
				gsm.setCurrentLevel(evt.getTheLevelNumber());
			}

			gsm.setState(new Level(this, gsm.getCurrentTiledMap()));
			return;
		}
		if(name.equalsIgnoreCase("nextlevel")){

			gsm.setCurrentLevel(gsm.getCurrentLevel()+1);		
			gsm.setState(new Level(this, gsm.getCurrentTiledMap()));
			return;

		}

		if(name.equalsIgnoreCase("pop")){

			gsm.popState();
			return;
		}
		if(name.equalsIgnoreCase("finish")){

			gsm.pushState((new MenuFactory()).getLevelFinishedMenu(gsm.getCurrentLevel()));
			return;
		}
		if(name.equals("latestPressed")){
			GameState state = gsm.getCurrentState();
			if(state.getClass().equals(ChangeControlMenu.class)){
				((ChangeControlMenu)state).setKey(evt.getTheLevelNumber());
			}
			return;
		}
		if(name.equals("toggleMouse")){
			MyInputProcessor.setActive(evt.isActive());
			return;
		}
		if(!(name.equalsIgnoreCase("selectMenuItem") || name.equalsIgnoreCase("currentMenuItem"))){
			if(name.equalsIgnoreCase("main")){
				gsm.setState((new MenuFactory()).getMenu(name));
			}else{
				gsm.pushState((new MenuFactory()).getMenu(name));
			}

		}

		theLevel.perform(evt);
		

	}

	/**
	 * @author Rebecka Reitmaier
	 * getTiledMap is a method returns an object from the hashmap maps
	 * OBS: currently only works with ints 1-3
	 * 
	 * @param int i, the map to the level you want
	 * @return TiledMap
	 */
	public TiledMap getTiledMap(int i){
		return maps.get(i);
	}

	/**
	 * When level is finished this method is called. 
	 * @param i, the number of the level that have been done.
	 */


	private void initHashMap(){ // this class should be in Content
		finishedBgr = new HashMap<Integer, Texture>();
		finishedBgr.put(1,  new Texture("res/menu/lol.jpg"));
		finishedBgr.put(2,  new Texture("res/menu/lol.jpg"));
		finishedBgr.put(3,  new Texture("res/menu/lol.jpg"));
		finishedBgr.put(4,  new Texture("res/menu/lol.jpg"));
	}
}
