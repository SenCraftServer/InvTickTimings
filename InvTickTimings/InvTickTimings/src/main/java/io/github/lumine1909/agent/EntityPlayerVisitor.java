package io.github.lumine1909.agent;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class EntityPlayerVisitor extends ClassVisitor {
    String name;

    public EntityPlayerVisitor(String className, ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
        name = className;
        cv.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "lastTickMap", "Ljava/util/Map;", "Ljava/util/Map<Lorg/bukkit/entity/Player;Ljava/lang/Long;>;", null).visitEnd();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && name.equals("l") && desc.equals("()V")) {
            mv = new InvTickAdapter(mv);
        }
        if (mv != null && name.equals("<clinit>") && desc.equals("()V")) {
            mv = new InvClInitAdapter(mv);
        }
        return mv;
    }
}
