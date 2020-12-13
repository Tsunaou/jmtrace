package njuics.jmtrace.jvmtest;

public class JVMTester {

    static int static_filed = 0;
    long field;

    public JVMTester(long field) {
        this.field = field;
    }

    public static void main(String[] args) {
        long filed = 2333;
        System.out.println(
                "W " +  Thread.currentThread().getId() + " " + System.identityHashCode(filed) + " " + filed
        );

        JVMTester tester = new JVMTester(filed);
        // get_static
        int x = JVMTester.static_filed;
        System.out.println(
                "R " +  Thread.currentThread().getId() + " " + System.identityHashCode(JVMTester.static_filed) + " " + JVMTester.static_filed
        );

        // put_static
        JVMTester.static_filed = 1;
        System.out.println(
                "W " +  Thread.currentThread().getId() + " " + System.identityHashCode(JVMTester.static_filed) + " " + JVMTester.static_filed
        );

        // get_field
        long y = tester.field;
        System.out.println(
                "R " +  Thread.currentThread().getId() + " " + System.identityHashCode(tester.field) + " " + tester.field
        );
        // put_field
        tester.field = 666;
        System.out.println(
                "W " +  Thread.currentThread().getId() + " " + System.identityHashCode(tester.field) + " " + tester.field
        );

        int[] a = new int [10];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
            System.out.println(
                    "W " +  Thread.currentThread().getId() + " " + System.identityHashCode(a[i]) + " " + a[i]
            );

        }


    }
}
