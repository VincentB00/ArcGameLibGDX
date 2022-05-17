package helper;

public enum EnumTag 
{
    helper("HELPER"),
    player("PLAYER"),
    enemy("ENEMY"),
    tile("TILE"),
    objective("OBJECTIVE");
    
    public final String tag;

    EnumTag(String tag)
    {
        this.tag = tag;
    }
}
