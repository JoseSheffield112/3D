/**
 * 
 * TODO LIST
 * 
 * sort out get and set methods
 * For all lights classes, you need to figure out a way to set their lights correctly (this, sunlight, should not have same light values as a lamp!)
 * Current way of doing it sucks - maybe the light class constructor should take an int, and that is used to change the values?
 * 
 * 
 * 
 * Explanation : Directional lights in the tutorial have a directin - 
 * however I didn't bother with that as it required more work and I can get direction by subtracting light.position from fragment
 * 
 * Though a directional light is supposed to have a direction and no position, since the class we extend from (Light)
 * has a position, for now I have left direction light with a position and no direction - for calculation I can just do position-fragPosition
 * As this will allow me to render the light for debugging
 * 
 * Further into the project I could explore getting rid of the position
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
        this.direction = new Vec3(10f,0f,0f); //goes left to right
        // I can either do it this horrible way, or I can pass the vectors into the constructor - though I'd still need the default for use in glEventListener class
        this.setDefaultAmbient(dimness);
    }   


    
    public Vec3 getDirection() {
        return direction;
    }


    // public as i need to call this is the gleventlistener to update the day cycle!
    public void setDefaultAmbient(float dimness){
        Vec3 defaultAmbient = new Vec3(0.5f,0.5f,0.5f);
        this.getMaterial().setAmbient(Vec3.multiply(defaultAmbient, dimness));
    }

    public void setDefaultDiffuseSpecular(float dimness){
        Vec3 defaultSpecular = new Vec3(0.8f,0.8f,0.8f);
        this.getMaterial().setAmbient(Vec3.multiply(defaultSpecular, dimness));
        this.getMaterial().setDiffuse(defaultSpecular);
    }
}