package model;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import controller.Variables;

public class Content {
	
	private Map<String, Texture> textures;
	//private HashMap<String, Music> music;
	private Map<String, Sound> sounds;
	
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
	
	public void playSound(String key){
		sounds.get(key).play(Variables.music_volume);
	}
	
	public Sound getSound(String key) {
		return sounds.get(key);
	}
	
	public void removeSound(String key) {
		Sound sound = sounds.get(key);
		if(sound != null) {
			sounds.remove(key);
			sound.dispose();
		}
	}
	
}
