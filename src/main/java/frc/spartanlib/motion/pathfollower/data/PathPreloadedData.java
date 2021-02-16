package frc.spartanlib.motion.pathfollower.data;

import java.nio.file.Path;
import java.nio.file.Paths;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;

public class PathPreloadedData implements PathData {

  private String mName;
  private Path mFilePath;

  public PathPreloadedData(String name) {
    mName = name;
    mFilePath = Paths.get("/home/lvuser/deploy/paths/output/" + name + ".wpilib.json");
  }

  @Override
  public Trajectory processTrajectory() {
    try {
      return TrajectoryUtil.fromPathweaverJson(mFilePath);
    } catch (Exception e) { return null; }
  }

  @Override
  public String getName() {
    return mName;
  }

}