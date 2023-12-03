package io.github.lumine1909.agent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InvClInitAdapter extends MethodVisitor {
    protected InvClInitAdapter(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN || opcode == Opcodes.ARETURN) {
            super.visitTypeInsn(Opcodes.NEW, "java/util/HashMap");
            super.visitInsn(Opcodes.DUP);
            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
            super.visitFieldInsn(Opcodes.PUTSTATIC, "net/minecraft/server/level/EntityPlayer", "lastTickMap", "Ljava/util/Map;");
        }
        super.visitInsn(opcode);
    }
}
