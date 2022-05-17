package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import helper.Constants;
import helper.EnumConstants;
import helper.TileMapHelper;

import static helper.Constants.PPM;

public class Renderer 
{
    public OrthographicCamera camera;
    public SpriteBatch spriteBatch;
    public World world;
    public Box2DDebugRenderer box2dDebugRenderer;
    public ShapeRenderer shapeRenderer;
    public OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    public TiledMap tiledMap;

    //camera
    private float camX = 0;
    private float camY = 0;
    private float camZ = 0;

    public Renderer(OrthographicCamera camera)
    {
        setUp(camera);
        shapeRenderer = new ShapeRenderer();
    }

    public Renderer(OrthographicCamera camera, Vector3 initalCameraPos)
    {
        setUp(camera);
        setCameraPosition(initalCameraPos);
        shapeRenderer = new ShapeRenderer();
    }

    private void setUp(OrthographicCamera camera)
    {
        this.camera = camera;
        this.spriteBatch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.box2dDebugRenderer = new Box2DDebugRenderer();

        tiledMap = new TmxMapLoader().load("C:/Users/vince/OneDrive/study/Oswego/CSC455/HW6/TileMap.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        orthogonalTiledMapRenderer.setView(this.camera);
    }

    public void dispose()
    {
        spriteBatch.dispose();
        world.dispose();
        box2dDebugRenderer.dispose();
        shapeRenderer.dispose();
    }

    public void render()
    {
        world.step(1/60f, 6, 2);

        cameraUpdate();

        spriteBatch.setProjectionMatrix(camera.combined);
        
        setRGBBackground(143, 232, 180, 1);
        
        orthogonalTiledMapRenderer.render();

        box2dDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    public void setRGBBackground(double red, double green, double blue, double alpha)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glClearColor((float)red/255f, (float)green/255f, (float)blue/255f, (float)alpha);
    }

    //--------------------------------camera------------------------------------

    private void cameraUpdate()
    {
        camera.position.set(new Vector3(camX, camY, camZ));
        camera.update();
    }

    public void addCameraX(float x)
    {
        camX += x;
    }

    public void addCameraY(float y)
    {
        camY += y;
    }

    public void addCameraZ(float z)
    {
        camZ += z;
    }

    public void setCameraPosition(Vector3 vector3)
    {
        camX = vector3.x;
        camY = vector3.y;
        camZ = vector3.z;
    }
}
