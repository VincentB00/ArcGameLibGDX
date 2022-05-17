package model.player_model;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;

import helper.EnumTag;
import model.matrix.WeightTile;

public class AutoSpaceShip extends Player
{
    public boolean haveDestination = false;
    public Vector2 destination;
    public LinkedList<WeightTile> destinationList;

    public AutoSpaceShip()
    {
        super("Assets/svg_spaceships/images/svg_spaceships_21.png", "player", EnumTag.player.tag);
        controllable = false;
        position.x = 0;
        statSystem.attackDamgesBase = 0;
        this.baseRotation = -90;
    }
    public AutoSpaceShip(String spritePath, String name, String tag) 
    {
        super(spritePath, name, tag);
        controllable = false;
        position.x = 0;
        statSystem.attackDamgesBase = 0;
    }

    public void setDestination(LinkedList<WeightTile> destinationList)
    {
        if(destinationList.size() < 2)
            return;

        this.destinationList = destinationList;
        
        //check if it is currently in a teleport tile
        if(destinationList.getFirst().isTeleportTile && destinationList.get(1).isTeleportTile)
        {
            destinationList.removeFirst();
            this.position = destinationList.getFirst().getMiddlePosition();
        }

        //check if it is the last tile
        if(destinationList.size() <= 1)
            return;

        destinationList.removeFirst();
        destination = destinationList.getFirst().getMiddlePosition();

        haveDestination = true;
    }

    @Override
    public void update(float delta) 
    {
        super.update(delta);

        if(haveDestination)
        {
            

            if(destinationList.getFirst().hitBox.overlaps(this.hitBox))
            {
                //check teleport
                if(destinationList.getFirst().isTeleportTile && destinationList.size() > 2 && destinationList.get(1).isTeleportTile)
                {
                    destinationList.removeFirst();
                    this.position = destinationList.getFirst().getMiddlePosition();
                }
                else
                    this.position = destinationList.getFirst().getMiddlePosition();

                destinationList.removeFirst();

                if(destinationList.size() <= 0)
                {
                    haveDestination = false;
                    return;
                }
                
                destination = destinationList.getFirst().getMiddlePosition();

                
            }

            Float temp = baseRotation;
            baseRotation = 0;
            setRotation();
            Vector2 moveVector2 = this.calculateMotionVectorForward(this.rotation);
            super.addForce(moveVector2.x, moveVector2.y);
            baseRotation = temp;
            setRotation();
        }

    }
    
    private void setRotation()
    {
        if(destination.y > this.position.y)
        {
            this.rotation = baseRotation + 90;
        }

        if(destination.y < this.position.y)
        {
            this.rotation = baseRotation + 270;
        }

        if(destination.x > this.position.x)
        {
            this.rotation = baseRotation + 0;
        }

        if(destination.x < this.position.x)
        {
            this.rotation = baseRotation + 180;
        }
    }
}
