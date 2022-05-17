package model.player_model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import helper.EnumTag;

public class BlueShipPlayer extends Player
{

    public BlueShipPlayer() 
    {
        super("Assets/svg_spaceships/images/svg_spaceships_21.png", "player", EnumTag.player.tag);
        statSystem.attackDamgesBase = 0;
        statSystem.accelerate = 3;
        statSystem.deccelerate = 1;
        statSystem.maxSpeed = 1000;
        useStearingBehavior = true;
        this.baseRotation = -90;
    }
    
}
