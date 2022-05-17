package helper;

import java.util.LinkedList;

import model.GameObject;

public enum EnumRenderObject 
{
    renderObject(new LinkedList<GameObject>()),
    renderProjectile(new LinkedList<GameObject>());
    
    public LinkedList<GameObject> list;

    EnumRenderObject(LinkedList<GameObject> list)
    {
        this.list = list;
    }
}
