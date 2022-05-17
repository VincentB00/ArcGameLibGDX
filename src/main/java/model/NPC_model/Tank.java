package model.NPC_model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.EnumTag;
import helper.Timer;
import model.Agent;
import model.GameObject;
import model.projectile.Projectile;

public class Tank extends Agent
{
    Timer hitCD;
    Timer shootTimer;
    int type;
    
    public Tank(String name)
    {
        super("Assets/tanknsoldier/tank/tank1/tank1-export1.png", name, EnumTag.enemy.tag);
        hitCD = new Timer(3);
        this.useStearingBehavior = true;
        this.baseRotation = -90;

        Random random = new Random();
        type = random .nextInt(5);

        if(type == 0)
        {
            this.setSprite("Assets/tanknsoldier/tank/tank1/tank1-export1.png");
            this.statSystem.maxHeath = 400;
            this.statSystem.accuracyBase = 85;
            super.detectionCircle.radius = 250;
            this.statSystem.maxSpeed = 40;
            this.statSystem.accelerate = 2;
            this.shootTimer = new Timer(2);
        }
        else if(type == 1)
        {
            this.setSprite("Assets/tanknsoldier/tank/tank2/tank2-export1.png");
            this.statSystem.maxHeath = 450;
            this.statSystem.accuracyBase = 90;
            super.detectionCircle.radius = 300;
            this.statSystem.maxSpeed = 50;
            this.statSystem.accelerate = 3;
            this.shootTimer = new Timer(2.5);
        }
        else if(type == 2)
        {
            this.setSprite("Assets/tanknsoldier/tank/tank3/tank3-export1.png");
            this.statSystem.maxHeath = 500;
            this.statSystem.accuracyBase = 95;
            super.detectionCircle.radius = 350;
            this.statSystem.maxSpeed = 60;
            this.statSystem.accelerate = 4;
            this.shootTimer = new Timer(3);
        }
        else
        {
            this.setSprite("Assets/tanknsoldier/tank/tank4/tank4-export1.png");
            this.statSystem.maxHeath = 450;
            this.statSystem.accuracyBase = 100;
            super.detectionCircle.radius = 400;
            this.statSystem.maxSpeed = 70;
            this.statSystem.accelerate = 5;
            this.shootTimer = new Timer(3.5);
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
