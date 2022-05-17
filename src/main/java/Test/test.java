package Test;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

public class test 
{
    // Driver program to test above function
    public static void main (String[] args)
    {
        // System.out.println(Math.toDegrees(Math.atan(-100f/200f)));
        // Vector3D player = new Vector3D(200, 0, 100);
        // Vector3D guard = new Vector3D(300, 0, 200);
        // Rotation r = new Rotation(player, guard);

        // System.out.println(r.getAngle());
        // System.out.println(Math.toDegrees(r.getAngle()));

        // Vector2D player = new Vector2D(1, 0);
        // Vector2D guard = new Vector2D(100, -100);

        // System.out.println(Vector2D.distance(player, guard));
        // System.out.println(Math.toDegrees(Vector2D.angle(player, guard)));

        // System.out.println(PointOnCircle(100, 270, new Vector2(0, 0)));

        float targetAngle = 22;
        float rotateSpeed = 30;
        float currentAngle = 90;
        float leftWeight = targetAngle - currentAngle;
        float rightWeight = currentAngle + (360 - targetAngle);

        System.out.println("left weight: " + leftWeight + " right weight: " + rightWeight);
    }

    public static Vector2 PointOnCircle(float radius, float angleInDegrees, Vector2 origin)
    {

        // Convert from degrees to radians via multiplication by PI/180        
        float x = (float)(radius * Math.cos(angleInDegrees * Math.PI / 180F)) + origin.x;
        float y = (float)(radius * Math.sin(angleInDegrees * Math.PI / 180F)) + origin.y;

        return new Vector2(x, y);
    }
}
