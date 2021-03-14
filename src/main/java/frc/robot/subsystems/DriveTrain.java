package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import frc.spartanlib.commands.UpdateModule;
import frc.spartanlib.helpers.SwerveMixerData;
import frc.spartanlib.helpers.threading.SpartanRunner;
import frc.spartanlib.motion.pathfollower.PathManager;
import frc.spartanlib.swerve.SwerveDrive;
import frc.spartanlib.swerve.module.SwerveModule;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.swerve.module.TeslaModule;

public class DriveTrain extends SwerveDrive {

  private AHRS navx;

  private DriveTrain() {
    super(Constants.Values.WHEEL_BASE, Constants.Values.TRACK_WIDTH);

    navx = new AHRS(Port.kUSB);
    resetGyro();

    mModules = new SwerveModule[4];
    for (int i = 0; i < 4; i++) {
      mModules[i] = new TeslaModule(i, Constants.Ports.AZIMUTH_PORTS[i], Constants.Ports.DRIVE_PORTS[i],
          Constants.Ports.MODULE_ENCODERS[i], Constants.Values.MODULE_ZEROS[i], Constants.Values.AZIMUTH_GAINS[i],
          Constants.Values.DRIVE_GAINS[i]);
    }

    for (int i = 0; i < mModules.length; i++) {
      mModules[i].setDriveBrakeMode(true);
      SpartanRunner.LockThread();
      Robot.mRunner.AddAction(new UpdateModule(mModules[i], this));
      SpartanRunner.UnlockThread();
    }
  }

  public double getFullAngle() {
    return navx.getFusedHeading();
  }

  public double getGyroAngle() {
    return navx.getYaw();
  }

  public void resetGyro() {
    navx.reset();
  }

  @Override
  public void periodic() {
    updateSmartDashboard();
  }

  public void updateSmartDashboard() {
    SmartDashboard.putNumber("Drivetrain/Gyro Angle", getFullAngle());
    SmartDashboard.putNumber("Drivetrain/Module 1 Angle", mModules[0].getAngle());
    SmartDashboard.putNumber("Drivetrain/Module 2 Angle", mModules[1].getAngle());
    SmartDashboard.putNumber("Drivetrain/Module 3 Angle", mModules[2].getAngle());
    SmartDashboard.putNumber("Drivetrain/Module 4 Angle", mModules[3].getAngle());
  }

  public void setCoast() {
    for (int i = 0; i < mModules.length; i++) {
      mModules[i].setDriveBrakeMode(false);
    }
  }

  public void setBrake() {
    for (int i = 0; i < mModules.length; i++) {
      mModules[i].setDriveBrakeMode(true);
    }
  }

  public SwerveMixerData toSwerveMixerData(SwerveModuleState[] moduleStates) {
    SwerveMixerData dat = new SwerveMixerData();
    double[] angles = new double[4], speeds = new double[4];
    for (int i = 0; i < 4; i++) {
      angles[i] = moduleStates[i].angle.getDegrees();
      speeds[i] = PathManager.m2f(moduleStates[i].speedMetersPerSecond);
    }
    dat.setAngles(angles);
    dat.setSpeeds(speeds);
    return dat;
  }

  private static DriveTrain instance;

  public static DriveTrain getInstance() {
    if (instance == null) {
      instance = new DriveTrain();
    }
    return instance;
  }
}