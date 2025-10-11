package fr.uvsq.cprog.collex;

import org.junit.Test;
import static org.junit.Assert.*;

public class NomMachineApp {
    @Test
    public void testToString() {
        NomMachine machine = new NomMachine("ww.google.com");
        assertEquals(machine.toString(), "ww.google.com");
    }
}
