package mygame;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.EnumTag;
import helper.Timer;
import model.GameObject;
import model.Renderer;
import model.NPC_model.Enemy;
import model.UI.Text;
import model.matrix.EnemyMatrix;
import model.player_model.Player;

public class SpaceShooterGame 
{
    // LinkedList<Enemy> enemyList;
    EnemyMatrix enemyMatrix;
    public Player player;
    public Renderer renderer;
    Text heathBar;
    private int maxEnemy = 60;
    private int currentNumEnemy = 60;
    private Timer shootTimer;
    private double shootCD = 2;
    private Random random;
    private LinkedList<GameObject> barriers;

    public SpaceShooterGame(Renderer renderer)
    {
        setup();
        this.renderer = renderer;
    }

    public void setup()
    {
        shootTimer = new Timer(shootCD);
        random = new Random();
        barriers = new LinkedList<GameObject>();

        // enemyMatrix = new EnemyMatrix(new Vector2(50, 60), new Vector2(90, 220), 5, 12);
        enemyMatrix = new EnemyMatrix(new Vector2(50, -60), new Vector2(90, 490), 5, 12);
        renewMatrix();

        //player
        player = new Player("Assets/svg_spaceships/images/svg_spaceships_21.png", "player", EnumTag.player.tag);
        player.position.x = 300;
        player.statSystem.attackDamgesBase = 100;
        EnumRenderObject.renderObject.list.addLast(player);

        //heath bar
        heathBar = new Text(String.format("player heath: %s", player.statSystem.currentHeath));
        heathBar.position.set(50, 570);
        heathBar.font.setColor(Color.RED);
        heathBar.font.getData().setScale(2);
        EnumRenderObject.renderObject.list.add(heathBar);

        //barrier
        for(int count = 70; count <= 800; count+=200)
        {
            GameObject barrier = new GameObject("Assets/Water_Effect/05/Fix/Water__05.png");
            barrier.position.set(count, 100);
            barriers.add(barrier);
        }
    }

    public void update(float delta)
    {
        if(player.statSystem.isDead())
        {
            player.dispose();
            player = new Player("Assets/svg_spaceships/images/svg_spaceships_21.png", "player", EnumTag.player.tag);
            player.position.x = 300;
            player.statSystem.attackDamgesBase = 100;
            EnumRenderObject.renderObject.list.addLast(player);

            enemyMatrix.dispose();
            currentNumEnemy = 1;
            renewMatrix();
        }


        //player collision with enemy
        enemyMatrix.onCollisionBy(player);
        
        //onllision by project tile on enemy and player
        LinkedList<GameObject> projectileList = EnumRenderObject.renderProjectile.list;
        try
        {
            for(int count = 0; count < projectileList.size(); count++)
            {
                if(projectileList.isEmpty())
                    break;

                enemyMatrix.onCollisionBy(projectileList.get(count));
                
                projectileList.get(count).onCollisionWith(player);
                
                for (GameObject gameObject : barriers) 
                {
                    projectileList.get(count).onCollisionWith(gameObject);
                }
            }
        }
        catch(Exception ex)
        {
            // ex.printStackTrace();
        }

        //heath bar
        heathBar.text = String.format("player heath: %s", player.statSystem.currentHeath);

        int numberOfNotDeadEnemy = enemyMatrix.getNumberOfNotDeadEnemy();
        //game calculation
        if(numberOfNotDeadEnemy <= 0 && currentNumEnemy == maxEnemy)
        {
            Gdx.app.exit();
        }
        else if(numberOfNotDeadEnemy <= 0)
        {
            currentNumEnemy++;
            renewMatrix();

        }
        else if(shootTimer.isTimeUp())
        {
            Enemy tempEnemy = enemyMatrix.getRandomNonDeadEnemy();

            int ranNumEnemy = random.nextInt(numberOfNotDeadEnemy) + 1;

            // if(enemyMatrix.getNumberOfNotDeadEnemy() == 1)
            // {
            //     shootTimer.resetTimer(0.5);
            //     tempEnemy.moveSpeed += 30;
            //     tempEnemy.destinationOffset *= 2;

            //     if(shootTimer.isTimeUp())
            //     {
            //         tempEnemy.shoot();
            //         shootTimer.resetTimer();
            //     }
            // }
            // else 

            for(int count = 0; count < numberOfNotDeadEnemy; count++)
            {
                tempEnemy = enemyMatrix.getRandomNonDeadEnemy();

                tempEnemy.shoot();
                    
            }

            shootTimer.resetTimer();
        }
    }

    

    public void render(float delta)
    {
        update(delta);
    }

    public void dispose()
    {
        enemyMatrix.dispose();
        player.dispose();
    }

    private void renewMatrix()
    {
        shootTimer.resetTimer(shootCD);
        enemyMatrix.renewMatrix();
        
        for(int count = 0; count < currentNumEnemy; count ++)
        {
            Enemy enemy = new Enemy("Assets/svg_spaceships/images/Fix/svg_spaceships_4_2.png", "enemy " + count, EnumTag.enemy.tag);
            enemy.addRotation(180);
            enemy.moveSpeed = 10;
            enemy.statSystem.attackDamgesBase = 10;
            enemy.autoShoot = false;
            enemyMatrix.addEnemy(enemy);
        }

        enemyMatrix.resetAllPosition();
        enemyMatrix.registerRenderAllObject();
    }
}
