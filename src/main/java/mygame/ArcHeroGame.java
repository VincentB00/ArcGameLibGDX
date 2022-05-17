package mygame;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.EnumTag;
import helper.Timer;
import model.Agent;
import model.GameObject;
import model.Map;
import model.Renderer;
import model.NPC_model.Drone;
import model.NPC_model.Objective;
import model.NPC_model.PinkShooter;
import model.NPC_model.Tank;
import model.UI.Text;
import model.player_model.ArcShooter;
import model.player_model.BlueShipPlayer;
import model.projectile.Projectile;

public class ArcHeroGame extends Map
{
    Timer totalAttackTimer;
    Text heathBar;
    Text moveSpeedBar;
    Text attackDamgesBar;
    Text totalNumberOfPointBar;
    
    LinkedList<GameObject> objectiveList;
    LinkedList<Agent> enemyList;
    ArcShooter player;

    int maxNumberOfEnemy;

    Vector2 playerSpawnPoint;

    double totalPoint;

    int numberOfKillEnemy;

    public ArcHeroGame(Renderer renderer)
    {
        super(renderer);
    }

    @Override
    public void start() 
    {
        super.start();

        totalAttackTimer = new Timer(30);

        totalPoint = 0;

        enemyList = new LinkedList<>();
        objectiveList = new LinkedList<>();
        
        maxNumberOfEnemy = 1;

        spawnPlayer();

        newStage(null);

        //BAR
        heathBar = new Text(String.format("player heath: %s", player.statSystem.currentHeath));
        heathBar.position.set(45, 750);
        heathBar.font.setColor(Color.RED);
        heathBar.font.getData().setScale(1.2f);

        moveSpeedBar = new Text(String.format("player moving speed: %s", player.moveSpeed));
        moveSpeedBar.position.set(45, 720);
        moveSpeedBar.font.setColor(Color.RED);
        moveSpeedBar.font.getData().setScale(1.2f);

        attackDamgesBar = new Text(String.format("player attack damges: %s", player.statSystem.getMaxAttackDamges()));
        attackDamgesBar.position.set(400, 750);
        attackDamgesBar.font.setColor(Color.RED);
        attackDamgesBar.font.getData().setScale(1.2f);

        totalNumberOfPointBar = new Text(String.format("player total Point: %s", this.totalPoint));
        totalNumberOfPointBar.position.set(1000, 720);
        totalNumberOfPointBar.font.setColor(Color.RED);
        totalNumberOfPointBar.font.getData().setScale(1.2f);
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

        checkPoint();

        //UI bar
        heathBar.text = String.format("player heath: %s", player.statSystem.currentHeath);
        moveSpeedBar.text = String.format("player move speed: %s", player.moveSpeed);
        attackDamgesBar.text = String.format("player attack damges: %s", player.statSystem.getMaxAttackDamges());
        totalNumberOfPointBar.text = String.format("player total Point: %s", this.totalPoint);

        player.lookAt(super.mousePoint);

        LinkedList<Integer> removeList = new LinkedList<>();

        LinkedList<GameObject> gameObjectsList = EnumRenderObject.renderProjectile.list;
        for(int count = 0; count < gameObjectsList.size(); count++)
        {
            //project tile colly with ...
            gameObjectsList.get(count).onCollisionWith(player);

            for(int count1 = 0; count1 < enemyList.size(); count1++)
            {
                if(count >= gameObjectsList.size())
                    continue;

                Agent agent = enemyList.get(count1);

                if(agent != null)
                    gameObjectsList.get(count).onCollisionWith(agent);
                else
                    removeList.add(count1);
            }   

            int removeSize = removeList.size();
            for(int count1 = 0; count1 < removeSize; count1 ++)
            {
                enemyList.remove(removeList.pollLast());
            }
                
        }


        if(totalAttackTimer.isTimeUp())
        {
            for(Agent agent : this.enemyList)
                agent.detectionCircle.radius *= 2;
        }
    }

    private void spawnEnemyAtRandom()
    {
        Random random = new Random();
        TiledMap tileMap = renderer.tiledMap;
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        LinkedList<Vector2> positionList = new LinkedList<>();

        for(int x = 0; x < 60; x++)
        {
            for(int y = 0; y < 25; y++)
            {
                Cell cell = layer.getCell(x, y);
                if(cell != null && cell.getTile().getProperties() != null)
                {
                    if(cell.getTile().getProperties().containsKey("random spawn"))
                    {
                        int realX = x * 32;
                        int realY = y * 32;

                        positionList.add(new Vector2(realX, realY));
                    }
                }
            }
        }

        for(int count = 0; count < this.maxNumberOfEnemy; count ++)
        {
            int num = random.nextInt(positionList.size());

            PinkShooter pinkShooter = new PinkShooter("random PinkShooter" + count);
            pinkShooter.setPosition(positionList.get(num).x, positionList.get(num).y);
            enemyList.add(pinkShooter);
        }
    }

    private void spawnDefender()
    {
        Random random = new Random();
        TiledMap tileMap = renderer.tiledMap;
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        LinkedList<Vector2> positionList = new LinkedList<>();

        for(int x = 0; x < 60; x++)
        {
            for(int y = 0; y < 25; y++)
            {
                Cell cell = layer.getCell(x, y);
                if(cell != null && cell.getTile().getProperties() != null)
                {
                    if(cell.getTile().getProperties().containsKey("enemy spawn"))
                    {
                        int realX = x * 32;
                        int realY = y * 32;

                        positionList.add(new Vector2(realX, realY));
                    }
                }
            }
        }


        for(int count = 0; count < this.maxNumberOfEnemy; count ++)
        {
            int num = random.nextInt(positionList.size());

            PinkShooter pinkShooter = new PinkShooter("defender PinkShooter" + count);
            pinkShooter.setPosition(positionList.get(num).x, positionList.get(num).y);
            enemyList.add(pinkShooter);
        }
    }
    
    private void spawnCar()
    {
        Random random = new Random();
        TiledMap tileMap = renderer.tiledMap;
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        LinkedList<Vector2> positionList = new LinkedList<>();

        for(int x = 0; x < 60; x++)
        {
            for(int y = 0; y < 25; y++)
            {
                Cell cell = layer.getCell(x, y);
                if(cell != null && cell.getTile().getProperties() != null)
                {
                    if(cell.getTile().getProperties().containsKey("car spawn"))
                    {
                        int realX = x * 32;
                        int realY = y * 32;

                        positionList.add(new Vector2(realX, realY));
                    }
                }
            }
        }


        for(int count = 0; count < this.maxNumberOfEnemy; count ++)
        {
            int num = random.nextInt(positionList.size());

            Tank tank = new Tank("Tank " + count);
            tank.setPosition(positionList.get(num).x, positionList.get(num).y);
            enemyList.add(tank);
        }
    }

    private void spawnObjective()
    {
        TiledMap tileMap = renderer.tiledMap;
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        for(int x = 0; x < 60; x++)
        {
            for(int y = 0; y < 25; y++)
            {
                Cell cell = layer.getCell(x, y);
                if(cell != null && cell.getTile().getProperties() != null)
                {
                    if(cell.getTile().getProperties().containsKey("goal"))
                    {
                        int realX = x * 32;
                        int realY = y * 32;
                        Objective objective = new Objective(realX, realY)
                        {
                            @Override
                            public void onCollisionWith(GameObject otherGameObject) 
                            {
                                if(hitBox.overlaps(otherGameObject.hitBox) && otherGameObject.tag.compareTo(EnumTag.player.tag) == 0 && this.activateTimer.isTimeUp())
                                {
                                    newStage(this);
                                    this.activateTimer.resetTimer();
                                }
                            }
                        };

                        objectiveList.add(objective);
                    }
                }
            }
        }
    }   

    private void spawnDrone()
    {
        Random random = new Random();
        TiledMap tileMap = renderer.tiledMap;
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        LinkedList<Vector2> positionList = new LinkedList<>();

        for(int x = 0; x < 60; x++)
        {
            for(int y = 0; y < 25; y++)
            {
                Cell cell = layer.getCell(x, y);
                if(cell != null && cell.getTile().getProperties() != null)
                {
                    if(cell.getTile().getProperties().containsKey("random spawn"))
                    {
                        int realX = x * 32;
                        int realY = y * 32;

                        positionList.add(new Vector2(realX, realY));
                    }
                }
            }
        }

        for(int count = 0; count < this.maxNumberOfEnemy; count ++)
        {
            int num = random.nextInt(positionList.size());

            Drone drone = new Drone("drone " + count);
            drone.setPosition(positionList.get(num).x, positionList.get(num).y);
            enemyList.add(drone);
        }
    }

    private void spawnPlayer()
    {
        TiledMap tileMap = renderer.tiledMap;
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        for(int x = 0; x < 60; x++)
        {
            for(int y = 0; y < 25; y++)
            {
                Cell cell = layer.getCell(x, y);
                if(cell != null && cell.getTile().getProperties() != null)
                {
                    if(cell.getTile().getProperties().containsKey("spawn"))
                    {
                        //player
                        player = new ArcShooter();
                        player.setPosition(x * 32, y * 32);
                        player.baseRotation = 90;

                        playerSpawnPoint = new Vector2(x * 32, y * 32);
                        break;
                    }
                }
            }
        }
    }

    private void newStage(Objective objective)
    {
        if(objective != null)
        {
            maxNumberOfEnemy++;

            if(objective.type == 0)
                player.statSystem.attackDamgesModifier += 10;
            else if(objective.type == 1)
                player.moveSpeed += 10;
            else
                player.statSystem.maxHeath += 30;
        }

        numberOfKillEnemy = 0;
        
        for(Agent agent : this.enemyList)
            agent.dispose();

        this.enemyList.clear();

        for(GameObject gameObject : this.objectiveList)
            gameObject.dispose();

        this.objectiveList.clear();

        player.statSystem.fullyHeal();
        player.setPosition(this.playerSpawnPoint.x, this.playerSpawnPoint.y);
        
        spawnDefender();

        spawnEnemyAtRandom();

        spawnCar();

        spawnDrone();

        spawnObjective();

        totalAttackTimer.resetTimer();
    }

    private void checkPoint()
    {
        int totalNumberOfEnemy = maxNumberOfEnemy * 4;

        int numberOfRemaingEnemy = 0;

        for(Agent agent : this.enemyList)
        {
            if(!agent.statSystem.isDead())
            {
                numberOfRemaingEnemy++;
            }
        }

        if(this.numberOfKillEnemy >= (totalNumberOfEnemy - numberOfRemaingEnemy))
            return;
        else
        {
            this.numberOfKillEnemy = totalNumberOfEnemy - numberOfRemaingEnemy;
            this.totalPoint ++;
        }

    }
}
