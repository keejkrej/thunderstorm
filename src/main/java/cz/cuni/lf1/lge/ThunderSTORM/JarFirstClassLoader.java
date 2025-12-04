package cz.cuni.lf1.lge.ThunderSTORM;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A classloader that tries to load a class from the same jar before delegating
 * to parent classloader.
 */
public class JarFirstClassLoader extends URLClassLoader {

    private static JarFirstClassLoader instance = new JarFirstClassLoader();

    private JarFirstClassLoader() {
        super(new URL[]{JarFirstClassLoader.class.getProtectionDomain().getCodeSource().getLocation()}, JarFirstClassLoader.class.getClassLoader());
    }

    public static JarFirstClassLoader getInstance() {
        return instance;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // Skip internal Sun classes that were removed in Java 9+
        if (name.startsWith("com.sun.net.ssl.internal.") || 
            name.startsWith("sun.net.www.protocol.https.")) {
            throw new ClassNotFoundException(name + " (removed in Java 9+)");
        }
        
        Class<?> c = findLoadedClass(name);
        if(c == null) {
            try {
                c = findClass(name);
                return c;
            } catch(ClassNotFoundException ex) {
                return super.loadClass(name);
            }
        } else {
            return c;
        }
    }

}
