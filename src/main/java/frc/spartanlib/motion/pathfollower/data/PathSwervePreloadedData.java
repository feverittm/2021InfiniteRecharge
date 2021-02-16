package frc.spartanlib.motion.pathfollower.data;

import edu.wpi.first.wpilibj.geometry.Rotation2d;

public class PathSwervePreloadedData extends PathPreloadedData {

  private Rotation2d mStartRot, mEndRot;

  public PathSwervePreloadedData(String name, Rotation2d startRotation, Rotation2d endRotation) {
    super(name);

    mStartRot = startRotation;
    mEndRot = endRotation;
  }

  // This is why I'm switching the lang we use
  public Rotation2d getStartRotation() { return mStartRot; }
  public Rotation2d getEndRotation() { return mEndRot; }

}