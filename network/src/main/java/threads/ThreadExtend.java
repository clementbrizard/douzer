package threads;

/**
 * Abstract class that only adds a method 'kill' to the Thread class. The method 'kill'
 * should be used to stop properly a thread.
 */
public abstract class ThreadExtend extends Thread {
  public abstract void kill();
}
