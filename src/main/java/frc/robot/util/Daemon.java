package frc.robot.util;

public class Daemon {

  private Runnable mExec;
  private Thread mThread;

  /**
   * A daemon to process, then wait for data
   * 
   * @param exec What to do every update
   */
  public Daemon(String name, Runnable exec) {
    mExec = exec;
    mThread = new Thread(mExec);
    mThread.setName(name);
  }

  public void start() {
    mThread.start();
  }

  public boolean cancel() {
    try {
      mThread.join(100);
      return true;
    } catch (Exception e) { return false; }
  }

}