package model.NPC_model;

import helper.EnumTag;
import helper.Timer;
import model.Agent;
import model.GameObject;

public class BigRedSpaceShip extends Agent
{
    public BigRedSpaceShip(String name) 
    {
        super("Assets/svg_spaceships/images/Fix/svg_spaceships_4.png", name, EnumTag.enemy.tag);
        this.detectionCircle.setRadius(200f);
        this.useStearingBehavior = false;
    }
    
    @Override
    public void update(float delta) 
    {
        super.update(delta);
        

        if(this.detectGameObjectWithin(270))
        {
            this.moveForwardFirstDetectGameObject();
        }
        else if(!this.isAtStartPosition())
        {
            this.moveBackToStartPosition();
        }
        else 
        {
            this.addRotation(0.2f);
        }
    }
    
    @Override
    public void onCollisionWith(GameObject otherGameObject) 
    {
        super.onCollisionWith(otherGameObject);

        if(this.enableTriggerCollision)
        {
            if(hitBox.overlaps(otherGameObject.hitBox) && otherGameObject.tag.compareTo(EnumTag.player.tag) == 0)
            {
                otherGameObject.statSystem.takeDamges(10000);
            }
        }
    }
}
