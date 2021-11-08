/**
 * TODO LIST
 * Need to add directional light class
 * need to change all other classes that depend on that
 * then you also need to make sure the fragment shaders are fine!
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
        this.direction = new Vec3(0f,-1f,0f); //Has to be negative in order to point down!
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
   
}