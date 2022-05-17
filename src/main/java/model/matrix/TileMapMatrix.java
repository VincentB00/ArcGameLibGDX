package model.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import org.lwjgl.Sys;

import helper.EnumRenderObject;

public class TileMapMatrix 
{
    public LinkedList<LinkedList<WeightTile>> tileMap;
    public int[][] graph;
    public Vector2 distance;
    public Vector2 startPosition;
    public int maxColumn = 3;
    public int maxRow = 3;
    public HashMap<WeightTile, Vector2> tileHashMap;
    public HashMap<WeightTile, WeightTile> teleportHashMap;

    public TileMapMatrix(Vector2 distance, Vector2 startPosition, int maxRow, int maxColumn)
    {
        tileMap = new LinkedList<LinkedList<WeightTile>>();
        this.distance = distance;
        this.startPosition = startPosition;
        this.maxColumn = maxColumn;
        this.maxRow = maxRow;
        tileHashMap = new HashMap<WeightTile, Vector2>();
        teleportHashMap = new HashMap<WeightTile, WeightTile>();
        while(tileMap.size() < maxRow)
            tileMap.add((new LinkedList<WeightTile>()));
    }

    public void fillTileMap()
    {
        // int maxTile = maxRow * maxColumn;

        try 
        {
            LinkedList<String[]> lines = new LinkedList<>();
            BufferedReader br = new BufferedReader(new FileReader(new File("Assets/File Input/TileMap.TXT")));

            String line;
            while ((line = br.readLine()) != null)
            {
                String[] weights = line.split(" ");

                lines.addFirst(weights);

                // for (String string : weights) 
                // {
                //     addWeightTile(new WeightTile(string, string));
                // }
            }

            for (String[] strings : lines) 
            {
                for (String string : strings) 
                {
                    addWeightTile(new WeightTile(string, string));
                }
            }

            br.close();
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }

    public void setHashMapOnGrid()
    {
        for(int row = 0; row < tileMap.size(); row++)
        {
            for(int column = 0; column < tileMap.get(row).size(); column++)
            {
                tileHashMap.put(tileMap.get(row).get(column), new Vector2(column, row));
            }
        }

        //for teleport portal
        for (java.util.Map.Entry<WeightTile, Vector2> entry : tileHashMap.entrySet()) 
        {
            WeightTile weightTile = entry.getKey();

            if(weightTile.vertex.contains("T") && !teleportHashMap.containsKey(weightTile))
            {
                WeightTile theOtherPortal = getConnectedPortal(weightTile);
                if(theOtherPortal != null)
                {
                    weightTile.isTeleportTile = true;
                    theOtherPortal.isTeleportTile = true;
                    weightTile.weight = 0;
                    theOtherPortal.weight = 0;
                    teleportHashMap.put(weightTile, theOtherPortal);
                    teleportHashMap.put(theOtherPortal, weightTile);
                }
            }
        }
    }

    private WeightTile getConnectedPortal(WeightTile startPortal)
    {
        for (java.util.Map.Entry<WeightTile, Vector2> entry : tileHashMap.entrySet()) 
        {
            WeightTile weightTile = entry.getKey();

            if(weightTile != startPortal && weightTile.vertex.compareTo(startPortal.vertex) == 0)
                return weightTile;
        }

        return null;
    }

    public WeightTile get(int row, int column)
    {
        return tileMap.get(row).get(column);
    }

    public WeightTile getByGrid(int x, int y)
    {
        return tileMap.get(y).get(x);
    }

    public void addWeightTile(WeightTile weightTile)
    {
        LinkedList<WeightTile> spot = findNextSpot();

        if(spot != null)
        {
            spot.add(weightTile);
        }
    }

    public LinkedList<WeightTile> findNextSpot()
    {
        int countRow = 0;
        while(tileMap.get(countRow).size() >= maxColumn && countRow < maxRow)
            countRow++;

        if(countRow < maxRow && tileMap.get(countRow).size() < maxColumn)
            return tileMap.get(countRow);
        else
            return null;
    }

    public boolean isFull()
    {
        int countRow = 0;
        while(tileMap.get(countRow).size() >= maxColumn && countRow < maxRow)
            countRow++;

        if(countRow < maxRow)
            return false;
        else
            return true;
    }

    public void registerRenderAllObject()
    {
        for (LinkedList<WeightTile> linkedList : tileMap) 
        {
            for (WeightTile tile : linkedList) 
            {
                EnumRenderObject.renderObject.list.add(tile);
            }
        }
    }

    public void setAllToGridPosition()
    {
        float x = startPosition.x;
        float y = startPosition.y;
        for (LinkedList<WeightTile> linkedList : tileMap) 
        {
            for (WeightTile tile : linkedList) 
            {
                tile.position.set(x, y);

                x +=  distance.x;
            }
            y += distance.y;
            x = startPosition.x;
        }
    }

    public void dispose()
    {
        for (LinkedList<WeightTile> linkedList : tileMap) 
        {
            for (WeightTile tile : linkedList) 
            {
                tile.dispose();
            }
        }
        tileMap.clear();
    }

    public WeightTile getSouth(int x, int y)
    {
        if(y <= 0)
            return null;

        int maxRow = tileMap.size();
        int south = (y - 1 + maxRow) % maxRow;
        return getByGrid(x, south);
    }

    public WeightTile getNorth(int x, int y)
    {
        int maxRow = tileMap.size();

        if(y >= maxRow - 1)
            return null;

        int north = (y + 1) % maxRow;
        return getByGrid(x, north);
    }

    public WeightTile getWest(int x, int y)
    {
        if(x <= 0)
            return null;

        int maxColumn = tileMap.getFirst().size();
        int west = (x - 1 + maxColumn) % maxColumn;
        return getByGrid(west, y);
    }

    public WeightTile getEast(int x, int y)
    {
        int maxColumn = tileMap.getFirst().size();

        if(x >= maxColumn - 1)
            return null;

        int east = (x + 1) % maxColumn;
        return getByGrid(east, y);
    }


    //logic

    public LinkedList<WeightTile> findSortestPath(WeightTile start, WeightTile goal)
    {
        LinkedList<LinkedList<WeightTile>> allSortedPath = new LinkedList<LinkedList<WeightTile>>();

        allSortedPath.add(calculateAStar(start, goal));

        if(teleportHashMap.size() <= 0)
            return allSortedPath.getFirst();

        for (java.util.Map.Entry<WeightTile, WeightTile> entry : teleportHashMap.entrySet()) 
        {
            WeightTile startProtal = entry.getKey();
            WeightTile endPortal = entry.getValue();
            
            LinkedList<WeightTile> firstPath = calculateAStar(start, startProtal);
            LinkedList<WeightTile> secondPath = calculateAStar(endPortal, goal);

            LinkedList<WeightTile> totalPath = firstPath;

            for (WeightTile weightTile : secondPath) 
            {
                totalPath.addLast(weightTile);
            }

            allSortedPath.add(totalPath);
        }

        int path = 0;
        for(int count = 0; count < allSortedPath.size(); count++)
        {
            if(calculateTotalWeight(allSortedPath.get(count)) < calculateTotalWeight(allSortedPath.get(path)))
            {
                path = count;
            }
        }

        return allSortedPath.get(path);
    }

    public LinkedList<WeightTile> calculateAStar(WeightTile start, WeightTile goal)
    {
        LinkedList<WeightTile> open = new LinkedList<>();
        LinkedList<WeightTile> closeed = new LinkedList<>();

        calculateHForAllTile(goal);

        start.g = 0;
        start.f = start.calculateF(0);
        open.add(start);
        
        while(!open.contains(goal))
        {
            WeightTile lookAt = getLowestF(open);
            // System.out.println(String.format("Look At(%s) F: %s, ", tileHashMap.get(lookAt), lookAt.f));
            look(lookAt, open, closeed);
            // System.out.println(String.format("Open list: %s", printList(open)));
            // System.out.println(String.format("closed list: %s", printList(closeed)));
        }

        LinkedList<WeightTile> tracePath = new LinkedList<WeightTile>();
        WeightTile trace = goal;
        while(trace != start)
        {
            tracePath.addFirst(trace);
            trace = trace.previousTile;
        }
        
        tracePath.addFirst(start);

        cleanUpAllTile();

        return tracePath;
    }

    public double calculateTotalWeight(LinkedList<WeightTile> weightTiles)
    {
        double total = 0;

        for (WeightTile weightTile : weightTiles) 
        {
            total+=weightTile.weight;
        }

        return total - weightTiles.getFirst().weight;
    }

    private String printList(LinkedList<WeightTile> list)
    {
        String result = "";
        for (WeightTile weightTile : list) 
        {
            result += String.format("position: %s && F: %s", tileHashMap.get(weightTile), weightTile.f);
        }
        return "[" + result + "]";
    }

    public void calculateHForAllTile(WeightTile goal)
    {
        for (LinkedList<WeightTile> row : tileMap) 
        {
            for (WeightTile columnTile : row) 
            {
                columnTile.h = calculateH(columnTile, goal);
            }
        }
    }

    private WeightTile getLowestF(LinkedList<WeightTile> open)
    {
        int result = 0;
        for(int count = 0; count < open.size(); count++)
        {
            if(open.get(count).f < open.get(result).f || (open.get(count).f == open.get(result).f && open.get(count).h < open.get(result).h))
                result = count;
        }

        return open.get(result);
    }

    private void look(WeightTile current, LinkedList<WeightTile> open, LinkedList<WeightTile> closeed)
    {
        LinkedList<WeightTile> discoverTile = new LinkedList<>();
        Vector2 currentTargetPositionOnGrid = tileHashMap.get(current);
        int x = (int)currentTargetPositionOnGrid.x;
        int y = (int)currentTargetPositionOnGrid.y;
        discoverTile.add(getNorth(x, y));
        discoverTile.add(getSouth(x, y));
        discoverTile.add(getWest(x, y));
        discoverTile.add(getEast(x, y));

        for (WeightTile weightTile : discoverTile) 
        {
            if(weightTile != null && !closeed.contains(weightTile))
            {
                Double g = calculateG(current, weightTile);
                Double f = weightTile.calculateF(g);
                if(f < weightTile.f || weightTile.f == 0)
                {
                    weightTile.g = g;
                    weightTile.f = f;
                    weightTile.previousTile = current;
                }

                if(!open.contains(weightTile))
                    open.add(weightTile);
            }
        }

        open.remove(current);
        closeed.add(current);
    }

    private double calculateG(WeightTile currentTarget, WeightTile newTarget)
    {
        return currentTarget.g + newTarget.weight;
    }

    private double calculateH(WeightTile target, WeightTile goal)
    {
        Vector2 targetV = tileHashMap.get(target);
        Vector2 goalV = tileHashMap.get(goal);
        double xDistance = Math.abs(goalV.x - targetV.x);
        double yDistance = Math.abs(goalV.y - targetV.y);

        return xDistance + yDistance;
    }

    public void createGraph()
    {
        graph = new int[tileMap.size()][tileMap.getFirst().size()];

        for(int row = 0; row < tileMap.size(); row++)
        {
            LinkedList<WeightTile> rowList = tileMap.get(row);
            for(int column = 0; column < rowList.size(); column++)
            {
                graph[row][column] = rowList.get(column).weight;
            }
        }
    }

    public void printGraph()
    {
        for(int row = 0; row < graph.length; row++)
        {
            for(int column = 0; column < graph[0].length; column++)
            {
                System.out.print(graph[row][column] + "\t");
            }
            System.out.println();
        }
    }

    public void cleanUpAllTile()
    {
        for (java.util.Map.Entry<WeightTile, Vector2> entry : tileHashMap.entrySet()) 
        {
            WeightTile weightTile = entry.getKey();

            weightTile.cleanUp();
        }
    }
}
