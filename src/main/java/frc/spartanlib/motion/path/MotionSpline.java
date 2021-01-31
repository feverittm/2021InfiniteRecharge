package frc.spartanlib.motion.path;

import java.util.ArrayList;
import java.util.List;

public class MotionSpline {

  private Waypoint mA = null, mB = null;
  private Point[] mPoints = null;

  public MotionSpline(Waypoint a, Waypoint b) {
    mA = a;
    mB = b;
  }

  public void calulate(int steps) {
    Point p0, p1, p2, p3;
    p0 = mA.getPosition();
    p1 = mA.getHeader();
    p2 = mB.getPosition().subtract(mB.getHeader().subtract(mB.getPosition()));
    p3 = mB.getPosition();

    List<Point> points = new ArrayList<Point>();

    for (double i = 0; i < 1; i += (1 / (double) steps)) {
      double x = (Math.pow((1 - i), 3) * p0.x) + (Math.pow((1 - i), 2) * 3 * i * p1.x)
          + ((1 - i) * 3 * i * i * p2.x) + (i * i * i * p3.x);
      double y = (Math.pow((1 - i), 3) * p0.y) + (Math.pow((1 - i), 2) * 3 * i * p1.y)
          + ((1 - i) * 3 * i * i * p2.y) + (i * i * i * p3.y);
      points.add(new Point(x, y));
      System.out.println(x + ", " + y);
    }
    double x = (Math.pow((1 - 1), 3) * 3 * p0.x) + (Math.pow((1 - 1), 2) * 3 * 1 * p1.x) + ((1 - 1) * 3 * 1 * 1 * p2.x)
        + (1 * 1 * 1 * p3.x);
    double y = (Math.pow((1 - 1), 3) * 3 * p0.y) + (Math.pow((1 - 1), 2) * 3 * 1 * p1.y) + ((1 - 1) * 3 * 1 * 1 * p2.y)
        + (1 * 1 * 1 * p3.y);
    points.add(new Point(x, y));

    mPoints = new Point[points.size()];
    for (int i = 0; i < mPoints.length; i++) {
      mPoints[i] = points.get(i);
    }
  }

  public Point[] getPoints() {
    return mPoints;
  }

}
