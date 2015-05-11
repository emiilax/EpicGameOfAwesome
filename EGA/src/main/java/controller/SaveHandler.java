package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.GameData;

import com.badlogic.gdx.Gdx;

public class SaveHandler {

	public SaveHandler(){

	}

	public static GameData gd;

	public static void save() {
		try{
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("highScores.sav")
					);
			out.writeObject(gd);
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	public static void load(){
		try{
			if(!saveFileExists()){
				init();
				return;
			}
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream("highScores.sav")
					);
			gd = (GameData)input.readObject();
			input.close();
		} 
		catch(IOException e) {
			e.printStackTrace();
			Gdx.app.exit();
		} 
		catch (ClassNotFoundException e) {			
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	public static boolean saveFileExists(){
		File f = new File ("highScores.sav");
		return f.exists();
	}

	public static void init(){
		gd = new GameData();
		save();
	}
}
