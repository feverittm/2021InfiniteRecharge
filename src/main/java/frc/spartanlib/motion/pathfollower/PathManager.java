package frc.spartanlib.motion.pathfollower;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import frc.robot.util.Daemon;
import frc.spartanlib.motion.pathfollower.data.PathData;

import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

public class PathManager {

  private Daemon[] mDaemons;
  private ConcurrentHashMap<String, Trajectory> mProcessedData;
  private ConcurrentLinkedQueue<PathData> mRawData;

  private PathManager() {
    mDaemons = new Daemon[2];
    mRawData = new ConcurrentLinkedQueue<PathData>();
    mProcessedData = new ConcurrentHashMap<String, Trajectory>();
  }

  public void queueData(PathData dat) {
    mRawData.add(dat);
  }

  public void startDaemons() {
    for (int i = 0; i < mDaemons.length; i++) {
      mDaemons[i] = new Daemon("Daemon [" + i + "]", () -> processData());
      mDaemons[i].start();
    }
  }

  public void stopDaemons() {
    for (int i = 0; i < mDaemons.length; i++) {
      mDaemons[i].cancel();
    }
  }

  private void processData() {
    while (true) {
      if (mRawData.size() > 0) {
        PathData pd = mRawData.poll();
        try {
          if (pd != null) {
            Trajectory t = pd.processTrajectory();
            if (t != null) {
              mProcessedData.put(pd.getName(), t);
            }
          }
        } catch (Exception e) {
        }
      } else {
        try {
          Thread.sleep(250);
        } catch (Exception e) {
          try {
            Thread.currentThread().join();
          } catch (Exception e2) { }
        }
      }
    }
  }

  public Trajectory getTrajectory(String name) {
    return mProcessedData.get(name);
  }

  public static DifferentialDriveWheelSpeeds getDriveSpeeds(double trackWidth, Trajectory.State currentState) {
    return (new DifferentialDriveKinematics(trackWidth))
        .toWheelSpeeds(new ChassisSpeeds(currentState.velocityMetersPerSecond, 0,
            currentState.curvatureRadPerMeter * currentState.velocityMetersPerSecond));
  }

  public static double f2m(double feet) {
    return feet / 3.281;
  }

  public static double m2f(double meters) {
    return meters * 3.281;
  }

  private static PathManager instance;

  public static PathManager getInstance() {
    if (instance == null)
      instance = new PathManager();
    return instance;
  }

}