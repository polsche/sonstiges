package de.ba.bub.studisu.test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertTrue;

/**
 * Klasse mit Hilfsmethoden fuer Testklassen
 * @author dauminn
 */
public class TestUtils {
    
    /**
     * allgemeine Testmethode fuer private Konstruktoren in Utility-Klassen, die UnsupportedOperationException werfen
     * @param clazz Klasse, deren privater Konstruktor getestet werden soll
     * @throws NoSuchMethodException moeglicher Fehler bei Reflection-Aufruf
     * @throws IllegalAccessException moeglicher Fehler bei Reflection-Aufruf
     * @throws InstantiationException moeglicher Fehler bei Reflection-Aufruf
     * @throws InvocationTargetException moeglicher Fehler bei Reflection-Aufruf
     */
    public void callUtilityClassConstructor(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, 
            InstantiationException, InvocationTargetException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        assertTrue("privater Konstruktor in Klasse " + clazz.getName(), Modifier.isPrivate(constructor.getModifiers()));
 
        constructor.setAccessible(true);
        constructor.newInstance();
    }
    
}