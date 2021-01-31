package frc.spartanlib.motion.path;

public class Waypoint {

  private Point mPosition, mHeader;

  public Waypoint(Point position, Point header) {
    mPosition = position; mHeader = header;
  }

  public Point getPosition() { return mPosition; }
  public Point getHeader() { return mHeader; }

}
