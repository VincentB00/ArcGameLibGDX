package helper;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapHelper 
{
    private TiledMap tiledMap;
    
    public TileMapHelper()
    {

    }

    public OrthogonalTiledMapRenderer setupMap(String tileMapPath)
    {
        tiledMap = new TmxMapLoader().load(tileMapPath);
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public OrthogonalTiledMapRenderer setupMap()
    {
        tiledMap = new TmxMapLoader().load("Assets/Maps/map1.tmx");
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void dispose()
    {
        tiledMap.dispose();
    }
}
