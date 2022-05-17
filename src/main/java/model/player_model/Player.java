package model.player_model;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.Timer;
import model.GameObject;
import model.Renderer;
import model.projectile.Projectile;

public class Player extends GameObject
{
    Timer readyToShoot;
    public boolean controllable = true;

    private double shootCD = 1;

    public Player(String spritePath, String name, String tag) 
    {
        super(spritePath, name, tag);
        this.enableTriggerCollision = true;
        readyToShoot = new Timer(shootCD);
    }

    @Override
    public void start() 
    {
        // TODO Auto-generated method stub
        super.start();
    }

    @Override
    public void render(Renderer renderer, float delta) 
    {
        super.render(renderer, delta);
    }

    @Override
    public void update(float delta) 
    {
        super.update(delta);
        
        if(controllable)
        {
            //movement
            if(Gdx.input.isKeyPressed(Input.Keys.D))
            {
                if(!useStearingBehavior)
                    super.addForce(1, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A))
            {
                if(!useStearingBehavior)
                    super.addForce(-1, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W))
            {
                if(useStearingBehavior)
                {
                    if(this.statSystem.moveSpeed < 0)
                        breaking();
                    else
                        moveForward();
                }
                else
                    super.addForce(0, 1);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S))
            {
                if(useStearingBehavior)
                {
                    if(this.statSystem.moveSpeed > 0)
                        breaking();
                    else
                        moveBackward();
                }
                else
                    super.addForce(0, -1);
            }

            //testing rotation
            if(Gdx.input.isKeyPressed(Input.Keys.Q))
            {
                super.addRotation(1f);
                // System.out.println("rotation: " + super.rotation);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.E))
            {
                super.addRotation(-1f);
                // System.out.println("rotation: " + super.rotation);
            }

            //testing flip
            // if(Gdx.input.isKeyJustPressed(Input.Keys.F))
            // {
            //     super.flipX();
            // }

            //testing move speed
            // if(Gdx.input.isKeyJustPressed(Input.Keys.T))
            // {
            //     super.addMoveSpeed(20f);;
            // }

            // if(Gdx.input.isKeyJustPressed(Input.Keys.G))
            // {
            //     super.addMoveSpeed(-20f);;
            // }

            // //shooting
            // if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && readyToShoot.isTimeUp())
            // {
            //     Projectile projectile = new Projectile("Assets/Water_Effect/01/Fix/Water__01.png", this, 90, new Vector2(13, 0));
            //     projectile.disposeTimer.resetTimer(6);
            //     EnumRenderObject.renderProjectile.list.addLast(projectile);
            //     readyToShoot.resetTimer();
            // }
        }
    }

    @Override
    public void onCollisionWith(GameObject otherGameObject) 
    {
        if(enableTriggerCollision)
        {
            if(this.hitBox.overlaps(otherGameObject.hitBox))
            {
                // System.out.println(otherGameObject.name);
            }
        }
    }
}
