package model.matrix;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import model.GameObject;
import model.NPC_model.Enemy;

public class EnemyMatrix 
{
    public LinkedList<LinkedList<Enemy>> enemyMatrix; //row and collumn
    public Vector2 distance;
    public Vector2 startPosition;
    public int maxColumn = 3;
    public int maxRow = 3;
    public int maxEnemy;
    private Random random;

    public EnemyMatrix(Vector2 distance, Vector2 startPosition, int maxRow, int maxColumn)
    {
        enemyMatrix = new LinkedList<LinkedList<Enemy>>();
        this.distance = distance;
        this.startPosition = startPosition;
        this.maxColumn = maxColumn;
        this.maxRow = maxRow;
        while(enemyMatrix.size() < maxRow)
            enemyMatrix.add((new LinkedList<Enemy>()));
        maxEnemy = maxRow * maxColumn;
        random = new Random();
    }

    public int getNumberOfDeadEnemy()
    {
        int count = 0;

        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                if(enemy.statSystem.isDead())
                    count++;
            }
        }

        return count;
    }

    public int getNumberOfNotDeadEnemy()
    {
        int count = 0;

        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                if(!enemy.statSystem.isDead())
                    count++;
            }
        }

        return count;
    }

    public Enemy get(int row, int column)
    {
        return enemyMatrix.get(row).get(column);
    }

    public Enemy getRandom(int row)
    {
        return enemyMatrix.get(row).get(random.nextInt(enemyMatrix.get(row).size()));
    }

    public int getRandomNonEmptyRow()
    {
        int row = random.nextInt(enemyMatrix.size());
        while(enemyMatrix.get(row).size() <= 0)
            row = random.nextInt(enemyMatrix.size());

        return row;
    }

    public Enemy getRandomNonDeadEnemy()
    {
        if(getNumberOfNotDeadEnemy() <= 0)
            return null;

        int ranRow = getRandomNonEmptyRow();
        Enemy tempEnemy = getRandom(ranRow);

        while(tempEnemy.statSystem.isDead())
        {
            ranRow = getRandomNonEmptyRow();
            tempEnemy = getRandom(ranRow);
        }

        return tempEnemy;
    }

    public boolean isAllDead()
    {
        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                if(!enemy.statSystem.isDead())
                    return false;
            }
        }

        return true;
    }

    public void addEnemy(Enemy enemy)
    {
        LinkedList<Enemy> spot = findNextSpot();

        if(spot != null)
        {
            spot.add(enemy);
        }
    }

    public LinkedList<Enemy> findNextSpot()
    {
        int countRow = 0;
        while(enemyMatrix.get(countRow).size() >= maxColumn && countRow < maxRow)
            countRow++;

        if(countRow < maxRow)
            return enemyMatrix.get(countRow);
        else
            return null;
    }

    public boolean isFull()
    {
        int countRow = 0;
        while(enemyMatrix.get(countRow).size() >= maxColumn && countRow < maxRow)
            countRow++;

        if(countRow < maxRow)
            return false;
        else
            return true;
    }

    public void resetAllPosition()
    {
        float x = startPosition.x;
        float y = startPosition.y;
        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                enemy.position.set(x, y);
                x +=  distance.x;
            }
            y += distance.y;
            x = startPosition.x;
        }
    }

    public void registerRenderAllObject()
    {
        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                EnumRenderObject.renderObject.list.add(enemy);
            }
        }
    }

    public void update(float deltaTime)
    {

    }

    public void render(float deltaTime)
    {
        update(deltaTime);
    }

    public void onCollision(GameObject otherGameObject)
    {
        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                enemy.onCollisionWith(otherGameObject);
            }
        }
    }

    public void onCollisionBy(GameObject otherGameObject)
    {
        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                otherGameObject.onCollisionWith(enemy);
            }
        }
    }

    public void renewMatrix()
    {
        dispose();
        while(enemyMatrix.size() < maxColumn)
            enemyMatrix.add((new LinkedList<Enemy>()));
    }

    public void dispose()
    {
        for (LinkedList<Enemy> linkedList : enemyMatrix) 
        {
            for (Enemy enemy : linkedList) 
            {
                enemy.dispose();
            }
        }
        enemyMatrix.clear();
    }
}
