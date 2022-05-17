package model.UI;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import helper.EnumRenderObject;
import model.GameObject;
import model.Renderer;

public class Text extends GameObject
{
    public CharSequence text;
    public BitmapFont font;

    public Text (String text)
    {
        this.text = text;
        font = new BitmapFont();
        EnumRenderObject.renderObject.list.add(this);
    }

    @Override
    public void render(Renderer renderer, float delta) 
    {
        // TODO Auto-generated method stub
        super.render(renderer, delta);
        font.draw(renderer.spriteBatch, text, this.getX(), this.getY());
    }
    
}
