package model.collision;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import model.GameObject;
import model.Renderer;


public class HitBox extends Rectangle
{   
    public boolean renderHitBox = false;
    public GameObject source;

    public HitBox(GameObject source) 
    {
        super(source.position.x, source.position.y, source.getWidth(), source.getHeight());
        this.source = source;
        // shapeRenderer.setAutoShapeType(true);
    }

    public HitBox()
    {
        // shapeRenderer.setAutoShapeType(true);
    }

    public void update(float delta)
    {
        this.setPosition(source.position);
    }

    public void render(Renderer renderer, float delta)
    {
        update(delta);

        if(renderHitBox)
        {
            renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.shapeRenderer.setColor(Color.GREEN);
            renderer.shapeRenderer.rect(x, y, width, height);
            renderer.shapeRenderer.end();
        }
    }

    public void switchShowHitBox()
    {
        this.renderHitBox = !this.renderHitBox;
    }

    public void dispose()
    {
        this.set(0, 0, 0, 0);
    }

    
}