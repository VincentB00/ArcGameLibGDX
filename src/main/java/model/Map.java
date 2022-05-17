package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import helper.EnumConstants;
import helper.TileMapHelper;

public class Map 
{
    public Renderer renderer;

    public OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    public TileMapHelper tileMapHelper;
    public boolean renderTileMap = false;

    public GameObject mousePoint;

    public Map()
    {
        renderTileMap = false;
        mousePoint = new GameObject();
        start();
    }

    public Map(Renderer renderer)
    {
        this.renderer = renderer;
        mousePoint = new GameObject();
        start();
    }

    public void start()
    {
        mousePoint.enableCollision = false;
        mousePoint.enableTriggerCollision = false;
    }

    public void setTileMap(String path)
    {
        renderTileMap = true;
        if(tileMapHelper != null)
            tileMapHelper.dispose();

        if(orthogonalTiledMapRenderer != null)
            orthogonalTiledMapRenderer.dispose();

        this.tileMapHelper = new TileMapHelper();
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap(path);
    }

    private void renderTileMap()
    {
        orthogonalTiledMapRenderer.setView(renderer.camera);
        orthogonalTiledMapRenderer.render();
    }

    public void update(float delta)
    {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector2 mouseV = new Vector2(x, EnumConstants.screenH.ratio - y);
        mousePoint.position.set(mouseV);
    }


    public void render(float delta)
    {
        if(renderTileMap)
            renderTileMap();

        update(delta);
    }

    public void dispose()
    {
        renderTileMap = false;
        orthogonalTiledMapRenderer.dispose();
        tileMapHelper.dispose();
    }
}
