package model.projectile;

import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.EnumTag;
import helper.Timer;
import model.GameObject;

public class Projectile extends GameObject
{
    public GameObject source;
    public float baseRotation = 0;
    public Vector2 offSetPosition;
    private Vector2 moveDirection;
    public Timer disposeTimer;
    public float radius = 40;

    public Projectile(String spritePath) 
    {
        super(spritePath);
        disposeTimer = new Timer(3);
    }

    public Projectile(String spritePath, String name) 
    {
        super(spritePath, name);
        disposeTimer = new Timer(3);
    }

    public Projectile(String spritePath, String name, String tag) 
    {
        super(spritePath, name, tag);
        disposeTimer = new Timer(3);
    }

    public Projectile(String spritePath, GameObject source) 
    {
        super(spritePath);
        disposeTimer = new Timer(3);
        this.source = source;
        calculateRotationAndDirection();
    }

    public Projectile(String spritePath, GameObject source, float baseRotation) 
    {
        super(spritePath);
        disposeTimer = new Timer(3);
        this.source = source;
        this.baseRotation = baseRotation;
        calculateRotationAndDirection();
    }

    public Projectile(String spritePath, GameObject source, Vector2 offSetPosition) 
    {
        super(spritePath);
        disposeTimer = new Timer(3);
        this.source = source;
        this.offSetPosition = offSetPosition;
        calculateRotationAndDirection();
    }

    public Projectile(String spritePath, GameObject source, float baseRotation, Vector2 offSetPosition) 
    {
        super(spritePath);
        disposeTimer = new Timer(3);
        this.source = source;
        this.baseRotation = baseRotation;
        this.offSetPosition = offSetPosition;
        calculateRotationAndDirection();
    }

    public Projectile(String spritePath, GameObject source, float baseRotation, Vector2 offSetPosition, float radius) 
    {
        super(spritePath);
        disposeTimer = new Timer(3);
        this.source = source;
        this.baseRotation = baseRotation;
        this.offSetPosition = offSetPosition;
        this.radius = radius;
        calculateRotationAndDirection();
    }


    private void calculateRotationAndDirection()
    {
        super.moveSpeed = source.statSystem.getMaxBulletSpeed();

        if(offSetPosition == null)
            offSetPosition = new Vector2();

        float sourceRotation = source.rotation;
        

        super.setRotation(baseRotation + sourceRotation + source.statSystem.getBulletSpreadAngle());


        //move direction
        moveDirection = calculateMotionVectorForward(super.rotation);

        //starting point
        // Vector2 startPosition = calculatePointOnCircle(55, super.rotation, source.getMiddlePosition());
        Vector2 startPosition = calculatePointOnCircle(radius, super.rotation, source.getMiddlePosition());
        

        super.position.set(startPosition.x + offSetPosition.x, startPosition.y + offSetPosition.y);
        // super.position.set(startPosition.x, startPosition.y);
        
        // Vector2 startOffset = this.getMiddlePosition();
        // System.out.println("offset " + startOffset + " start " + startPosition);
        
        // super.position.set(startPosition.x - (startPosition.x - startOffset.x), startPosition.y - (startPosition.y - startOffset.y));
        // super.position.set(startPosition.x - startOffset.x, startPosition.y - startOffset.y);
    }

    // private Vector2 calculateMotionVectorForward(float sourceRotation)
    // {
    //     float moveX = 0;
    //     float moveY = 0;
        
    //     //quarrant I
    //     if(sourceRotation <= 90f)
    //     {
    //         return calculateDirection(sourceRotation);
    //     }

    //     //quarrant II
    //     if(sourceRotation <= 180f)
    //     {
    //         Vector2 direction = calculateDirection(180f - sourceRotation);
    //         moveX = direction.x * -1;
    //         moveY = direction.y;
    //         return new Vector2(moveX, moveY);
    //     }

    //     //quarrant III
    //     if(sourceRotation <= 270f)
    //     {
    //         Vector2 direction = calculateDirection(270f - sourceRotation);
    //         moveX = direction.y * -1;
    //         moveY = direction.x * -1;
    //         return new Vector2(moveX, moveY);
    //     }

    //     //quarrant IV
    //     if(sourceRotation <= 360f)
    //     {
    //         Vector2 direction = calculateDirection(360f - sourceRotation);
    //         moveX = direction.x;
    //         moveY = direction.y * -1;
    //         return new Vector2(moveX, moveY);
    //     }     

        

    //     return new Vector2(moveX, moveY);
    // }

    // private Vector2 calculateDirection(float sourceRotation)
    // {
    //     float moveX = 0;
    //     float moveY = 0;
    //     if(sourceRotation < 45f)
    //     {
    //         moveX = 1f;
    //         moveY = (sourceRotation / 45);
    //     }  
    //     else
    //     {
    //         moveY = 1f;
    //         moveX = (90 - sourceRotation) / 45;
    //     }

    //     return new Vector2(moveX, moveY);
    // }

    // public Vector2 calculatePointOnCircle(float radius, float angleInDegrees, Vector2 origin)
    // {

    //     // Convert from degrees to radians via multiplication by PI/180        
    //     float x = (float)(radius * Math.cos(angleInDegrees * Math.PI / 180F)) + origin.x;
    //     float y = (float)(radius * Math.sin(angleInDegrees * Math.PI / 180F)) + origin.y;

    //     return new Vector2(x, y);
    // }

    @Override
    public void update(float delta) 
    {
        // TODO Auto-generated method stub

        if(disposeTimer.isTimeUp() || this.isOutOfScreen() || super.isColliding())
        {
            dispose();
            return;
        }

        super.update(delta);
        super.addForce(moveDirection.x, moveDirection.y);
    }

    @Override
    public void dispose() 
    {
        // TODO Auto-generated method stub
        super.dispose();
        EnumRenderObject.renderProjectile.list.remove(this);
    }

    @Override
    public void onCollisionWith(GameObject otherGameObject) 
    {
        if(enableTriggerCollision)
        {
            if(this.hitBox.overlaps(otherGameObject.hitBox) && otherGameObject != source && source.tag.compareTo(otherGameObject.tag) != 0)
            {
                if(otherGameObject.tag.compareTo(EnumTag.player.tag) == 0)
                {
                    // System.out.println("Projectile hit player");
                    otherGameObject.statSystem.takeDamges(source.statSystem.getMaxAttackDamges());
                    this.dispose();
                }

                if(otherGameObject.tag.compareTo(EnumTag.enemy.tag) == 0 && source.tag.compareTo(EnumTag.enemy.tag) != 0)
                {
                    otherGameObject.statSystem.takeDamges(source.statSystem.getMaxAttackDamges());
                    // System.out.println("Projectile hit enemy " + otherGameObject.statSystem.currentHeath + " from " + this.statSystem.attackDamges);
                    this.dispose();
                }
            }
        }
    }
}
