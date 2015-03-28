package main;

import lombok.Data;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;

/**
 * Hello world!
 *
 */
@Data
public class DesktopLauncher {
	
    public static void main( String[] args ) {
    	
    	
    	LwjglApplicationConfiguration cfg = 
    			new LwjglApplicationConfiguration();
    	
    	cfg.title = Game.TITLE;
    	cfg.width = Game.V_WIDTH * Game.SCALE;
    	cfg.height = Game.V_HEIGTH * Game.SCALE;
    	new LwjglApplication(new Game(), cfg);
    }
}
