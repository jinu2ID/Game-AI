package controllers;

import engine.*;

import java.util.List;

/**
 * Created by jinuj on 4/22/2016.
 */
public class AvoidWallController extends Controller {

    private GameObject target;

    private RotatedRectangle rayCenter;
    private RotatedRectangle rayLeft;
    private RotatedRectangle rayRight;


    public AvoidWallController(GameObject _target){
        target = _target;
        rayCenter = new RotatedRectangle(0,0,1,1,0);
        rayLeft = new RotatedRectangle(0,0,1,1,0);
        rayRight = new RotatedRectangle(0,0,1,1,0);
    }

    public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;

        // Get targets position
        GameVector endPosition = new GameVector(target.getX(), target.getY());

        List<GameObject> objects = game.getObjects();

        int ray = rayCast(subject, objects);

        if(ray == 1)
            endPosition = rotateVector(endPosition, Math.PI/2);
        else if (ray == 2)
            endPosition = rotateVector(endPosition, -Math.PI/2);
        else if (ray == 3)
            endPosition = rotateVector(endPosition, Math.PI/2);

        // Get current direction
        GameVector dirVector = new GameVector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle()));
        dirVector = dirVector.normalize();

        // Get acceleration request
        GameVector accelRequest = seek(subject, endPosition);

        // Throttle/Brake output filtered
        double linearAccel = getLinearAccel(accelRequest, dirVector);

        // Left/Right output filtered
        double angularAccel = getAngularAccel(accelRequest, dirVector);

        // Throttle/Brake
        if (linearAccel > 0){
            controlVariables[VARIABLE_THROTTLE] = 1;
        }
        else if (linearAccel < 0.01){
            controlVariables[VARIABLE_BRAKE] = 1;
        }

        // Turn Left/Right
        if (angularAccel > 0){
            controlVariables[VARIABLE_STEERING] = 1;
        }
        else if (angularAccel < 0){
            controlVariables[VARIABLE_STEERING] = -1;
        }

        if (ray == 1) {
            controlVariables[VARIABLE_BRAKE] = 1;
            controlVariables[VARIABLE_THROTTLE] = 0;
        }
    }

    private GameVector seek(GameObject subject, GameVector end){

        // D = E - subject.position
        GameVector displacement = new GameVector(end.getX() - subject.getX(), end.getY() - subject.getY());

        // ND = D/|D|
        displacement = displacement.normalize();

        return displacement;
    }

    // Returns the projection of accelRequest onto dirVector
    private double getLinearAccel(GameVector accelRequest, GameVector dirVector){

        double projectVW = dirVector.dotProduct(accelRequest);
        return projectVW;
    }

    private double getAngularAccel(GameVector accelRequest, GameVector dirVector){

        double ca = Math.cos(Math.PI/2);
        double sa = Math.sin(Math.PI/2);
        GameVector right = new GameVector(ca * dirVector.getX() - sa * dirVector.getY(), sa * dirVector.getX() + ca * dirVector.getY());

        return (right.normalize().dotProduct(accelRequest));
    }

    private GameVector rotateVector(GameVector _target, double angle){
        double ca = Math.cos(angle);
        double sa = Math.sin(angle);
        GameVector rotated = new GameVector(ca * _target.getX() - sa * _target.getY(), sa * _target.getX() + ca * _target.getY());
        rotated.normalize();
        return rotated;

    }

    private int rayCast(Car subject, List<GameObject> _objects) {
        double angle = subject.getAngle();

        //System.out.println("Angle: " + Math.toDegrees(angle));
        //System.out.println("subject: " + "(" + subject.getX() + "," + subject.getY() + ")" );
        GameVector dir = new GameVector(Math.round(Math.cos(angle)), Math.round(Math.sin(angle)));
        //System.out.println(dir.toString());

        double offset = 25;
        rayCenter.C.x = subject.getX() + Math.round(dir.getX()) * offset;
        rayCenter.C.y = subject.getY() + Math.round(dir.getY()) * offset;

        rayLeft.C.x = rayCenter.C.x + Math.round(dir.getX()) * offset;
        rayLeft.C.y = rayCenter.C.y + Math.round(dir.getY()) * offset;
        rayLeft.ang = angle + Math.toRadians(-30);

        rayRight.C.x = rayCenter.C.x + Math.round(dir.getX()) * offset;
        rayRight.C.y = rayCenter.C.y + Math.round(dir.getY()) * offset;
        rayRight.ang = angle + Math.toRadians(30);

        //System.out.println("rayCenter: " + "(" + rayCenter.C.x + "," + rayCenter.C.y + ")");
        //System.out.println("rayLeft: " + "(" + rayLeft.C.x + "," + rayLeft.C.y + ")");

        for (GameObject obj : _objects) {
            if ((obj != subject) && (obj!= target) && (obj.collision(rayCenter))) {
                System.out.println("collision center");
                return 1;
            }
            if ((obj != subject) && (obj!= target) && (obj.collision(rayLeft))) {
                System.out.println("collision left");
                return 2;
            }
            if ((obj != subject) && (obj!= target) && (obj.collision(rayRight))) {
                System.out.println("collision right");
                return 3;
            }

        }

        return 0;
    }


}
