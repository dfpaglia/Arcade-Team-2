
public class Vector2D {
	//z is weird, it's like a scale factor. IT IS NOT A DEPTH COORDINATE, DO NOT TREAT IT AS SUCH. for instance, you do not add the z's when you add vectors.
	//Treating z as a depth coordinate WILL screw you over.
	private double x, y, z;
	
	public Vector2D(){
		x = 0;
		y = 0;
		z = 1;
	}
	
	public Vector2D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	//Normalize such that the scale factor z=1. This is basically called by 
	//all functions in this class. usually, 0 is used for z to denote this is a position vector,
	//but this is purely a vector class, so 0 is not a valid z value.
	public void normalize(){
		if(z!=0){
			x/=z;
			y/=z;
			z=1;
		}else{
			z=1;
		}
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	//Gives the lenght of the vector
	public double magnitude(){
		return Math.sqrt(dotProduct(this,this));
	}
	public String toString(){
		return "< " + x + ", " + y + ", " + z + ">";
	}
	//Gives a vector in the same direction of a such that it's lenght is 1
	public static Vector2D unitVector(Vector2D a){
		double mag = a.magnitude();
		if(mag!=0){
			return new Vector2D(a.getX()/mag, a.getY()/mag, 1);
		}
		return new Vector2D(0,0,1);
	}
	//Inverts the vector, so it points in the opposite direction
	public static Vector2D opposite(Vector2D a){
		a.normalize();
		return new Vector2D(-a.getX(), -a.getY(), 1);
	}
	//scales the vector by some scale factor.
	public static Vector2D scale(Vector2D a, double scale){
		a.normalize();
		return new Vector2D(scale*a.getX(), scale*a.getY(), 1);
	}
	//Vectorally sums 2 vectors.
	public static Vector2D add(Vector2D a, Vector2D b){
		a.normalize();
		b.normalize();
		return new Vector2D(a.getX() + b.getX(), a.getY() + b.getY(), 1);
	}
	//Vectorally subtracts 2 vectors. (a-b)
	public static Vector2D subtract(Vector2D a, Vector2D b){
		Vector2D c = opposite(b);
		c = add(a,c);
		return c;
	}
	//Gives the dot product
	public static double dotProduct(Vector2D a, Vector2D b){
		a.normalize();
		b.normalize();
		return a.getX()*b.getX() + a.getY()*b.getY();
	}
	//Gives the shortest angle between 2 vectors
	public static double angleBetween(Vector2D a, Vector2D b){
		double magProd = a.magnitude()*b.magnitude();
		if(magProd>0){
			return Math.acos(dotProduct(a,b)/magProd);
		}
		return 0;
	}
	//Projects vector a onto b. 
	public static Vector2D project(Vector2D a, Vector2D b){
		Vector2D c = unitVector(b);
		return Vector2D.scale(c, Vector2D.dotProduct(a, c));
	}
	//Rotate by angle in radians
	public static Vector2D rotate(Vector2D a, double angle){
		return new Vector2D(a.getX()*Math.cos(angle) - a.getY()*Math.sin(angle), a.getX()*Math.sin(angle) + a.getY()*Math.cos(angle), 1);
	}
}
