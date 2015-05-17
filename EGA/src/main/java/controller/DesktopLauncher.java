package controller;

import lombok.Data;

import com.badlogic.gdx.Gdx;
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
    	
    	cfg.title = EGA.TITLE;
    	cfg.width = EGA.V_WIDTH * EGA.SCALE;
    	cfg.height = EGA.V_HEIGTH * EGA.SCALE;
    	GdxNativesLoader.load();
    	new LwjglApplication(new EGA(), cfg);
    	
    	//GdxNativesLoader.load(); tror ej denna behövs /rebecka
    }
}
