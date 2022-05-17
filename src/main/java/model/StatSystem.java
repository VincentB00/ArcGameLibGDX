package model;

import java.util.Random;

public class StatSystem 
{
    public double currentHeath = 0;
    public double maxHeath = 0;
    public double baseHeath = 0;
    public double heathModifier = 0;
    public double moveSpeedModifier = 0;

    //attack damges
    public double attackDamgesBase = 10;
    public double attackDamgesModifier = 0;
    
    //accuracy
    public double accuracyBase = 90;
    public double accuracyModifier = 0;

    public boolean imortal = false;

    //bullet
    public double bulletSpeed = 60 * 2;
    public double bulletSpeedModifier = 0;

    public double worthPoint = 0;

    //--------------speed-----------------
    public double maxSpeed = 60;
    public double moveSpeed = 0;
    public double accelerate = 3;
    public double deccelerate = 1;
    public double breakSpeed = 2;

    //--------------rotation--------------
    public double rotationSpeed = 40;

    private GameObject parrentGameObject;

    private Random random;
    
    public StatSystem(GameObject parrentGameObject)
    {
        baseHeath = 100;
        calculateHeath();
        fullyHeal();
        this.parrentGameObject = parrentGameObject;
        this.random = new Random();
    }

    public void takeDamges(double damges)
    {
        currentHeath -= damges;
    }

    public void removeAllModifier()
    {
        heathModifier = 0;
    }

    public void fullyHeal()
    {
        currentHeath = maxHeath;
    }

    public void calculateHeath()
    {
        maxHeath = baseHeath + heathModifier;
        if(currentHeath > maxHeath)
            currentHeath = maxHeath;
    }

    public void update()
    {
        calculateHeath();
    }

    public boolean isDead()
    {
        if(currentHeath <= 0 && !imortal)
            return true;
        else
            return false;
    }

    public double maxSpeed()
    {
        return maxSpeed;
    }

    public void accelerate()
    {
        moveSpeed += accelerate;
        if(moveSpeed >= maxSpeed())
            moveSpeed = maxSpeed();
    }

    public void backcelerate()
    {
        moveSpeed -= accelerate;
        double maxBackSpeed = maxSpeed() * -1;
        if(moveSpeed <= maxBackSpeed)
            moveSpeed = maxBackSpeed;
    }

    public void deccelerate()
    {
        deccelerate(this.deccelerate);
    }

    private void deccelerate(double decelerateSpeed)
    {
        if(moveSpeed < 0)
            moveSpeed+= decelerateSpeed;
        else if(moveSpeed > 0)
            moveSpeed-= decelerateSpeed;
    }

    public void breaking()
    {
        deccelerate(this.breakSpeed);
    }

    public float getMaxBulletSpeed()
    {
        return (float) (bulletSpeed + bulletSpeedModifier);
    }

    public float getMaxAttackDamges()
    {
        return (float) (attackDamgesBase + attackDamgesModifier);
    }

    public float getBulletSpreadAngle()
    {
        float base = (float) (accuracyBase + accuracyModifier);

        if(base >= 100)
            return 0;
        
        base = 100 - base;

        float randomfloat = random.nextInt((int)base) * (random.nextInt(2) == 1 ? 1 : -1);

        return randomfloat;
    }
}
