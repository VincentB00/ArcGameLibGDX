package model.matrix;

import com.badlogic.gdx.math.Vector2;

import helper.EnumTag;
import model.GameObject;

public class WeightTile extends GameObject
{
    public final int INF = Integer.MAX_VALUE;
    public int weight;
    public String vertex;
    public String originalWeight;


    //for A* algorithm
    public double g = 0;
    public double h = 0;
    public double f = 0;
    public WeightTile previousTile;
    public boolean isTeleportTile = false;
    
    public WeightTile(String vertex, int weight)
    {
        // super("Assets/craftpix-net-198222-free-industrial-zone-tileset-pixel-art/1 Tiles/IndustrialTile_03.png", "Tile(s)", EnumTag.tile.tag);
        super();
        this.vertex = vertex;
        this.weight = weight;
    }

    public WeightTile(String vertex, String weight)
    {
        // super("Assets/craftpix-net-198222-free-industrial-zone-tileset-pixel-art/1 Tiles/IndustrialTile_03.png", "Tile(s)", EnumTag.tile.tag);
        super();
        this.vertex = vertex;

        if(isInteger(weight))
            this.weight = Integer.parseInt(weight);
        else
        {
            this.weight = INF;
            this.originalWeight = weight;
        }
    }

    public boolean isInteger(String num)
    {
        try
        {
            Integer.parseInt(num);
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    public double calculateF(double gT)
    {
        return gT + h;
    }

    public void cleanUp()
    {
        g = 0;
        h = 0;
        f = 0;
        previousTile = null;
    }
}
