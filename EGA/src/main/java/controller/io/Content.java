package controller.io;

import java.util.HashMap;
import java.util.Map;

import view.Content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import controller.SaveHandler;
import controller.Variables;
import event.EventSupport;

public class Content {
	/** Map for the textures*/
	private Map<String, Texture> textures;
	/** Map for the sound*/
	private Map<String, Sound> sounds;
	/** HashMap for the levels*/
	private HashMap<Integer, TiledMap> maps;
	/** The instance */
	private static Content instance = null;
	
	public Content(){

		textures = new HashMap<String, Texture>();
		sounds = new HashMap<String, Sound>();
	}
	
	public void loadTexture(String path, String key){
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}
	public Texture getTexture(String key){
		return textures.get(key);
			
	}
	public void disposeTexture(String key){
		Texture tex = textures.get(key);
		
		if(tex != null) tex.dispose();
	}
	
	public void loadSound(String path, String key) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(key, sound);
	}
	
	
	public Sound getSound(String key) {
		return sounds.get(key);
	}

	public void playSound(String key){
		sounds.get(key).play(SaveHandler.getGameData().getSoundVolume());
	}
	
	
	public void removeSound(String key) {
		Sound sound = sounds.get(key);
		if(sound != null) {
			sounds.remove(key);
			sound.dispose();
		}
	}
	/**
	 * The method that returns the singelton object. If the 
	 * instance is = null, it will construvt a new instance
	 * 
	 * @return the instance
	 */
	public synchronized static Content getInstance(){
		if(instance == null){
			instance = new Content();
		}
		return instance;
	}
	/* 
	 * @author Rebecka Reitmaier
	 * creates the Pictures 
	 * this is also in the new class Pictures in View
	 */
	private void createPictures(){
			
		this.loadTexture("res/tiles/bunny.png", "bunny");
		this.loadTexture("res/stars/star.png", "star");
		this.loadTexture("res/tiles/hud.png", "hud");
		this.loadTexture("res/characters/redball_small.png", "smallplayer");
		this.loadTexture("res/characters/redball_big.png", "bigPlayer");
		this.loadTexture("res/stars/bigStar.png", "bigStar");
		this.loadTexture("res/door/openDoor.jpg", "openDoor");
		this.loadTexture("res/door/closedDoor.jpg", "lockedDoor");
		this.loadTexture("res/tiles/upSpikes_16x21.png", "upSpike");
		this.loadTexture("res/tiles/downSpikes_16x21.png", "downSpike");
		this.loadTexture("res/tiles/leftSpikes_21x16.png", "leftSpike");
		this.loadTexture("res/tiles/rightSpikes_21x16.png", "rightSpike");
		this.loadTexture("res/key/key-4.png", "key");
		
		createMaps();
	
	}
	
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
}