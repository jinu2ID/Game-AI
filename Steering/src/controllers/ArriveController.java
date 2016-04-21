package controllers;

import engine.GameObject;
import engine.Car;
import engine.GameVector;
import engine.Game;


/**
 * Created by jinuj on 4/18/2016.
 */
public class ArriveController extends Controller {

    private GameObject target;

    public ArriveController(GameObject _target){
        target = _target;
    }

    public void update(Car subject, Game game, double delta_t, double[] controlVariables){
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;

        // Get targets position
        GameVector endPosition = new GameVector(target.getX(), target.getY());

        // Get current direction
        GameVector dirVector = new GameVector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle()));
        dirVector = dirVector.normalize();

        // Get acceleration request
        GameVector accelRequest = arrive(subject, endPosition, .01, 100, 0.04);

        // Throttle/Brake output filtered
        double linearAccel = getLinearAccel(accelRequest, dirVector);

        // Left/Right output filtered
        double angularAccel = getAngularAccel(accelRequest, dirVector);

        // Throttle/Brake
        if (linearAccel > 0){
            controlVariables[VARIABLE_THROTTLE] = 1;
        }
        else if (linearAccel < 0){
            controlVariables[VARIABLE_BRAKE] = 1;
        }

        // Turn Left/Right
        if (angularAccel > 0){
            controlVariables[VARIABLE_STEERING] = 1;
        }
        else if (angularAccel < 0){
            controlVariables[VARIABLE_STEERING] = -1;
        }

    }

    GameVector arrive(Car subject, GameVector end, double targetRadius, double slowRadius, double time){


        // D = E - character.position
        GameVector displacement = new GameVector(end.getX() - subject.getX(), end.getY() - subject.getY());

        // Length = |D|
        double length = displacement.magnitude();
        //System.out.println("length: " + length);
        if (length < targetRadius) {
            return new GameVector(0,0);
        }

        double targetSpeed;
        double maxSpeed = 250;

        //If Length>slowRadius then targetSpeed = maxSpeed
        if (length > slowRadius){
            targetSpeed = maxSpeed;
        } else { // else targetSpeed = maxSpeed * Length/slowRadius
            targetSpeed = maxSpeed * length/slowRadius;

        }
        //System.out.println(targetSpeed);

        // targetVelocity = (D/|D|)*targetSpeed
        GameVector targetVelocity = displacement.normalize();
        targetVelocity.multByScalar(targetSpeed);
        //System.out.println("target veloc: " + targetVelocity.toString());

        double subjAlpha = subject.getAngle();
        //System.out.println("subjAlpha: " + subjAlpha);
        GameVector subjVelocity = new GameVector(subject.getSpeed() * Math.cos(subjAlpha), subject.getSpeed() * Math.sin(subjAlpha));
        //System.out.println("subject veloc: " + subjVelocity.toString());

        // A = (targetVelocity – character.velocity)/time
        GameVector accelRequest = (targetVelocity.subtract(subjVelocity));
        //System.out.println("accelRequest: " + accelRequest.toString());
        accelRequest.multByScalar((25));


        // If |A|>maxAcceleration then A = (A/|A|)*maxAcceleration
        if (accelRequest.magnitude() > 1){
            accelRequest = accelRequest.normalize();
            accelRequest.multByScalar(1);
        }

        return accelRequest;
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
}
