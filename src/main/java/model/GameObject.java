package model;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import helper.EnumConstants;
import helper.EnumRenderObject;
import helper.EnumTag;
import model.collision.HitBox;

public class GameObject extends Sprite
{
    public String name = "";

    public String tag = "";

    public Vector2 position;
    public float rotation = 0;

    public float moveSpeed = 60 * 2;
    public float gravity = 60 * 1.8f;

    private boolean allowOutOfScreen = false;
    private float screenW = EnumConstants.screenW.ratio;
    private float screenH = EnumConstants.screenH.ratio;

    private boolean flipX = false;
    private boolean flipY = false;

    public boolean enableTriggerCollision = true;
    public boolean enableCollision = true;
    public HitBox hitBox;

    private float delta;
    private boolean isColliding;
    

    //stat system
    public StatSystem statSystem;

    public LinkedList<GameObject> childs;

    private boolean renderSprite = true;

    public float baseRotation = 0;

    //----------------stearing behavior---------------------
    public boolean useStearingBehavior = false;
    private boolean accelerating = false;

    public GameObject()
    {
        start();
        enableTriggerCollision = false;
        enableCollision = false;
        renderSprite = false;
        allowOutOfScreen = true;
        enableTriggerCollision = false;
        this.tag = EnumTag.helper.tag;
        this.statSystem.imortal = true;
    }

    public GameObject(String spritePath)
    {
        super(new Sprite(new Texture(spritePath)));
        start();
    }

    public GameObject(String spritePath, String name)
    {
        super(new Sprite(new Texture(spritePath)));
        this.name = name;
        start();
    }

    public GameObject(String spritePath, String name, String tag)
    {
        super(new Sprite(new Texture(spritePath)));
        this.name = name;
        this.tag = tag;
        start();
    }

    public void setSprite(String spritePath)
    {
        super.set(new Sprite(new Texture(spritePath)));
    }

    public void start()
    {
        statSystem = new StatSystem(this);
        position = new Vector2(0, 0);
        hitBox = new HitBox(this);
        childs = new LinkedList<GameObject>();
        EnumRenderObject.renderObject.list.add(this);
    }

    @Override
    public void setPosition(float x, float y) 
    {
        position.set(x, y);
    }

    public void render(Renderer renderer, float delta) 
    {
        this.delta = delta;

        hitBox.render(renderer, delta);

        checkCollition(renderer);

        update(delta);

        // batch.draw(this, position.x, position.y);
        
        if(renderSprite)
            super.draw(renderer.spriteBatch);

        
    }

    public void update(float delta)
    {
        if(useStearingBehavior)
        {
            if(accelerating)
                accelerating = false;
            else
            {
                statSystem.deccelerate();
                moveForward((float)statSystem.moveSpeed);
            }
        }

        
        checkOutOfScreen();

        super.setX(position.x);
        super.setY(position.y);

        super.setRotation(this.getRotation());

        if(statSystem.isDead())
            onDead();

        if(Gdx.input.isKeyJustPressed(Input.Keys.H))
        {
            hitBox.switchShowHitBox();
        }
    }

    public void onCollisionWith(GameObject otherGameObject)
    {
        //nothing happen
        
    }

    public void onCollisionBy(GameObject otherGameObject)
    {
        //nothing happen
        
    }

    public void onDead()
    {
        enableTriggerCollision = false;
        dispose();
        //play effect ...
        
    }

    public void dispose()
    {
        try
        {
            EnumRenderObject.renderObject.list.remove(this);
            hitBox.dispose();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void revide()
    {
        statSystem.fullyHeal();

        //play effect ...
    }

    //------------------------------------------------------------------------------------------------

    private void checkCollition(Renderer renderer)
    {
        TiledMap tileMap = renderer.tiledMap;
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        isColliding = false;

        float newX = position.x;
        float newY = position.y;
        float oldX = super.getX();
        float oldY = super.getY();

        if(enableCollision)
        {
            if(!isTiledCollition(layer, oldX, oldY))
            {
                isColliding = isTiledCollition(layer, newX, newY);
                if(isColliding)
                    this.setPosition(oldX, oldY);
            }
        }
    }

    private boolean isTiledCollition(TiledMapTileLayer layer, float x, float y)
    {
        float tileWidth = layer.getTileWidth();
        float tileHeight = layer.getTileHeight();
        boolean collision = false;

        //top left
        Cell cell = layer.getCell((int)(x / tileWidth), (int)((y + super.getHeight()) / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        if(collision)
            return collision;

        //middle left
        cell = layer.getCell((int)(x / tileWidth), (int)((y + super.getHeight() / 2) / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        if(collision)
            return collision;

        //bottom left
        cell = layer.getCell((int)(x / tileWidth), (int)(y / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        if(collision)
            return collision;

        //top right
        cell = layer.getCell((int)((x + getWidth()) / tileWidth), (int)((y + getHeight()) / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        if(collision)
            return collision;
        
        //middle right
        cell = layer.getCell((int)((x + getWidth()) / tileWidth), (int)((y + getWidth() / 2) / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        if(collision)
            return collision;

        //bottom right
        cell = layer.getCell((int)((x + getWidth()) / tileWidth), (int)(y / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        //top middle
        cell = layer.getCell((int)((x + getWidth() / 2) / tileWidth), (int)((y + getHeight()) / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        if(collision)
            return collision;

        //bottom middle
        cell = layer.getCell((int)((x + getWidth() / 2) / tileWidth), (int)(y / tileHeight));
        if(cell != null && cell.getTile().getProperties() != null)
            collision |= cell.getTile().getProperties().containsKey("blocked");

        return collision;
    }

    public boolean isOutOfScreen()
    {
        if(position.x > screenW)
            return true;

        if(position.x < 0)
            return true;

        if(position.y > screenH)
            return true;

        if(position.y < 0)
            return true;

        //second half
        float x = position.x + this.getWidth();
        float y = position.y + this.getHeight();
        
        if(x > screenW)
            return true;

        if(y > screenH)
            return true;

        return false;
    }

    public void checkOutOfScreen()
    {
        if(!allowOutOfScreen)
        {
            if(position.x > screenW)
                position.x = screenW;

            if(position.x < 0)
                position.x = 0;

            if(position.y > screenH)
                position.y = screenH;

            if(position.y < 0)
                position.y = 0;

            
            //second half
            float x = position.x + this.getWidth();
            float y = position.y + this.getHeight();
            
            if(x > screenW)
                position.x = screenW - this.getWidth();

            if(y > screenH)
                position.y = screenH - this.getHeight();
            
        }
    }
    

    public Vector2 getMiddlePosition()
    {
        float X = position.x;
        float Y = position.y;

        float middleX = (this.getWidth() / 2) + X;
        float middleY = (this.getHeight() / 2) + Y;

        return new Vector2(middleX, middleY);
    }

    

    public void addMoveSpeed(float speed)
    {
        moveSpeed += speed;
    }

    public void addForce(float x, float y)
    {
        float finalMoveSpeed = 0;

        if(useStearingBehavior)
        {
            finalMoveSpeed = (float) (statSystem.moveSpeed);
            accelerating = true;
        }
        else
            finalMoveSpeed = (float) (moveSpeed + statSystem.moveSpeedModifier);
        
        position.add(x * delta * finalMoveSpeed, y * delta * finalMoveSpeed);
    }

    public void addForce(float x, float y, float finalMoveSpeed)
    {
        position.add(x * delta * finalMoveSpeed, y * delta * finalMoveSpeed);
    }

    public void addRotation(float rotation)
    {
        this.rotation = ((this.rotation + rotation) + 360f) % 360f;
    }

    public float getRotation()
    {
        return this.rotation + this.baseRotation;
    }

    public void flipX()
    {
        flipX = !flipX;
        super.setFlip(flipX, flipY);
    }

    public void flipY()
    {
        flipY = !flipY;
        super.setFlip(flipX, flipY);
    }

    public void setRotation(float rotation)
    {
        this.rotation = (rotation + 360f) % 360f;
    }
    
    

    public boolean isWithinObject(int x, int y)
    {
        // System.out.println(x + " & " + y);
        // System.out.println(position.x + " | " + position.y);

        if(x < position.x)
            return false;

        if(y < position.y)
            return false;

        if(x > position.x + this.getWidth())
            return false;

        if(y > position.y + this.getHeight())
            return false;

        return true;
    }

    public boolean isWithinObject(Vector2 vector2)
    {
        return isWithinObject((int)vector2.x, (int)vector2.y);
    }

    public boolean isClickByRightMouse()
    {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector2 mouseV = new Vector2(x, EnumConstants.screenH.ratio - y);
        return this.isWithinObject(mouseV);
    }

    public boolean isColliding()
    {
        return isColliding;
    }

    //--------------------------------------------------------------------------------------------------------------------

    public void breaking()
    {
        statSystem.breaking();
    }

    public void moveForward(GameObject target)
    {
        lookAt(target);
        moveForward();
    }

    public void moveForward()
    {
        Vector2 moveVector = calculateMotionVectorForward(this.rotation);
        statSystem.accelerate();
        this.addForce(moveVector.x, moveVector.y);
    }

    public void moveBackward()
    {
        Vector2 moveVector = calculateMotionVectorForward(this.rotation);
        statSystem.backcelerate();
        this.addForce(moveVector.x, moveVector.y);
    }

    public void moveForward(float finalMoveSpeed)
    {
        Vector2 moveVector = calculateMotionVectorForward(this.rotation);
        this.addForce(moveVector.x, moveVector.y, finalMoveSpeed);
    }

    

    public void lookAt(GameObject target)
    {
        if(useStearingBehavior)
            rotateTo(target);
        else
            this.rotation = (float) calculateLookAtAngle(this, target);
    }

    public boolean rotateTo(GameObject target)
    {
        double lookAtAngle = calculateLookAtAngle(this, target);
        double currentAngle = this.rotation;
        double rotateSpeed = statSystem.rotationSpeed;
        double rotateAngle;

        if(Math.abs(currentAngle - lookAtAngle) < 0.2)
        {
            this.setRotation((float)lookAtAngle);
            return false;
        }

        double leftWeight;
        double rightWeight;

        if(lookAtAngle > currentAngle)
        {
            rightWeight = currentAngle + (360 - lookAtAngle);
            leftWeight = 360 - rightWeight;
        }
        else
        {
            rightWeight = currentAngle - lookAtAngle;
            leftWeight = 360 - rightWeight;
        }

        if(leftWeight > rightWeight)
            rotateAngle = (currentAngle - (rotateSpeed * this.delta));
        else
            rotateAngle = (currentAngle + (rotateSpeed * this.delta));
        
        this.setRotation((float)rotateAngle);

        return true;
        
        // System.out.println("current angle: " + currentAngle +  " target angle: " + lookAtAngle + " left weight: " + leftWeight + " right weight: " + rightWeight + " rotate angle: " + rotateAngle);
    }

    public double calculateLookAtAngle(GameObject looker, GameObject target)
    {
        Vector2 targetMiddle = target.getMiddlePosition();
        double targetX = targetMiddle.x;
        double targetY = targetMiddle.y;

        Vector2 selfMiddle = looker.getMiddlePosition();
        double selfX = selfMiddle.x;
        double selfY = selfMiddle.y;

        targetX -= selfX;
        targetY -= selfY;

        Vector2D baseVector = new Vector2D(1, 0);
        Vector2D targetVector = new Vector2D(targetX, targetY);

        double rotationT;

        rotationT = Vector2D.angle(baseVector, targetVector);

        rotationT = Math.toDegrees(rotationT);

        rotationT = (rotationT + 360f) % 360f;

        if(targetY < 0)
            rotationT = 360 - rotationT;

        return rotationT;
    }

    public float calculateAngleFromForwardTo(GameObject target)
    {
        Vector2 targetMiddle = target.getMiddlePosition();
        double targetX = targetMiddle.x;
        double targetY = targetMiddle.y;

        Vector2 selfMiddle = this.getMiddlePosition();
        double selfX = selfMiddle.x;
        double selfY = selfMiddle.y;

        targetX -= selfX;
        targetY -= selfY;

        Vector2 basePoint = calculatePointOnCircle(10, this.rotation, new Vector2(0, 0));
        Vector2D baseVector = new Vector2D(basePoint.x, basePoint.y);
        Vector2D targetVector = new Vector2D(targetX, targetY);

        double rotationT;

        rotationT = Vector2D.angle(baseVector, targetVector);

        rotationT = Math.toDegrees(rotationT);

        rotationT = (rotationT + 360) % 360;

        return (float) rotationT;
    }

    public Vector2 calculatePointOnCircle(float radius, float angleInDegrees, Vector2 origin)
    {

        // Convert from degrees to radians via multiplication by PI/180        
        float x = (float)(radius * Math.cos(angleInDegrees * Math.PI / 180F)) + origin.x;
        float y = (float)(radius * Math.sin(angleInDegrees * Math.PI / 180F)) + origin.y;

        return new Vector2(x, y);
    }

    //direction
    public Vector2 calculateMotionVectorForward(float target)
    {
        float moveX = 0;
        float moveY = 0;
        
        //quarrant I
        if(target <= 90f)
        {
            return calculateDirection(target);
        }

        //quarrant II
        if(target <= 180f)
        {
            Vector2 direction = calculateDirection(180f - target);
            moveX = direction.x * -1;
            moveY = direction.y;
            return new Vector2(moveX, moveY);
        }

        //quarrant III
        if(target <= 270f)
        {
            Vector2 direction = calculateDirection(270f - target);
            moveX = direction.y * -1;
            moveY = direction.x * -1;
            return new Vector2(moveX, moveY);
        }

        //quarrant IV
        if(target <= 360f)
        {
            Vector2 direction = calculateDirection(360f - target);
            moveX = direction.x;
            moveY = direction.y * -1;
            return new Vector2(moveX, moveY);
        }     

        

        return new Vector2(moveX, moveY);
    }

    public static double calculateDistance(Vector2 self, Vector2 target)
    {
        double selfX = self.x;
        double selfY = self.y;
        double targetX = target.x - selfX;
        double targetY = target.y - selfY;

        return Math.sqrt(Math.pow(targetX, 2) + Math.pow(targetY, 2));
    }

    private Vector2 calculateDirection(float sourceRotation)
    {
        float moveX = 0;
        float moveY = 0;
        if(sourceRotation < 45f)
        {
            moveX = 1f;
            moveY = (sourceRotation / 45);
        }  
        else
        {
            moveY = 1f;
            moveX = (90 - sourceRotation) / 45;
        }

        return new Vector2(moveX, moveY);
    }
}
