package frc.spartanlib.motion.path;

import java.util.ArrayList;
import java.util.List;

public class MotionProcessor {

  private MotionPath mPath;
  private Point[] mPoints;

  public MotionProcessor(MotionPath path) {
    mPath = path;
  }

  public double getPathDistance() {
    if (mPath.getPoints() == null) mPath.calculate(1000);

    double dist = 0;

    Point[] p = mPath.getPoints();

    for (int i = 0; i < p.length - 1; i++) {
      dist += getDist(p[i], p[i + 1]);
    }

    return dist;
  }

  public void genAdjustedPoints(double dist) {

    mPath.calculate(1000);
    Point[] pOld = mPath.getPoints();

    List<Point> newPoints = new ArrayList<Point>();

    Point a = pOld[0];
    newPoints.add(a);
    double d = 0;

    int total = 0;
    double acum = 0;

    for (int i = 1; i < pOld.length; i++) {
      d = getDist(a, pOld[i]);
      if (d >= dist) {
         newPoints.add(pOld[i]);
         a = pOld[i];
         total++;
         acum += d;
      }
    }

    System.out.println("Avg Distance: " + (acum / total));

    mPoints = new Point[newPoints.size()];
    for (int i = 0; i < mPoints.length; i++) {
      mPoints[i] = newPoints.get(i);
    }
  }

  public Point[] getPoints() { return mPoints; }

  private double getDist(Point a, Point b) {
    double x = Math.pow(b.x - a.x, 2);
    double y = Math.pow(b.y - a.y, 2);
    return Math.sqrt(x + y);
  }
}
