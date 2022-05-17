package mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Boot extends Game
{
    public static Boot INSTANCE;
    private int w, h;
    private OrthographicCamera mainCamera;
    GameScreen gameScreen;

    /**
     * this method will be call when game first launch
     */
    @Override
    public void create() 
    {
        this.w = Gdx.graphics.getWidth();
        this.h = Gdx.graphics.getHeight();
        this.mainCamera = new OrthographicCamera();
        this.mainCamera.setToOrtho(false, w, h);
        gameScreen = new GameScreen(mainCamera);
        setScreen(gameScreen);
    }
    
    @Override
    public void dispose() 
    {
        // TODO Auto-generated method stub
        super.dispose();
        gameScreen.dispose();
    }
}
