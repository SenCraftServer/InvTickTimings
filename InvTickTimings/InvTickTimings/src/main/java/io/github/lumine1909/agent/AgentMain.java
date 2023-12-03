package io.github.lumine1909.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException {
        System.out.println("加载EntityPlayer hook中...");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                if (className.equals("net/minecraft/server/level/EntityPlayer")) {
                    ClassReader reader = new ClassReader(classfileBuffer);
                    ClassWriter writer = new ClassWriter(reader, 6);
                    ClassVisitor visitor = new EntityPlayerVisitor(className, writer);
                    reader.accept(visitor, 6);
                    System.out.println("hook加载完成");
                    return writer.toByteArray();
                }
                return null;
            }
        });
    }
}
