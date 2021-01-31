package frc.spartanlib.motion.path;

/**
 * It's disgusting, I know
 */
public class Point {
  
  public double x = 0, y = 0;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Point subtract(Point a) {
    return new Point(x - a.x, y - a.y);
  }

  public Point add(Point a) {
    return new Point(x + a.x, y + a.y);
  }
}
