package io;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import controller.SaveHandler;


/**
 * @author Rebecka Reitmaier, Emil Axelsson
 * Content is a Singleton class that contains all pictures 
 * and tiledMaps for EGA.
 */
public class Content {
	/** Map for the textures*/
	private Map<String, Texture> textures;
	
	/** Map for the sound*/
	private Map<String, Sound> sounds;
	
	/** HashMap for the levels*/
	private HashMap<Integer, TiledMap> maps;
	
	/** The instance */
	private static Content instance;
	
	/** The constructor, it creates the HashMaps with sound and textures */
	private Content(){
		
		textures = new HashMap<String, Texture>();
		sounds = new HashMap<String, Sound>();
		
		loadImages();
		loadMaps();
		loadSounds();
		
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
	 * load the pictures for EGA
	 */
	private void loadImages(){
		loadTexture("res/stars/star.png", "star");
		loadTexture("res/characters/smallCharacter_test1.png", "smallplayer");
		loadTexture("res/characters/bigCharacter_test1.png", "bigPlayer");
		loadTexture("res/stars/bigBigStar.png", "bigStar");
		loadTexture("res/door/openDoor.png", "openDoor");
		loadTexture("res/door/closedDoor.png", "lockedDoor");
		loadTexture("res/tiles/upSpikes_16x21.png", "upSpike");
		loadTexture("res/tiles/downSpikes_16x21.png", "downSpike");
		loadTexture("res/tiles/leftSpikes_21x16.png", "leftSpike");
		loadTexture("res/tiles/rightSpikes_21x16.png", "rightSpike");
		loadTexture("res/key/7key.png", "key");
	
	}
	/**
	 * load the maps for EGA and adds them to the HashMap "maps"
	 */
	private void loadMaps(){
		TiledMap level1 = new TmxMapLoader().load("res/maps/level1.tmx");
		TiledMap level2 = new TmxMapLoader().load("res/maps/level2.tmx");
		TiledMap level3 = new TmxMapLoader().load("res/maps/level3.tmx");
		TiledMap level7 = new TmxMapLoader().load("res/maps/level99.tmx");
		
		maps = new HashMap<Integer, TiledMap>();
		maps.put(1, level1);
		maps.put(2, level2);
		maps.put(3, level3);
		maps.put(7, level7);
		}
	/**
	 * load the sound for EGA
	 */
	public void loadSounds(){
		loadSound("res/sound/sound_mariojump.wav", "jump");
		loadSound("res/sound/sound_forward.wav", "forward");
		loadSound("res/sound/eriksmamma.wav", "grow");
		loadSound("res/sound/sound_ta-da.wav", "finish");
		loadSound("res/sound/sound_shrink.wav", "shrink");
		loadSound("res/sound/sound_unlockdoor.wav", "unlock");
		loadSound("res/sound/sound_collectkey.wav", "collectkey");
		loadSound("res/sound/sound_oflyt.wav", "fail");
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
}