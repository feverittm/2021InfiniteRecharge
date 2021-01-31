package frc.spartanlib.helpers.threading;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SpartanRunner {

  private boolean halt = false;
  private int loopFrequency;
  private double lastRun = 0;
  private Thread t;
  private ArrayList<SpartanAction> actions;

  public static Lock threadLock;

  public SpartanRunner(int loopFrequency) {

    threadLock = new ReentrantLock();

    this.loopFrequency = loopFrequency;
    actions = new ArrayList<SpartanAction>();

    t = new Thread(this::Run);
    t.start();
  }

  public static boolean LockThread() {
    if (threadLock == null) return false;

    threadLock.lock();

    return true;
  }

  public static boolean UnlockThread() {
    if (threadLock == null) return false;

    threadLock.unlock();

    return true;
  }

  private void Run() {
    lastRun = System.currentTimeMillis();
    while (!halt) {
      if (lastRun + loopFrequency < System.currentTimeMillis()) {
        lastRun = System.currentTimeMillis();
        if (actions.size() > 0) {
          LockThread();
          actions.forEach(x -> {

            if (!x.hasInit) {
              x.init();
              x.hasInit = true;
            }
            x.exec();
            if (x.isDone()) {
              x.interrupt(); x.ended = true;
            }

          });
          actions.removeIf(x -> x.ended);
          UnlockThread();
        }
      }

      double sleepTime = (lastRun + loopFrequency) - System.currentTimeMillis();
      sleepTime = sleepTime < 0 ? 0 : sleepTime;
      try { Thread.sleep((long)sleepTime); } catch (Exception e) { e.printStackTrace(); }
    }

    actions.forEach(x -> x.interrupt());
    actions.clear();
  }

  public void Stop() {
    halt = true;
  }

  public void AddAction(SpartanAction action) {
    actions.add(action);
  }

}
