package controller;

import lombok.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.jogl.JoglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.graphics.GL20;
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
    	
    	//LwjglApplicationConfiguration.disableAudio = false;
    	//GdxNativesLoader.load();
    	
    	new LwjglApplication(new EGA(), cfg);
    	
    	//GdxNativesLoader.load(); tror ej denna behövs /rebecka
    }
}
