package frc.spartanlib.commands;

import frc.spartanlib.helpers.threading.SpartanAction;
import frc.spartanlib.swerve.module.SwerveModule;

import edu.wpi.first.wpilibj2.command.Subsystem;

public class UpdateModule extends SpartanAction {

  // private final int ALIGNMENT_TIMEOUT = 1250; // Milliseconds until I start complaining
  // private final double ALIGNMENT_TOLERANCE = 2.5; // Tolerance in degrees

  private SwerveModule mod;

  // private double lastTargetAngle = 0;
  // private double lastGoodAlignment;

  public UpdateModule(SwerveModule module, Subsystem s) {
    mod = module;
  }

  public UpdateModule(SwerveModule module) {
    mod = module;
  }

  @Override
  protected void init() {
    //if (mod instanceof ProtoModule) {
      //((ProtoModule)mod).resetAzimuthController();
    //}
    // lastGoodAlignment = System.currentTimeMillis();
  }

  @Override
  protected void exec() {
    mod.update();
  }

  @Override
  protected boolean isDone() {
    return false;
  }

  @Override
  protected void interrupt() {
    System.out.println("[" + mod.mID + "] Interrupted");
  }

}
