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
public class PointLight extends Light{

    // Declaring variables
    private Float constant, linear, quadratic, cutOff, outerCutOff;

    // Constructor
    public PointLight(GL3 gl, float dimness){
        super(gl, dimness);
        this.constant = 1.0f;
        this.linear = 0.07f;
        this.quadratic = 0.017f;
    }

    /**
     * Set methods
     */

    public void setConstant(float constant){
        constant = constant;
    }
    public void setLinear(float linear){
        linear = linear;
    }
    public void setQuadratic(float quadratic){
        quadratic = quadratic;
    }
    public void setCuttOff(float cuttOff){
        cuttOff = cuttOff;
    }
    public void setOuterCuttOff(float outerCuttOff){
        outerCuttOff = outerCuttOff;
    }

    /**
     * Get methods
     */

    public float getConstant(){
        return(constant);
    }
    public float getLinear(){
        return(linear);
    }
    public float getQuadratic(){
        return(quadratic);
    }
}