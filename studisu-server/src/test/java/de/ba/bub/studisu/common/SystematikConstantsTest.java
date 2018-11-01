package de.ba.bub.studisu.common;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import de.ba.bub.studisu.test.TestUtils;

public class SystematikConstantsTest {

	/**
     * Test fuer privaten Konstruktor
     *
     * @throws NoSuchMethodException moeglicher Fehler bei Konstruktoraufruf per Reflection
     * @throws IllegalAccessException moeglicher Fehler bei Konstruktoraufruf per Reflection
     * @throws InstantiationException moeglicher Fehler bei Konstruktoraufruf per Reflection
     * @throws InvocationTargetException moeglicher Fehler bei Reflection-Aufruf
     */
    @Test
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException,
            InvocationTargetException {
        new TestUtils().callUtilityClassConstructor(SystematikConstants.class);
    }

}
