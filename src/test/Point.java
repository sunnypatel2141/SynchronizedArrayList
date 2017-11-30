package test;

public class Point {
  private static int count = 0;

  private double x;
  private double y;
  private double z;

  public Point(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
    Point.count++;
  }

  public Point(Point other) {
    this(other.x, other.y, other.z);
  }

  public static int getNumberCreated() {
    return Point.count;
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
  
  public String toString() {
	  return "[" + new Double(getX()) + ", " + new Double(getY()) + ", " + new Double(getZ()) + "]";  
  }
}