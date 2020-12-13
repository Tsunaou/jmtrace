package njuics.jmtrace.adapter;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;


public class MethodAdapter extends MethodVisitor implements Opcodes {
    /*
    A visitor to visit a Java method.
    The methods of this class must be called in the following order:
    ( visitParameter )*
    [ visitAnnotationDefault ]
    ( visitAnnotation | visitAnnotableParameterCount | visitParameterAnnotation visitTypeAnnotation | visitAttribute )*
    [ visitCode
    ( visitFrame | visit<i>X</i>Insn | visitLabel | visitInsnAnnotation | visitTryCatchBlock | visitTryCatchAnnotation | visitLocalVariable | visitLocalVariableAnnotation | visitLineNumber )*
    visitMaxs ]
    visitEnd.
    In addition, the visit<i>X</i>Insn and visitLabel methods must be called in the sequential order of the bytecode instructions of the visited code, visitInsnAnnotation must be called after the annotated instruction, visitTryCatchBlock must be called before the labels passed as arguments have been visited, visitTryCatchBlockAnnotation must be called after the corresponding try catch block has been visited, and the visitLocalVariable, visitLocalVariableAnnotation and visitLineNumber methods must be called after the labels passed as arguments have been visited.

     */
    protected String methodClassName;
    protected int methodAccess;
    protected String methodname;
    protected String methodDesc;
    protected String mehtodSignature;
    protected String[] methodExceptions;

    protected String curArrayType;
    protected String curOwner;
    protected String curObj;
    protected Boolean isStatic;

    public MethodAdapter(final MethodVisitor mv, final String mthodClassName, final int methodAccess, final String methodName,
                         final String methodDesc, final String methodSignature, final String[] methodExceptions) {
        super(ASM9, mv);
        this.methodClassName = mthodClassName;
        this.methodAccess = methodAccess;
        this.methodname = methodName;
        this.methodDesc = methodDesc;
        this.mehtodSignature = methodSignature;
        this.methodExceptions = methodExceptions;
        this.curArrayType = null;
        this.curOwner = null;
        this.curObj = null;
        this.isStatic = false;
        debugPrintln(toString());
    }
    
    public void debugPrintln(Object obj){
        boolean DEBUG = false;
        if(DEBUG){
            System.out.println(obj);
        }
    }

    public boolean isJavaLibary(String ownerName){
        if(ownerName.startsWith("sun/") || ownerName.startsWith("java/")){
            return true;
        }
        return false;
    }


    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        /*
        Visits a field instruction.
        A field instruction is an instruction that loads or stores the value of a field of an object.
        Parameters:
        opcode - the opcode of the type instruction to be visited.
                This opcode is either GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
        owner - the internal name of the field's owner class (see Type.getInternalName()).
        name - the field's name.
        descriptor - the field's descriptor (see Type).
         */
        if(isJavaLibary(owner) || isJavaLibary(methodClassName)){
            super.visitFieldInsn(opcode, owner, name, descriptor);
            return;
        }

        if(opcode >= GETSTATIC && opcode <=PUTFIELD && descriptor.startsWith("[")){
            if(opcode == GETSTATIC || opcode == PUTSTATIC){
                isStatic = true;
            }
            curArrayType = descriptor.substring(1).replace('/','.');
            curOwner = owner;
            curObj = owner.replace('/','.') + "." + name;
            debugPrintln("visitFieldInsn, isStatic is " + isStatic);
            debugPrintln("visitFieldInsn, ArrayType is " + curArrayType);
            debugPrintln("visitFieldInsn, ArrayOwner is " + curOwner);
            debugPrintln("visitFieldInsn, curObj is " + curObj);
        }

        switch (opcode){
            case GETSTATIC:
                debugPrintln("visitFieldInsn, OPCode is GETSTATIC");
                mv.visitLdcInsn(owner);
                mv.visitLdcInsn(name);
                mv.visitMethodInsn(
                        INVOKESTATIC,
                        "njuics/jmtrace/mtrace/MTrace",
                        "traceStaticRead",
                        "(Ljava/lang/String;Ljava/lang/String;)V",
                        false)
                ;

                break;
            case PUTSTATIC:
                debugPrintln("visitFieldInsn, OPCode is PUTSTATIC");
                mv.visitLdcInsn(owner);
                mv.visitLdcInsn(name);
                mv.visitMethodInsn(
                        INVOKESTATIC,
                        "njuics/jmtrace/mtrace/MTrace",
                        "traceStaticWrite",
                        "(Ljava/lang/String;Ljava/lang/String;)V",
                        false
                );
                break;
            case GETFIELD:
                // value, objref ->top
                debugPrintln("visitFieldInsn, OPCode is GETFIELD");
                mv.visitInsn(DUP);
                mv.visitLdcInsn(owner);
                mv.visitLdcInsn(name);
                mv.visitMethodInsn(
                        INVOKESTATIC,
                        "njuics/jmtrace/mtrace/MTrace",
                        "traceFieldRead",
                        "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V",
                        false
                );
                break;
            case PUTFIELD:
                // objref, value ->top
                debugPrintln("visitFieldInsn, OPCode is PUTFIELD");
                if(Type.getType(descriptor).getSize() == 1){
                    mv.visitInsn(SWAP);
                    // value, objref ->top
                    mv.visitInsn(DUP);
                    // value, objref, objref ->top
                    mv.visitLdcInsn(owner);
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(
                            INVOKESTATIC,
                            "njuics/jmtrace/mtrace/MTrace",
                            "traceFieldWrite",
                            "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V",
                            false
                    );
                    // value, objref ->top
                    mv.visitInsn(SWAP);
                    // objref, value ->top
                }else{
                    mv.visitInsn(DUP2_X1);
                    // value, objref, value ->top
                    mv.visitInsn(POP2);
                    // value, objref ->top
                    mv.visitInsn(DUP);
                    // value, objref, objref ->top
                    mv.visitLdcInsn(owner);
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(
                            INVOKESTATIC,
                            "njuics/jmtrace/mtrace/MTrace",
                            "traceFieldWrite",
                            "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V",
                            false
                    );
                    // value, objref ->top
                    mv.visitInsn(DUP_X2);
                    // objref, value, objref ->top
                    mv.visitInsn(POP);
                }

                break;
            default:
                break;
        }

        if(opcode >= GETSTATIC && opcode <=PUTFIELD){
            debugPrintln(toString());
            debugPrintln("visitFieldInsn{" +
                    "owner=" + owner +
                    ", name='" + name + '\'' +
                    ", descriptor='" + descriptor + '\'' +
                    '}');
            debugPrintln("------------------------------------------------");
        }

        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitInsn(int opcode) {
        if(curOwner == null || curOwner.equals("")){
            super.visitInsn(opcode);
            return;
        }
        // Visits a zero operand instruction.
        if(opcode>=IALOAD && opcode <=SALOAD){
            // arrayref, index -> top
            debugPrintln("visitInsn, OPCode is *LOAD");
            debugPrintln(toString());
//            MTrace.traceArrayRead(0,new Object(), arrayType);
            mv.visitInsn(DUP2);
            // arrayref, index, arrayref, index -> top
            mv.visitInsn(SWAP);
            // arrayref, index, index, arrayref -> top
            mv.visitLdcInsn(curArrayType);
            mv.visitMethodInsn(
                    INVOKESTATIC,
                    "njuics/jmtrace/mtrace/MTrace",
                    "traceArrayRead",
                    "(ILjava/lang/Object;Ljava/lang/String;)V",
                    false
            );

        }else if(opcode>=IASTORE && opcode <=SASTORE){
            // arrayref, index, value ->top
            debugPrintln("visitInsn, OPCode is *STORE");
            debugPrintln(toString());
            if(opcode == LASTORE || opcode == DASTORE){
                // value of size 2
                // arrayref, index, value ->top
                mv.visitInsn(DUP2_X2);
                // value, arrayref, index, value ->top
                mv.visitInsn(POP2);
                // value, arrayref, index ->top
                mv.visitInsn(DUP2);
                // value, arrayref, index, arrayref, index ->top
                mv.visitInsn(SWAP);
                // value, arrayref, index, index, arrayref ->top
                mv.visitLdcInsn(curArrayType);
                mv.visitMethodInsn(
                        INVOKESTATIC,
                        "njuics/jmtrace/mtrace/MTrace",
                        "traceArrayRead",
                        "(ILjava/lang/Object;Ljava/lang/String;)V",
                        false
                );
                // value, arrayref, index ->top
                mv.visitInsn(DUP2_X2);
                // arrayref, index, value, arrayref, index ->top
                mv.visitInsn(POP2);
                // arrayref, index, value ->top
            }else {
                // value of size 1
                // arrayref, index, value ->top
                mv.visitInsn(DUP_X2);
                // value, arrayref, index, value ->top
                mv.visitInsn(POP);
                // value, arrayref, index ->top
                mv.visitInsn(DUP2);
                // value, arrayref, index, arrayref, index ->top
                mv.visitInsn(SWAP);
                // value, arrayref, index, index, arrayref ->top
                mv.visitLdcInsn(curArrayType);
                mv.visitMethodInsn(
                        INVOKESTATIC,
                        "njuics/jmtrace/mtrace/MTrace",
                        "traceArrayRead",
                        "(ILjava/lang/Object;Ljava/lang/String;)V",
                        false
                );
                // value, arrayref, index ->top
                mv.visitInsn(DUP2_X1);
                // arrayref, index, value, arrayref, index ->top
                mv.visitInsn(POP2);
                // arrayref, index, value ->top
            }

            // arrayref, index, value ->top


        }else {
            super.visitInsn(opcode);
            return;
        }

        debugPrintln("------------------------------------------------");
        super.visitInsn(opcode);

    }

    @Override
    public String toString() {
        return "MethodAdapter{" +
                "methodClassName='" + methodClassName + '\'' +
                ", methodAccess=" + methodAccess +
                ", methodname='" + methodname + '\'' +
                ", methodDesc='" + methodDesc + '\'' +
                ", mehtodSignature='" + mehtodSignature + '\'' +
                ", methodExceptions=" + Arrays.toString(methodExceptions) +
                '}';
    }

}