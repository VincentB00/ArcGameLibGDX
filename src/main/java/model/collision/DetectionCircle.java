package model.collision;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import helper.Timer;
import model.GameObject;
import model.Renderer;

public class DetectionCircle extends Circle
{
    private GameObject source;
    public boolean renderCircle = false;
    public LinkedList<GameObject> detectGameObjectList;
    Timer clearDetectObjectTimer;

    public final int clearDetectObject = 2;

    public DetectionCircle(float radius, GameObject source)
    {
        super(source.getX(), source.getY(), radius);
        this.source = source;
        detectGameObjectList = new LinkedList<>();
        clearDetectObjectTimer = new Timer(clearDetectObject);
    }

    public void render(Renderer renderer, float delta)
    {
        this.update(delta);


        if(renderCircle)
        {
            renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.shapeRenderer.setColor(Color.RED);
            renderer.shapeRenderer.circle(this.x, this.y, this.radius);
            renderer.shapeRenderer.end();
        }

        if(clearDetectObjectTimer.isTimeUp())
        {
            detectGameObjectList.clear();
            clearDetectObjectTimer.resetTimer();
        }
    }

    public void update(float delta)
    {
        Vector2 vector2 = new Vector2(source.position);
        // vector2.set(vector2.x + (source.getWidth() / 2), vector2.y + (source.getHeight() / 2));
        // vector2.set(source.position);
        vector2.set(source.getMiddlePosition());
        this.setPosition(vector2);
    }

    public void switchRenderCircle()
    {
        this.renderCircle = !this.renderCircle;
    }

    public boolean onCollisionWith(Rectangle rectangle)
    {
        return Intersector.overlaps(this, rectangle);
    }

    public void addDetectGameObject(GameObject otherGameObject)
    {
        if(otherGameObject != source && !detectGameObjectList.contains(otherGameObject))
            detectGameObjectList.add(otherGameObject);
    }
}
