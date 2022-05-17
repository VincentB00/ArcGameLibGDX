package model.NPC_model;

import java.util.Random;

import helper.EnumTag;
import helper.Timer;
import model.GameObject;

public class Objective extends GameObject
{
    public int type;
    public Timer activateTimer;

    public Objective(int x, int y)
    {
        super("Assets/tanknsoldier/icon/coin/coin-export1.png", EnumTag.objective.tag);
        this.setPosition(x, y);
        Random random = new Random();
        type = random .nextInt(4);
        activateTimer = new Timer(5);

        if(type == 0)
        {
            this.setSprite("Assets/tanknsoldier/icon/coin/coin-export1.png");
        }   
        else if(type == 1)
        {
            this.setSprite("Assets/tanknsoldier/icon/energy/energy-export1.png");
        }
        else
        {
            this.setSprite("Assets/tanknsoldier/icon/gem/gem-export1.png");
        }

    }
}
