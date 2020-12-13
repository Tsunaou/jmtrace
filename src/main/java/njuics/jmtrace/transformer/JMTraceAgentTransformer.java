package njuics.jmtrace.transformer;

import njuics.jmtrace.adapter.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class JMTraceAgentTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader classLoader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
//        System.out.println("Transforming " + className);
        /*
        * TODO: 修改字节码，并返回修改后的字节码
        *  */
        byte[] transformed = null;
        try {
            ClassReader cr = new ClassReader(new java.io.ByteArrayInputStream(classfileBuffer));
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassAdapter ca = new ClassAdapter(cw);
            cr.accept(ca, ClassReader.EXPAND_FRAMES);
            transformed = cw.toByteArray();
        }catch (RuntimeException re){
            re.printStackTrace();
        }catch (IOException e) {
            System.err.println("Can't transform "+ className+"  "+e);
            e.printStackTrace();
        }
        return transformed;


    }
}
