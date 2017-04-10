package ru.otus.agent;

import java.lang.instrument.Instrumentation;

/**
 * Instrumentation agent for measuring object size in memory
 */
public class Agent {
    private static volatile Instrumentation globalInstr;

    public static void premain(String args, Instrumentation instr) {
        globalInstr = instr;
    }

    /**
     * Get size of an object in memory.
     * @param obj which size should be measured
     * @return the size of an object in bytes
     * @throws IllegalStateException if agent was not initialised
     */
    public static long getObjectSize(Object obj) {
        if(globalInstr == null)
            throw new IllegalStateException("Agent was not initialised.");
        return globalInstr.getObjectSize(obj);
    }
}
