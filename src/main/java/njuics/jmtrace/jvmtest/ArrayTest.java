package njuics.jmtrace.jvmtest;

public class ArrayTest {

    static class A{
        public int x;

        public A(int x) {
            this.x = x;
        }
    }

    public static void main(String[] args) {
        A a = new A(1);

        int[] arr = new int[2];
        arr[0] = a.x;
    }
}
