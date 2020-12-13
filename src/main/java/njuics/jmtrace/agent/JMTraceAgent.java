package njuics.jmtrace.agent;

import njuics.jmtrace.transformer.JMTraceAgentTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class JMTraceAgent {
    static private Instrumentation _inst = null;

    public static void premain(String agentArgs, Instrumentation inst){
//        System.out.println("AopAgentTest.premain() was called.");

        /* Provides services that allow Java programming language agents to instrument programs running on the JVM.*/
        _inst = inst;
        /* ClassFileTransformer :
        An agent provides an implementation of this interface in order to transform class files.*/
        ClassFileTransformer trans = new JMTraceAgentTransformer();

        /*Registers the supplied transformer.*/
        _inst.addTransformer(trans);
    }
}
