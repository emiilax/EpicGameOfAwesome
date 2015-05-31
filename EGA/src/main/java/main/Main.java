package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;

import controller.EGA;

/**
 * This is the main class that launches the game. It also sets window size and disables the rezizable function.
 * 
 * @author Erik
 *
 */
public class Main {
public static void main( String[] args ) {
		
    	LwjglApplicationConfiguration cfg = 
    			new LwjglApplicationConfiguration();
    	
    	cfg.title = EGA.TITLE;
    	cfg.width = EGA.V_WIDTH * EGA.SCALE;
    	cfg.height = EGA.V_HEIGTH * EGA.SCALE;
    	cfg.resizable = false;
    	cfg.x = -1;
    	cfg.y = 0;
    	GdxNativesLoader.load();
    	new LwjglApplication(new EGA(), cfg);
    	
    	GdxNativesLoader.load();
    }
}
