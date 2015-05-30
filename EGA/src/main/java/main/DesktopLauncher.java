package main;

import lombok.Data;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import controller.EGA;

/**
 * Launches the application
 *
 */


@Data
public class DesktopLauncher {
	
	public static void main( String[] args ) {
		
    	
    	LwjglApplicationConfiguration cfg = 
    			new LwjglApplicationConfiguration();
    	
    	cfg.title = EGA.TITLE;
    	cfg.width = EGA.V_WIDTH * EGA.SCALE;
    	cfg.height = EGA.V_HEIGTH * EGA.SCALE;
    	cfg.resizable = false;
    	cfg.x = -1;
    	cfg.y = 0;
    	
    	new LwjglApplication(new EGA(), cfg);
    	
    }
}
