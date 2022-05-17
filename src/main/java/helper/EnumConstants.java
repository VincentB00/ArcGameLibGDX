package helper;

public enum EnumConstants 
{
    screenW(1920),
    screenH(800);
    
    public final int ratio;

    EnumConstants(int ratio)
    {
        this.ratio = ratio;
    }
}
