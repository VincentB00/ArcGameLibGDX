package mygame;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import org.lwjgl.Sys;

import model.Map;
import model.Renderer;
import model.NPC_model.BigRedSpaceShip;
import model.player_model.BlueShipPlayer;

public class StageMachineGame extends Map 
{
    BigRedSpaceShip guard;
    BlueShipPlayer player;

    public StageMachineGame(Renderer renderer)
    {
        super(renderer);

        renderer.tiledMap = new TmxMapLoader().load("C:/Users/vince/OneDrive/study/Oswego/CSC455/HW6/stageMachineGame.tmx");
        renderer.orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(renderer.tiledMap);
        renderer.orthogonalTiledMapRenderer.setView(renderer.camera);

        guard = new BigRedSpaceShip("Guard 1");
        guard.setStartPosition(new Vector2(1024, 356));
        guard.addRotation(270);
        guard.baseRotation = -90;


    }

    @Override
    public void start() 
    {
        super.start();

        

        player = new BlueShipPlayer();
        player.useStearingBehavior = false;
        player.moveSpeed *= 2;
    }

    @Override
    public void render(float delta) 
    {
        super.render(delta);
        
    }

    @Override
    public void update(float delta) 
    {
        super.update(delta);
        guard.onCollisionWith(player);
        player.lookAt(this.mousePoint);
    }
}
