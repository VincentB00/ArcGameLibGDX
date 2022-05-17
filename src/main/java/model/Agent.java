package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;

import model.collision.DetectionCircle;

public class Agent extends GameObject
{
    public boolean autoAction;
    public DetectionCircle detectionCircle;
    public Agent startPosition = null;

    public Agent() 
    {
        super();
        detectionCircle = new DetectionCircle(0.1f, this);
    }

    public Agent(String spritePath, String name, String tag) 
    {
        super(spritePath, name, tag);
        detectionCircle = new DetectionCircle(100f, this);
        startPosition = new Agent();
    }

    public void setStartPosition(Vector2 vector2)
    {
        this.position.set(vector2);
        this.startPosition.position.set(vector2);
    }

    @Override
    public void start() 
    {
        super.start();
        autoAction = true;
        
    }

    @Override
    public void render(Renderer renderer, float delta) 
    {
        super.render(renderer, delta);

        detectionCircle.render(renderer, delta);
    }

    @Override
    public void update(float delta) 
    {
        super.update(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.J))
        {
            this.detectionCircle.switchRenderCircle();
        }

    }

    public void moveForwardFirstDetectGameObject()
    {
        if(detectGameObject())
        {
            GameObject target = detectionCircle.detectGameObjectList.getFirst();
            this.moveForward(target);
        }
    }

    public void moveBackToStartPosition()
    {
        if(!rotateTo(startPosition))
            this.moveForward();
    }

    public boolean isAtStartPosition()
    {
        return this.startPosition.detectionCircle.onCollisionWith(this.hitBox);
    }

    public boolean detectGameObjectWithin(float angle)
    {
        if(detectGameObject())
        {
            GameObject target = detectionCircle.detectGameObjectList.getFirst();
            return isWithinView(target, angle);
        }
        
        return false;
    }

    public boolean isWithinView(GameObject target, float angle)
    {
        float rotationFromForward = this.calculateAngleFromForwardTo(target);
        return rotationFromForward < (angle / 2);
    }

    public boolean detectGameObject()
    {
        return detectionCircle.detectGameObjectList.size() >= 1;
    }

    @Override
    public void onCollisionWith(GameObject otherGameObject) 
    {
        if(this.enableTriggerCollision)
        {
            if(hitBox.overlaps(otherGameObject.hitBox))
            {
                // System.out.println(this.name + " collision with " + otherGameObject.name);
            }
        }

        if(detectionCircle.onCollisionWith(otherGameObject.hitBox))
        {
            detectionCircle.addDetectGameObject(otherGameObject);
        }
    }

    @Override
    public void dispose() 
    {
        super.dispose();

        if(startPosition != null)
            startPosition.dispose();
    }
}
