package io.github.lumine1909.agent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InvTickAdapter extends MethodVisitor {
    int count = 0;
    public InvTickAdapter(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }


    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        if (opcode == Opcodes.ALOAD && varIndex == 0) {
            count++;
            if (count == 9) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                super.visitVarInsn(Opcodes.LSTORE, 1);
            }
            if (count == 11) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                super.visitVarInsn(Opcodes.LLOAD, 1);
                super.visitInsn(Opcodes.LSUB);
                super.visitVarInsn(Opcodes.LSTORE, 3);
                super.visitFieldInsn(Opcodes.GETSTATIC, "net/minecraft/server/level/EntityPlayer", "lastTickMap", "Ljava/util/Map;");
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/server/level/EntityPlayer", "getBukkitEntity", "()Lorg/bukkit/craftbukkit/v1_19_R3/entity/CraftHumanEntity;", false);
                super.visitVarInsn(Opcodes.LLOAD, 3);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
                super.visitInsn(Opcodes.POP);
            }
        }
        super.visitVarInsn(opcode, varIndex);
    }
}
