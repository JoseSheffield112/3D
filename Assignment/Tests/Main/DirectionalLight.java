/* I declare that this code is my own work */
/**
 * Author: Jose Alves
 * Email : jalves1@sheffield.ac.uk
 * Student # : 170163532
 */
/**
 * This whole class extends & was adapted from Dr.Maddocks "Light" class
 */
import java.util.ArrayList;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

/**
 * Extending light class
 */
public class DirectionalLight extends Light{

    private Vec3 direction;

    // constructor
    public DirectionalLight(GL3 gl, float dimness){
        super(gl, dimness);
        this.direction = new Vec3(10f,0f,0f); //goes left to right
        this.setDefaultAmbient(dimness);
    }   

    /**
     * Get methods
     */
    public Vec3 getDirection() {
        return direction;
    }

    /**
     * Set methods
     */
    public void setDefaultAmbient(float dimness){
        Vec3 defaultAmbient = new Vec3(0.5f,0.5f,0.5f);
        this.getMaterial().setAmbient(Vec3.multiply(defaultAmbient, dimness));
    }

    public void setDefaultDiffuseSpecular(float dimness){
        Vec3 defaultSpecular = new Vec3(0.8f,0.8f,0.8f);
        this.getMaterial().setDiffuse(Vec3.multiply(defaultSpecular, dimness));
        this.getMaterial().setSpecular(defaultSpecular);
    }
}