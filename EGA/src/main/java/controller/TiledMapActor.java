package controller;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TiledMapActor extends Actor {

    private TiledMap tiledMap;

    private TiledMapTileLayer tiledLayer;

    TiledMapTileLayer.Cell cell;

    public TiledMapActor(TiledMap tiledMap, TiledMapTileLayer tiledLayer, TiledMapTileLayer.Cell cell) {
        this.tiledMap = tiledMap;
        this.tiledLayer = tiledLayer;
        this.cell = cell;
    }

}