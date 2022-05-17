package model.player_model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import helper.EnumRenderObject;
import helper.EnumTag;
import helper.Timer;
import model.projectile.Projectile;

public class ArcShooter extends Player
{
    public Timer readyToShoot;
    private double shootCD = 0.05;
    public ArcShooter() 
    {
        super("Assets/tanknsoldier/enemy/enemy 1/idle/enemy1idle1.png", "ARC", EnumTag.player.tag);
        super.useStearingBehavior = false;
        super.statSystem.bulletSpeedModifier = 60;
        readyToShoot = new Timer(shootCD);
    }
    
    @Override
    public void update(float delta) 
    {
        super.update(delta);

        //shooting
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && readyToShoot.isTimeUp())
        {
            Projectile projectile = new Projectile("Assets/bullet/tile147.png", this, 0, new Vector2(-9, -8), 37);
            projectile.disposeTimer.resetTimer(0.4);
            
            EnumRenderObject.renderProjectile.list.addLast(projectile);
            readyToShoot.resetTimer();
        }

    }
}
