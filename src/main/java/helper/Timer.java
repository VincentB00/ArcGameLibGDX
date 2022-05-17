package helper;

public class Timer 
{
    private double setSecond;
    public double time; //in second
    
    public Timer(double time)
    {
        setSecond = (double)System.currentTimeMillis() / 1000;
        this.time = time;
    }

    public void resetTimer()
    {
        setSecond = (double)System.currentTimeMillis() / 1000;
    }

    public void resetTimer(double time)
    {
        setSecond = (double)System.currentTimeMillis() / 1000;
        this.time = time;
    }

    public boolean isTimeUp()
    {
        double currentTime = (double)System.currentTimeMillis() / 1000;

        if((currentTime - setSecond) >= time)
            return true;
        else
            return false;
    }
}
