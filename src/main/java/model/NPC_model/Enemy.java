package model.NPC_model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.Timer;
import model.GameObject;
import model.projectile.Projectile;

public class Enemy extends GameObject
{
    private Timer readyToShoot;
    private Timer readyToMove;
    private double shootCD = 2;
    private double moveCD = 2;
    private int destination = -1;
    public int destinationOffset = 40;
    private boolean moveRight = true;
    public boolean autoShoot = true;
    public boolean autoMove = true;

    public Enemy(String spritePath) 
    {
        super(spritePath);
        start();
    }
    
    public Enemy(String spritePath, String name)
    {
        super(spritePath, name);
        start();
    }

    public Enemy(String spritePath, String name, String tag)
    {
        super(spritePath, name, tag);
        start();
    }

    @Override
    public void start() 
    {
        super.start();
        readyToShoot = new Timer(shootCD);
        readyToMove = new Timer (moveCD);
    }

    @Override
    public void draw(Batch batch) 
    {
        // TODO Auto-generated method stub
        super.draw(batch);
    }

    @Override
    public void update(float delta) 
    {
        // TODO Auto-generated method stub
        super.update(delta);

        // LinkedList<Integer> temp = new LinkedList<Integer>(); 
        // for(int count = 0; count < childs.size(); count++)
        // {
        //     if(childs.get(count).isOutOfScreen())
        //         temp.add(count);
        // }
        // while(!temp.isEmpty())
        // {
        //     childs.remove(temp.getLast());
        //     temp.removeLast();
        // }

        if(readyToShoot.isTimeUp() && autoShoot)
        {
            Projectile projectile = new Projectile("Assets/Water_Effect/01/Fix/Water__02.png", this, 90, new Vector2(0, 0));
            EnumRenderObject.renderProjectile.list.add(projectile);
            readyToShoot.resetTimer();
        }

        if(readyToMove.isTimeUp() && autoMove)
        {
            if(destination == -1)
            {
                if(moveRight)
                {
                    destination = (int) (this.position.x + destinationOffset);
                }
                else
                {
                    destination = (int) (this.position.x - destinationOffset);
                }
            }
            
            if(this.position.x >= destination && moveRight)
            {
                destination = -1;
                readyToMove.resetTimer();
                moveRight = !moveRight;
            }
            else if(this.position.x <= destination && !moveRight)
            {
                destination = -1;
                readyToMove.resetTimer();
                moveRight = !moveRight;
            }
            else
            {
                if(moveRight)
                    this.addForce(1, 0);
                else
                    this.addForce(-1, 0);
            }

            // System.out.println("position " + this.position + " destination " + destination);
        }


    }

    public void shoot()
    {
        Projectile projectile = new Projectile("Assets/Water_Effect/01/Fix/Water__02.png", this, 90, new Vector2(0, 0));
        EnumRenderObject.renderProjectile.list.add(projectile);
    }
}
