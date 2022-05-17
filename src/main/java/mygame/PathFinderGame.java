package mygame;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntFloatMap.Entry;

import org.lwjgl.Sys;

import helper.EnumConstants;
import helper.EnumRenderObject;
import helper.EnumTag;
import model.Map;
import model.Renderer;
import model.matrix.TileMapMatrix;
import model.matrix.WeightTile;
import model.player_model.AutoSpaceShip;
import model.player_model.Player;

public class PathFinderGame extends Map
{
    AutoSpaceShip player;
    TileMapMatrix tileMapMatrix;
    boolean readyToFindPath;
    WeightTile currentPlayerPosition;

    public PathFinderGame(Renderer renderer)
    {
        super(renderer);
    }

    @Override
    public void start() 
    {
        super.start();
        readyToFindPath = true;
        
        //matrix
        tileMapMatrix = new TileMapMatrix(new Vector2(100, 150), new Vector2(20, 20), 7, 19);
        tileMapMatrix.fillTileMap();
        tileMapMatrix.setAllToGridPosition();
        tileMapMatrix.registerRenderAllObject();
        tileMapMatrix.setHashMapOnGrid();

        // WeightTile start = tileMapMatrix.getByGrid(0, 1);
        // WeightTile goal = tileMapMatrix.getByGrid(0, 0);

        // LinkedList<WeightTile> list = tileMapMatrix.calculateAStar(start, goal);

        // for (WeightTile weightTile : list) 
        // {
        //     System.out.println(tileMapMatrix.tileHashMap.get(weightTile) + " vertex: " + weightTile.vertex);
        // }

        // Gdx.app.exit();

        //player
        player = new AutoSpaceShip();
        EnumRenderObject.renderObject.list.addLast(player);

        currentPlayerPosition = tileMapMatrix.tileMap.getFirst().getFirst();
        player.position.set(currentPlayerPosition.getMiddlePosition());

        
    }

    @Override
    public void update(float delta) 
    {
        super.update(delta);

        // for (java.util.Map.Entry<WeightTile, Vector2> entry : tileMapMatrix.tileHashMap.entrySet()) 
        // {
        //     WeightTile weightTile = entry.getKey();

        //     if(weightTile.hitBox.overlaps(r))
        //     {

        //     }
        // }

        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && !player.haveDestination)
        {
            for (java.util.Map.Entry<WeightTile, Vector2> entry : tileMapMatrix.tileHashMap.entrySet()) 
            {
                WeightTile weightTile = entry.getKey();

                if(weightTile.isClickByRightMouse())
                {
                    if(weightTile.weight == weightTile.INF)
                        break;

                    LinkedList<WeightTile> list = tileMapMatrix.findSortestPath(currentPlayerPosition, weightTile);

                    for (WeightTile weightTile1 : list) 
                    {
                        System.out.println(tileMapMatrix.tileHashMap.get(weightTile1) + " vertex: " + weightTile1.vertex);
                    }

                    System.out.println(String.format("------------%s-----------", tileMapMatrix.calculateTotalWeight(list)));
                    
                    player.setDestination(list);

                    currentPlayerPosition = weightTile;

                    break;
                }
            }
        }
    }

    private Vector2 getMousePosition()
    {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        return new Vector2(x, EnumConstants.screenH.ratio - y);
    }

    @Override
    public void render(float delta) 
    {
        super.render(delta);


    }
}
