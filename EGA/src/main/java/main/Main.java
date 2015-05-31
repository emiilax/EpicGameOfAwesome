package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;

import controller.EGA;

/**
 * Only used by Erik because DesktopLauncher doesn't work for some reason
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
