package controller;

import java.util.HashMap;

import view.GameState;
import view.Level;
import view.LevelFinished;
import view.MenuState;
import lombok.Data;
import model.Content;
import model.GameData;
import model.MyInput;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
	
	//private SaveHandler saveHandler;
	private GameData gameData;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	private GameState theLevel;
	
	public static Content res;
	
	private HashMap<Integer, TiledMap> maps;

	private HashMap<Integer, Texture> finishedBgr;

	public void create() {
		
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		createPictures();
		
		SaveHandler.load();
		//SaveHandler.getGameData();
		
		EventSupport.getInstance().addListner(this);
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		gsm = new GameStateManager(this);
		initHashMap();
		
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

		}else if(MyInput.isDown(MyInput.BUTTON_RESTART)){

			theLevel.handleInput(MyInput.BUTTON_RESTART);

		}else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			theLevel.handleInput(-1);

		}
	}
	
	public void dispose() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}
	public void pause() {}

	public void eventRecieved(TheEvent evt) {
		if(theLevel instanceof Level){	
			if(evt.getNameOfEvent().equals("spikehit")){
				setLevel(new Level(gsm, gsm.getCurrentLevel()));
			}
			if(evt.getNameOfEvent().equals("pause")){
				theLevel.handleInput(MyInput.BUTTON_PAUSE);
			}
		}
		if(theLevel instanceof MenuState){
			/*if(evt.getNameOfEvent().equals("startLevel")){
				setLevel(new Level(gsm, gsm.getCurrentLevel()));
			}
			if(evt.getNameOfEvent().equals("levelSelect")){
				//put code here
			}
			if(evt.getNameOfEvent().equals("settings")){
				//put code here
			}
			if(evt.getNameOfEvent().equals("quit")){
				SaveHandler.save();
				Gdx.app.exit();
			}*/
			if(evt.getNameOfEvent().equals("selectMenuItem")){
				((MenuState) theLevel).select(evt.getX(), evt.getY());
			}
			if(evt.getNameOfEvent().equals("currentMenuItem")){
				((MenuState) theLevel).setCurrentItem(evt.getX(), evt.getY());
			}/*
			if(evt.getNameOfEvent().equals("currentMenuItem0")){
				((MenuState) theLevel).setCurrentItem(0);
			}
			if(evt.getNameOfEvent().equals("currentMenuItem1")){
				((MenuState) theLevel).setCurrentItem(1);
			}
			if(evt.getNameOfEvent().equals("currentMenuItem2")){
				((MenuState) theLevel).setCurrentItem(2);
			}
			if(evt.getNameOfEvent().equals("currentMenuItem3")){
				((MenuState) theLevel).setCurrentItem(3);
			}*/
		}
	}
	
	/* 
	 * @author Rebecka Reitmaier
	 * creates the Pictures 
	 * this is also in the new class Pictures in View
	 */
	private void createPictures(){
		res = new Content();
			
		res.loadTexture("res/tiles/bunny.png", "bunny");
		res.loadTexture("res/stars/star.png", "star");
		res.loadTexture("res/tiles/hud.png", "hud");
		res.loadTexture("res/characters/redball_small.png", "smallplayer");
		res.loadTexture("res/characters/redball_big.png", "bigPlayer");
		res.loadTexture("res/stars/bigStar.png", "bigStar");
		res.loadTexture("res/door/openDoor.jpg", "openDoor");
		res.loadTexture("res/door/closedDoor.jpg", "lockedDoor");
		res.loadTexture("res/tiles/upSpikes_16x21.png", "upSpike");
		res.loadTexture("res/tiles/downSpikes_16x21.png", "downSpike");
		res.loadTexture("res/tiles/leftSpikes_21x16.png", "leftSpike");
		res.loadTexture("res/tiles/rightSpikes_21x16.png", "rightSpike");
		res.loadTexture("res/key/key-4.png", "key");
		
		createMaps();
	
	}
	
	/* 
	 * @author Rebecka Reitmaier
	 * creates the Maps to levels and puts them in the hashMap maps
	 * this is also in the new class Pictures in View
	 */

	private void createMaps(){
		TiledMap level1 = new TmxMapLoader().load("res/maps/testmap.tmx");
		TiledMap level2 = new TmxMapLoader().load("res/maps/testmap2.tmx");
		TiledMap level3 = new TmxMapLoader().load("res/maps/testmap.tmx");
		
		maps = new HashMap<Integer, TiledMap>();
		maps.put(1, level1);
		maps.put(2, level2);
		maps.put(3, level3);
		
		}
	
	/*
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

	
	public void setLevelFinished(int i){
		LevelFinished state = new LevelFinished(gsm, finishedBgr.get(i), i);
		setLevel(state);
	}
	
	private void initHashMap(){
		finishedBgr = new HashMap<Integer, Texture>();
		finishedBgr.put(1,  new Texture("res/menu/lol.jpg"));
	}
}
