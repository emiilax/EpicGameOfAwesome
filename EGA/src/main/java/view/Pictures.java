package view;

import java.util.HashMap;

import model.Content;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/*
 * @author Rebecka Reitmaier, all in the project adds elements here
 * This class loads the pictures into the game
 */

public class Pictures {
	
	public static Content res;
	private HashMap<Integer, TiledMap> maps;
	
	public Pictures(){
		createPictures();
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
