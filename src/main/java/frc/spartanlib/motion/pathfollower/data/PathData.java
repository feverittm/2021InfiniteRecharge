package frc.spartanlib.motion.pathfollower.data;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

public interface PathData {

  public Trajectory processTrajectory();

  public String getName();

}