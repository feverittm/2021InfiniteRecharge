package frc.spartanlib.motion.pathfollower.data;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

public class PathGeneratedData implements PathData {

  private String mName;
  private List<Pose2d> mWaypoints;
  private TrajectoryConfig mConfig;

  public PathGeneratedData( String name, TrajectoryConfig config, Pose2d... waypoints) {
    mName = name;
    mConfig = config;
    mWaypoints = Arrays.asList(waypoints);
  }

  @Override
  public Trajectory processTrajectory() {
    return TrajectoryGenerator.generateTrajectory(mWaypoints, mConfig);
  }

  @Override
  public String getName() {
    return mName;
  }

}