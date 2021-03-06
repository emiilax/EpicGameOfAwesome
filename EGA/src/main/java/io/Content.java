package io;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


/**
 * @author Rebecka Reitmaier, Emil Axelsson
 * Content is a Singleton class that contains all pictures 
 * and tiledMaps for EGA.
 */
public class Content {
	/** Map for the textures*/
	private Map<String, Texture> textures;
	
	/** Map for the sound*/
	private Map<String, Music> sounds;
	
	/** HashMap for the levels*/
	private HashMap<Integer, TiledMap> maps;
	
	private static Content instance;
	
	/** The constructor, it creates the HashMaps with sound and textures. The constructor is a singleton. */
	private Content(){
		
		textures = new HashMap<String, Texture>();
		sounds = new HashMap<String, Music>();
		loadImages();
		loadMaps();
		loadSounds();
		
	}
	
	/**
	 * Adds a new texture to content
	 * @param path The path of the texture
	 * @param key The name of the texture
	 */
	public void loadTexture(String path, String key){
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}
	
	/**
	 * 
	 * @param key The name of the texture
	 * @return The texture with the specfic key
	 */
	public Texture getTexture(String key){
		return textures.get(key);
			
	}
	
	/**
	 * Removes the texture from content
	 * @param key The name of the texture
	 */
	public void disposeTexture(String key){
		Texture tex = textures.get(key);
		if(tex != null) tex.dispose();
	}
	
	/**
	 * Adds new sound to the content
	 * @param path Path of the sound
	 * @param key The name of the song
	 */
	public void loadSound(String path, String key) {
		Music sound = Gdx.audio.newMusic(Gdx.files.internal(path));
		sounds.put(key, sound);
	}
	
	/**
	 * @param key The name of the song
	 * @return The Music class of the song
	 */
	public Music getSound(String key) {
		return sounds.get(key);
	}
	
	/**
	 * Plays the song with the specific key
	 * @param key The name of the song
	 */
	public void playSound(String key){
		if (key != "fail"){
			sounds.get(key).stop();
		}
		sounds.get(key).setVolume(SaveHandler.getGameData().getSoundVolume());
		sounds.get(key).play();
	}
	
	/**
	 * Stops all sounds from playing
	 */
	public void stopAllSounds(){
		for(Map.Entry<String, Music> entry : sounds.entrySet()){
			entry.getValue().stop();
		}
	}
	
	
	/**
	 * Removes the sound with the specifik key
	 * @param key Name of the song
	 */
	public void removeSound(String key) {
		Music sound = sounds.get(key);
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
	
	/**
	 * Load pictures for ega
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
		TiledMap level2 = new TmxMapLoader().load("res/maps/level10.tmx");
		TiledMap level3 = new TmxMapLoader().load("res/maps/level3.tmx");
		
		TiledMap level4 = new TmxMapLoader().load("res/maps/Rebecka4.tmx");
		TiledMap level5 = new TmxMapLoader().load("res/maps/Rebecka5.tmx");
		TiledMap level6 = new TmxMapLoader().load("res/maps/Rebecka6.tmx");
		TiledMap level7 = new TmxMapLoader().load("res/maps/level99.tmx");
		TiledMap level8 = new TmxMapLoader().load("res/maps/level8.tmx");
		
		TiledMap level9 = new TmxMapLoader().load("res/maps/level9.tmx");
		TiledMap level10 = new TmxMapLoader().load("res/maps/level2.tmx");
		TiledMap level11 = new TmxMapLoader().load("res/maps/level11.tmx");
		TiledMap level12 = new TmxMapLoader().load("res/maps/level12.tmx");
		
		maps = new HashMap<Integer, TiledMap>();
		maps.put(1, level1);
		maps.put(2, level2);
		maps.put(3, level3);
		maps.put(4, level4);
		maps.put(5, level5);
		maps.put(6, level6);
		maps.put(7, level7);
		maps.put(8, level8);
		maps.put(9, level9);
		maps.put(10,  level10);
		maps.put(11,  level11);
		maps.put(12, level12);
		}
	/**
	 * load the sound for EGA
	 */
	public void loadSounds(){
		loadSound("res/sound/sound_mariojump.wav", "jump");
		loadSound("res/sound/sound_forward.wav", "forward");
		loadSound("res/sound/sound_bwawaw.wav", "grow");
		loadSound("res/sound/sound_ta-da.wav", "finish");
		loadSound("res/sound/sound_shrink.wav", "shrink");
		loadSound("res/sound/sound_unlockdoor.wav", "unlock");
		loadSound("res/sound/sound_collectkey.wav", "collectkey");
		loadSound("res/sound/sound_ohnoe.wav", "fail");
	}
	/**
	 * getTiledMap is a method returns an object from the hashmap maps
	 * 
	 * @param int i, the map to the level you want
	 * @return TiledMap
	 */
	public TiledMap getTiledMap(int i){
		return maps.get(i);
	}
}