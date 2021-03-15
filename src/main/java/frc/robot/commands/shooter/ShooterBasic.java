package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShooterBasic extends CommandBase {

  private double mSpeed = 0.0;

  public ShooterBasic(double speed) {
    mSpeed = speed;
    addRequirements(Shooter.getInstance());
  }

  @Override
  public void execute() {
    Shooter.getInstance().SetShooterPercent(mSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    Shooter.getInstance().SetShooterPercent(0.0);
  }

}