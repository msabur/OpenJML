public class Assigns {

  public static class T {
    public int v;
    public int w;
  }

  public Assigns() {
    t1 = new T();
    t2 = new T();
    a = new int[10];
  }

  public T t1;
  public T t2;
  public int x;
  public int[] a;

  //@ requires t1 != t2;
  //@ requires a.length == 10;
  //@ writes a[3..5];
  public void m5() {
    x = 0; // ERROR
    t1.v = 0; // ERROR
    a[4] = 0;
    a[1] = 0; // ERROR
  }
}
