package engine;

/**
 * Created by jinuj on 4/13/2016.
 * 2D Vector class for game engine
 */
public class GameVector {

    private double x;
    private double y;

    public GameVector(double _x, double _y){
        x = _x;
        y = _y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setX(double _x) {x = _x;}

    public void setY(double _y) {y = _y;}

    public void setXY(double _x, double _y){
        x = _x;
        y = _y;
    }


    // Add vectors by adding x and y components
    public GameVector add(GameVector v){

        double newX = x + v.getX();
        double newY = y + v.getY();

        return new GameVector(newX, newY);

    }

    // Subtract vectors by subtracting x and y components; subtracts v from caller (caller - v)
    public GameVector subtract(GameVector v){
        double newX = x - v.getX();
        double newY = y - v.getY();

        return new GameVector(newX, newY);
    }

    // Multiply a vector by a scalar
    public void multByScalar(double s){
        x = s*x;
        y = s*y;
    }

    public double magnitude(){
        return Math.sqrt((x*x)+(y*y));
    }

    // Normalize vector by dividing it by its magnitude
    public GameVector normalize(){

        double magnitude = this.magnitude();
        return new GameVector((x/magnitude), (y/magnitude));

    }

    public double dotProduct(GameVector v){
        //System.out.println("V1: " + this.toString());
        //System.out.println("V2: " + v.toString());
        double dotProd = (x * v.getX()) + (y * v.getY());
        //System.out.println("dot product: " + dotProd);
        return dotProd;
    }

    public String toString(){
        return new String("<" + x + "," + y + ">");
    }
}
