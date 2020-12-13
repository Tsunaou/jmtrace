package njuics.jmtrace.mtrace;

public class MTrace {

    public static void log(String optype, int tId, long objId, String target){
        System.out.printf("%s %d %016x %s\n", optype, tId, objId, target);
    }

    public static void traceStaticRW(String rw, String className, String name){
        StringBuilder builder = new StringBuilder()
                .append(className.replace('/','.'))
                .append('.')
                .append(name);
        String obj = builder.toString();
        log(rw, (int) Thread.currentThread().getId(), System.identityHashCode(obj), obj);
    }

    public static void traceFieldRW(String rw, Object obj, String className, String name){
        StringBuilder builder = new StringBuilder()
                .append(className.replace('/','.'))
                .append('.')
                .append(name);
        String target = builder.toString();
        log(rw, (int) Thread.currentThread().getId(), System.identityHashCode(obj), target);
    }

    public static void traceArrayRW(String rw, int index, Object obj, String type){
//        System.out.println("index is " + index + ", obj is " + obj + ", type is " + type);
        StringBuilder builder = new StringBuilder()
                .append(type)
                .append('[')
                .append(index)
                .append(']');
        String target = builder.toString();
        log(rw, (int) Thread.currentThread().getId(), System.identityHashCode(obj)+index, target);
    }

    public static void traceStaticRead(String className, String name){
        traceStaticRW("R", className, name);
    }

    public static void traceStaticWrite(String className, String name){
        traceStaticRW("W", className, name);
    }

    public static void traceFieldRead(Object obj, String className, String name){
        traceFieldRW("R", obj, className, name);
    }

    public static void traceFieldWrite(Object obj, String className, String name){
        traceFieldRW("W", obj, className, name);
    }

    public static void traceArrayRead(int index, Object obj, String type){
        traceArrayRW("R",index, obj, type);
    }

    public static void traceArrayWrite(int index, Object obj, String type){
        traceArrayRW("W",index, obj, type);
    }
}


