package zinsoft.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppPropertyUtil {

    private static Map<String, String> properties = new ConcurrentHashMap<>();

    private AppPropertyUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String get(String propId) {
        return properties.get(propId);
    }

    public static void set(String propId, String propVal) {
        properties.put(propId, propVal);
    }

    public static void setAll(Map<String, String> p) {
        properties.putAll(p);
    }

    public static void clear() {
        properties.clear();
    }

}