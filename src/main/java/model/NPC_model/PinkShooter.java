package model.NPC_model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.EnumTag;
import helper.Timer;
import model.Agent;
import model.GameObject;
import model.projectile.Projectile;

public class PinkShooter extends Agent
{
    Timer shootTimer;
    int type;
    
    public PinkShooter(String name)
    {
        super("Assets/tanknsoldier/enemy/enemy 2/idle/enemy2idle1.png", name, EnumTag.enemy.tag);
        this.baseRotation = 90;
        Random random = new Random();
        int type = random .nextInt(2);

        if(type == 1)
        {
            this.setSprite("Assets/tanknsoldier/enemy/enemy 3/idle/enemy3idle1.png");
            this.statSystem.maxHeath = 150;
            this.statSystem.accuracyBase = 70;
            super.detectionCircle.radius = 300;
            this.moveSpeed -= this.moveSpeed / 2;
            this.shootTimer = new Timer(0.4);
        }
        else
        {
            this.setSprite("Assets/tanknsoldier/enemy/enemy 2/idle/enemy2idle1.png");
            this.statSystem.maxHeath = 200;
            this.statSystem.accuracyBase = 60;
            super.detectionCircle.radius = 250;
            this.moveSpeed -= this.moveSpeed / 2.3;
            this.shootTimer = new Timer(0.6);
        }
        
        this.statSystem.fullyHeal();
    }

    @Override
    public void update(float delta) 
    {
        super.update(delta);

        GameObject player = null;

        for(GameObject gameObject : super.detectionCircle.detectGameObjectList)
        {
            if(gameObject.tag.compareTo(EnumTag.player.tag) == 0)
                player = gameObject;
        }

        // System.out.println(detectGameObject());

        //if detect player && player is within view
        if(player != null && isWithinView(player, 270))
        {
            double distance = this.calculateDistance(this.position, player.position);

            this.lookAt(player);

            if(distance > 200)
                this.moveForward();

            if(shootTimer.isTimeUp())
            {
                shoot();
                shootTimer.resetTimer();
            }
        }
        else
        {
            this.addRotation(0.2f);
        }

    }

    public void shoot()
    {
        Projectile projectile = new Projectile("Assets/bullet/tile068.png", this, 0, new Vector2(-9, -8), 37);
        projectile.disposeTimer.resetTimer(6);
        
        EnumRenderObject.renderProjectile.list.addLast(projectile);
    }
}
