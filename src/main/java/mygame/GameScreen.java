package mygame;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import helper.EnumConstants;
import helper.EnumRenderObject;
import model.GameObject;
import model.Renderer;

public class GameScreen extends ScreenAdapter 
{
    Renderer renderer;
    ArcHeroGame arcHeroGame;
    StageMachineGame stageMachineGame;
    public GameScreen(OrthographicCamera camera)
    {
        renderer = new Renderer(camera, new Vector3((EnumConstants.screenW.ratio / 2), (EnumConstants.screenH.ratio / 2), 0));

        // arcHeroGame = new ArcHeroGame(renderer);

        stageMachineGame = new StageMachineGame(renderer);
    }

    

    /**
     * this method will be call every frame
     */
    private void update()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        }
    }

    /**
     * this method will be call every frame
     */
    @Override
    public void render(float delta) 
    {
        this.update();

        renderer.render();
        
        renderer.spriteBatch.begin();

        LinkedList<GameObject> gameObjectsList = EnumRenderObject.renderObject.list;
        for(int count = 0; count < gameObjectsList.size(); count++)
        {
            GameObject currentGameObject = gameObjectsList.get(count);
            currentGameObject.render(renderer, delta);
            for(int count1 = 0; count1 < gameObjectsList.size(); count1++)
            {
                GameObject gameObject = gameObjectsList.get(count1);
                if(gameObject == null)
                    continue;

                if(currentGameObject != gameObject)
                    currentGameObject.onCollisionWith(gameObject);
            }
        }

        gameObjectsList = EnumRenderObject.renderProjectile.list;
        for(int count = 0; count < gameObjectsList.size(); count++)
            gameObjectsList.get(count).render(renderer, delta);

        stageMachineGame.render(delta);

        // arcHeroGame.render(delta);

        renderer.spriteBatch.end();
        
    }

    @Override
    public void dispose() 
    {
        System.out.println("dispose");
        // TODO Auto-generated method stub
        super.dispose();
        renderer.dispose();
    }
}
