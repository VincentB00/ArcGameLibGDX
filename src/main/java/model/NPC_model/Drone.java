package model.NPC_model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.EnumTag;
import helper.Timer;
import model.Agent;
import model.GameObject;
import model.projectile.Projectile;

public class Drone extends Agent
{
    Timer hitCD;
    int type;
    
    public Drone(String name)
    {
        super("Assets/tanknsoldier/drone/drone1/drone1-export1.png", name, EnumTag.enemy.tag);
        hitCD = new Timer(5);
        this.useStearingBehavior = true;
        this.baseRotation = 90;
        this.enableCollision = false;

        Random random = new Random();
        type = random .nextInt(4);

        if(type == 0)
        {
            this.setSprite("Assets/tanknsoldier/drone/drone1/drone1-export1.png");
            this.statSystem.maxHeath = 200;
            super.detectionCircle.radius = 350;
            this.statSystem.maxSpeed = 90;
            this.statSystem.accelerate = 7;
        }
        else if(type == 1)
        {
            this.setSprite("Assets/tanknsoldier/drone/drone2/drone2-export1.png");
            this.statSystem.maxHeath = 100;
            super.detectionCircle.radius = 400;
            this.statSystem.maxSpeed = 80;
            this.statSystem.accelerate = 8;
        }
        else
        {
            this.setSprite("Assets/tanknsoldier/drone/drone3/drone3-export1.png");
            this.statSystem.maxHeath = 300;
            super.detectionCircle.radius = 450;
            this.statSystem.maxSpeed = 110;
            this.statSystem.accelerate = 9;
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
            this.lookAt(player);

            this.moveForward();
        }
        else
        {
            this.addRotation(0.2f);
        }

    }

    public void shoot()
    {
        Projectile projectile = new Projectile("Assets/bullet/tile087.png", this, 0, new Vector2(-9, -8), 37);
        projectile.disposeTimer.resetTimer(6);
        
        EnumRenderObject.renderProjectile.list.addLast(projectile);
    }

    @Override
    public void onCollisionWith(GameObject otherGameObject) 
    {
        super.onCollisionWith(otherGameObject);

        if(this.enableTriggerCollision)
        {
            if(hitBox.overlaps(otherGameObject.hitBox) && otherGameObject.tag.compareTo(EnumTag.player.tag) == 0)
            {
                if(hitCD.isTimeUp())
                {
                    otherGameObject.statSystem.takeDamges(this.statSystem.getMaxAttackDamges());
                    hitCD.resetTimer();
                }
            }
        }
    }
}
