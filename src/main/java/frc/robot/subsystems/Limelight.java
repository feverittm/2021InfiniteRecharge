package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Constants;

/**
 * A class that creates objects to interface with the Limelight computer vision
 * cameras.
 */
public class Limelight {
  private String LimelightName;
  NetworkTableInstance getNT = NetworkTableInstance.getDefault();
  private NetworkTable LimelightNT;

  /**
   * An object that creates an interface with the Limelight series of vision
   * cameras over the NetworkTables protocol.
   */
  public Limelight() {
    LimelightName = "Limelight";
    LimelightNT = getNT.getTable(LimelightName);
  }

  /**
   * An object that creates an interface with the Limelight series of vision
   * cameras over the NetworkTables protocol.
   */
  public Limelight(String name) {
    LimelightName = name;
    LimelightNT = getNT.getTable(LimelightName);
  }

  private static Limelight instance;

  public static Limelight getInstance() {
    if (instance == null)
      instance = new Limelight();
    return instance;
  }

  /**
   * setLEDMode() - Sets LED mode. 0 use the LED Mode set in the current pipeline,
   * 1 force off, 2 force blink, 3 force on
   */
  public void setLEDMode(double value) {
    LimelightNT.getEntry("ledMode").setNumber(value);
  }

  /**
   * setCAMMode() - Sets camera mode. 0 for Vision processor, 1 for Driver Camera
   * (Increases exposure, disables vision processing)
   */
  public void setCAMMode(double value) {
    LimelightNT.getEntry("camMode").setNumber(value);
  }

  /**
   * setPipeline() Sets current pipeline on Limelight.
   * 
   * @param Pipeline number
   */
  public void setPipeline(double value) {
    LimelightNT.getEntry("pipeline").setNumber(value);
  }

  /**
   * setStreamMode() - Sets limelight’s streaming mode -0 Standard - Side-by-side
   * streams if a webcam is attached to Limelight - 1 PiP Main - The secondary
   * camera stream is placed in the lower-right corner of the primary camera
   * stream - 2 PiP Secondary - The primary camera stream is placed in the
   * lower-right corner of the secondary camera stream
   */
  public void setStreamMode(double value) {
    LimelightNT.getEntry("stream").setNumber(value);
  }

  /**
   * getTV() - Monitor whether the limelight has any valid targets (0 or 1)
   * 
   * @return 1 if limelight target lock, 0 if no lock.
   */
  public double getTV() {
    return LimelightNT.getEntry("tv").getDouble(0);
  }

  /**
   * getTA() - Target Area (0% of image to 100% of image)
   * 
   * @return (0-100)
   */
  public double getTA() {
    return LimelightNT.getEntry("ta").getDouble(0);
  }

  /**
   * getTX() - monitor the Limelight's TX Value
   * 
   * @return Horizontal Offset From Crosshair To Target (-29.8 to 29.8 degrees)
   */
  public double getTX() {
    return LimelightNT.getEntry("tx").getDouble(0);
  }

  /**
   * getTY() - monitor the Limelight's TY Value
   * 
   * @return Vertical Offset From Crosshair To Target (-24.85 to 24.85 degrees)
   */
  public double getTY() {
    return LimelightNT.getEntry("ty").getDouble(0);
  }

  /**
   * getPipeline() - monitor the Limelight's pipeline Value
   * 
   * @return pipeline
   */
  public double getPipeline() {
    return LimelightNT.getEntry("pipeline").getDouble(0);
  }

  /**
   * getCameraTranslation() - Results of a 3D position solution
   * 
   * @return 6 numbers: Translation (x,y,y) Rotation(pitch,yaw,roll)
   */
  public double getCameraTranslation() {
    return LimelightNT.getEntry("camtran").getDouble(0);
  }

  /**
   * get() - monitor any value needed outside of currently provided.
   * 
   * @param key to pull
   * @return value of key
   */
  public double get(String entry) {
    return LimelightNT.getEntry(entry).getDouble(0);
  }

  /**
   * setSnapshot - Allows users to take snapshots during a match
   * 
   * @param 0 for no, 1 for 2 snapshots per second.
   * @return False if the table key already exists with a different type
   */
  public boolean setSnapshot(double value) {
    return LimelightNT.getEntry("snapshot").setValue(value);
  }

  /**
   * set() - Set any value outside what is currently provided in this library. Ex.
   * If Limelight implements new values, this is a way to use them out of the box.
   * 
   * @return False if the table key already exists with a different type
   * @param key to set, and value to set.
   */
  public boolean set(String entry, Double value) {
    return LimelightNT.getEntry(entry).setNumber(value);
  }

  /**
   * getDist() - calculates approximate distance from a fixed angled limelight to
   * the target.
   * 
   * @param targetHeight    = target height in meters
   * @param limelightHeight = height of limelight from the ground in meters,
   * @param limelightAngle  = angle in degrees of the limelight on the robot.
   * @param debug           = Enable printing current distance to console
   * @return approx distance in meters
   */
  public double getDist(double targetHeight, boolean debug) {
    double ll_radian = Math.toRadians(Constants.Values.VISION_LIMELIGHT_ANGLE);
    double a2 = getTY();
    double currentDist = (Math.abs(Constants.Values.VISION_TARGET_HEIGHT - Constants.Values.VISION_LIMELIGHT_HEIGHT) / Math.tan(ll_radian + a2));
    if (debug == true) {
      System.out.println("Limelight_library: Current distance to target is " + currentDist);
      return currentDist;
    } else {
      return currentDist;
    }
  }

}
