package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class tmp {
	public static void main( String[] args ) {
		
    	LwjglApplicationConfiguration cfg = 
    			new LwjglApplicationConfiguration();
    	
    	cfg.title = Game.TITLE;
    	cfg.width = Game.V_WIDTH * Game.SCALE;
    	cfg.height = Game.V_HEIGTH * Game.SCALE;
    	GdxNativesLoader.load();
    	new LwjglApplication(new Game(), cfg);
    	GdxNativesLoader.load();
    }
}
