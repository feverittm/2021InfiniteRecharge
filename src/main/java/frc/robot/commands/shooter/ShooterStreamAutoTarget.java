package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Limelight;
import frc.spartanlib.math.MathUtils;

public class ShooterStreamAutoTarget extends CommandBase {

  private double mTarget = 0.0;
  //private long mLastUpdate;

  public ShooterStreamAutoTarget(){
    mTarget = 0.8;
  }

  public ShooterStreamAutoTarget(double speed) {
    mTarget = speed;
  }

  @Override
  public void initialize() {
    //mLastUpdate = System.currentTimeMillis();
  }

  @Override
  public void execute() {
    // double deltaT = System.currentTimeMillis() - mLastUpdate;
    // double output = LimeLight.getInstance().getPIDOutput(deltaT);
    // DriveTrain.getInstance().setMotors(-output, output);

    Shooter.getInstance().setRPM(mTarget);
    if (MathUtils.epsilon(Shooter.getInstance().getRPMs(), mTarget, 80) && Hopper.getInstance().mBallCount > 0
        && Math.abs(Limelight.getInstance().getTX()) < Constants.Values.VISION_ANGLE_TOLERANCE) {
      Hopper.getInstance().setSpeed(Constants.Values.HOPPER_STREAM_SPEED);
    } else {
      Hopper.getInstance().setSpeed(0.0);
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    Shooter.getInstance().GoodStop();
    Hopper.getInstance().setSpeed(0.0);
    //DriveTrain.getInstance().setMotors(0.0, 0.0);
  }

}