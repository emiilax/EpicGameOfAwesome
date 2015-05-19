package controller;

import lombok.Data;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

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
    	
    	
    	new LwjglApplication(new EGA(), cfg);
    	
    }
}
