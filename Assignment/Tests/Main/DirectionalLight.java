/**
 * Explanation : Directional lights in the tutorial have a directin - 
 * however I didn't bother with that as it required more work and I can get direction by subtracting light.position from fragment
 * 
 */
import java.util.ArrayList;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

/**
 * creating a subclass of Light class
 */
public class DirectionalLight extends Light{

    private Vec3 direction;
    public DirectionalLight(GL3 gl, float dimness){
        super(gl, dimness);
    }

    /**
     * Setting spotlight direction
     */
    public void setDirection(Vec3 v) {
        direction.x = v.x;
        direction.y = v.y;
        direction.z = v.z;
    }
      
    public void setDirection(float x, float y, float z) {
        direction.x = x;
        direction.y = y;
        direction.z = z;
    }

    public Vec3 getDirection() {
        return direction;
    }
   
}