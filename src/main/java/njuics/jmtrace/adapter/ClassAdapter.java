package njuics.jmtrace.adapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassAdapter extends ClassVisitor implements Opcodes {
    /*
    A visitor to visit a Java class.
    The methods of this class must be called in the following order:
    visit [ visitSource ][ visitModule ][ visitNestHost ][ visitPermittedclass ][ visitOuterClass ]
    ( visitAnnotation | visitTypeAnnotation | visitAttribute )*
    ( visitNestMember | visitInnerClass | visitRecordComponent | visitField | visitMethod )*
    visitEnd.
    * */

    private String owner;
    private boolean isInterface;

    public ClassAdapter(final ClassVisitor cv) {
        super(ASM9, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        /*See https://asm.ow2.io/javadoc/org/objectweb/asm/ClassVisitor.html*/
        cv.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if (!isInterface && mv != null && !"<init>".equals(name) && !"<clinit>".equals(name)) {
            mv = new MethodAdapter(mv, owner, access, name, desc, signature, exceptions);
//            mv = new MTraceAdapter(ASM9, mv, access, name, desc);
        }
        return mv;
    }
}
