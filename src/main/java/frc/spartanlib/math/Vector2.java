package frc.spartanlib.math;

public class Vector2 {

  public double x, y;

  public Vector2(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getMagnitude() { return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); }

}
