package frc.spartanlib.motion.pathfollower.data;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;

public class PathSwerveGeneratedData extends PathGeneratedData {

  private Rotation2d mStartRot, mEndRot;

  public PathSwerveGeneratedData(String name, Rotation2d startRotation, Rotation2d endRotation,
                                  TrajectoryConfig config, Pose2d waypoints) {

    super(name, config, waypoints);

    mStartRot = startRotation;
    mEndRot = endRotation;
  }

  public Rotation2d getStartRotation() { return mStartRot; }
  public Rotation2d getEndRotation() { return mEndRot; }

}