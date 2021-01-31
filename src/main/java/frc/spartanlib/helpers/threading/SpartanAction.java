package frc.spartanlib.helpers.threading;

import edu.wpi.first.wpilibj2.command.CommandBase;

public abstract class SpartanAction extends CommandBase {

  public boolean hasInit = false;
  public boolean ended = false;

  protected abstract void exec();
  protected abstract boolean isDone();
  protected abstract void interrupt();
  protected abstract void init();

  @Override
  public void initialize() {
    init();
  }

  @Override
  public void execute() {
    exec();
  }

  @Override
  public boolean isFinished() {
    return isDone();
  }
  
  @Override
  public void end(boolean interrupted) {
    interrupt();
    ended = true;
  }

}
