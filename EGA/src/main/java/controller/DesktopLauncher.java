package controller;

import lombok.Data;
<<<<<<< HEAD

import com.badlogic.gdx.Gdx;
/* why cant i import? Do i need the imports?
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.jogl.JoglApplicationConfiguration;
*/
=======
>>>>>>> master
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
