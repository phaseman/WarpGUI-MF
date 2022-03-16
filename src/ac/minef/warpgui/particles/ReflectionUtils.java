package ac.minef.warpgui.particles;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtils {
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        Constructor[] arrayOfConstructor;
        for (int length = (arrayOfConstructor = (Constructor[])clazz.getConstructors()).length, i = 0; i < length; i++) {
            Constructor<?> constructor = arrayOfConstructor[i];
            if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes))
                return constructor;
        }
        throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter types");
    }

    public static Constructor<?> getConstructor(String className, PackageType packageType, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getConstructor(packageType.getClass(className), parameterTypes);
    }

    public static Object instantiateObject(Class<?> clazz, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
    }

    public static Object instantiateObject(String className, PackageType packageType, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return instantiateObject(packageType.getClass(className), arguments);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        Method[] methods;
        for (int length = (methods = clazz.getMethods()).length, i = 0; i < length; i++) {
            Method method = methods[i];
            if (method.getName().equals(methodName) && DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes))
                return method;
        }
        throw new NoSuchMethodException("There is no such method in this class with the specified name and parameter types");
    }

    public static Method getMethod(String className, PackageType packageType, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getMethod(packageType.getClass(className), methodName, parameterTypes);
    }

    public static Object invokeMethod(Object instance, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    public static Object invokeMethod(Object instance, Class<?> clazz, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    public static Object invokeMethod(Object instance, String className, PackageType packageType, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return invokeMethod(instance, packageType.getClass(className), methodName, arguments);
    }

    public static Field getField(Class<?> clazz, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException {
        Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Field getField(String className, PackageType packageType, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
        return getField(packageType.getClass(className), declared, fieldName);
    }

    public static Object getValue(Object instance, Class<?> clazz, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return getField(clazz, declared, fieldName).get(instance);
    }

    public static Object getValue(Object instance, String className, PackageType packageType, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        return getValue(instance, packageType.getClass(className), declared, fieldName);
    }

    public static Object getValue(Object instance, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return getValue(instance, instance.getClass(), declared, fieldName);
    }

    public static void setValue(Object instance, Class<?> clazz, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        getField(clazz, declared, fieldName).set(instance, value);
    }

    public static void setValue(Object instance, String className, PackageType packageType, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        setValue(instance, packageType.getClass(className), declared, fieldName, value);
    }

    public static void setValue(Object instance, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        setValue(instance, instance.getClass(), declared, fieldName, value);
    }

    public enum DataType {
        BYTE(byte.class, Byte.class),
        SHORT(short.class, Short.class),
        INTEGER(int.class, Integer.class),
        LONG(long.class, Long.class),
        CHARACTER(char.class, Character.class),
        FLOAT(float.class, Float.class),
        DOUBLE(double.class, Double.class),
        BOOLEAN(boolean.class, Boolean.class);

        private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<>();

        private final Class<?> primitive;

        private final Class<?> reference;

        static {
            DataType[] values;
            for (int length = (values = values()).length, i = 0; i < length; i++) {
                DataType type = values[i];
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }

        DataType(Class<?> primitive, Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }

        public Class<?> getPrimitive() {
            return this.primitive;
        }

        public Class<?> getReference() {
            return this.reference;
        }

        public static DataType fromClass(Class<?> clazz) {
            return CLASS_MAP.get(clazz);
        }

        public static Class<?> getPrimitive(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return (type == null) ? clazz : type.getPrimitive();
        }

        public static Class<?> getReference(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return (type == null) ? clazz : type.getReference();
        }

        public static Class<?>[] getPrimitive(Class<?>[] classes) {
            int length = (classes == null) ? 0 : classes.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++)
                types[index] = getPrimitive(classes[index]);
            return types;
        }

        public static Class<?>[] getReference(Class<?>[] classes) {
            int length = (classes == null) ? 0 : classes.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++)
                types[index] = getReference(classes[index]);
            return types;
        }

        public static Class<?>[] getPrimitive(Object[] objects) {
            int length = (objects == null) ? 0 : objects.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++)
                types[index] = getPrimitive(objects[index].getClass());
            return types;
        }

        public static Class<?>[] getReference(Object[] objects) {
            int length = (objects == null) ? 0 : objects.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++)
                types[index] = getReference(objects[index].getClass());
            return types;
        }

        public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
            if (primary == null || secondary == null || primary.length != secondary.length)
                return false;
            for (int index = 0; index < primary.length; index++) {
                Class<?> primaryClass = primary[index];
                Class<?> secondaryClass = secondary[index];
                if (!primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass))
                    return false;
            }
            return true;
        }
    }

    public enum PackageType {
        MINECRAFT_SERVER("MINECRAFT_SERVER", 0, "net.minecraft.server." + getServerVersion()),
        CRAFTBUKKIT("CRAFTBUKKIT", 1, "org.bukkit.craftbukkit." + getServerVersion());/*,
        CRAFTBUKKIT_BLOCK("CRAFTBUKKIT_BLOCK", 2, (String)CRAFTBUKKIT, "block"),
        CRAFTBUKKIT_CHUNKIO("CRAFTBUKKIT_CHUNKIO", 3, (String)CRAFTBUKKIT, "chunkio"),
        CRAFTBUKKIT_COMMAND("CRAFTBUKKIT_COMMAND", 4, (String)CRAFTBUKKIT, "command"),
        CRAFTBUKKIT_CONVERSATIONS("CRAFTBUKKIT_CONVERSATIONS", 5, (String)CRAFTBUKKIT, "conversations"),
        CRAFTBUKKIT_ENCHANTMENS("CRAFTBUKKIT_ENCHANTMENS", 6, (String)CRAFTBUKKIT, "enchantments"),
        CRAFTBUKKIT_ENTITY("CRAFTBUKKIT_ENTITY", 7, (String)CRAFTBUKKIT, "entity"),
        CRAFTBUKKIT_EVENT("CRAFTBUKKIT_EVENT", 8, (String)CRAFTBUKKIT, "event"),
        CRAFTBUKKIT_GENERATOR("CRAFTBUKKIT_GENERATOR", 9, (String)CRAFTBUKKIT, "generator"),
        CRAFTBUKKIT_HELP("CRAFTBUKKIT_HELP", 10, (String)CRAFTBUKKIT, "help"),
        CRAFTBUKKIT_INVENTORY("CRAFTBUKKIT_INVENTORY", 11, (String)CRAFTBUKKIT, "inventory"),
        CRAFTBUKKIT_MAP("CRAFTBUKKIT_MAP", 12, (String)CRAFTBUKKIT, "map"),
        CRAFTBUKKIT_METADATA("CRAFTBUKKIT_METADATA", 13, (String)CRAFTBUKKIT, "metadata"),
        CRAFTBUKKIT_POTION("CRAFTBUKKIT_POTION", 14, (String)CRAFTBUKKIT, "potion"),
        CRAFTBUKKIT_PROJECTILES("CRAFTBUKKIT_PROJECTILES", 15, (String)CRAFTBUKKIT, "projectiles"),
        CRAFTBUKKIT_SCHEDULER("CRAFTBUKKIT_SCHEDULER", 16, (String)CRAFTBUKKIT, "scheduler"),
        CRAFTBUKKIT_SCOREBOARD("CRAFTBUKKIT_SCOREBOARD", 17, (String)CRAFTBUKKIT, "scoreboard"),
        CRAFTBUKKIT_UPDATER("CRAFTBUKKIT_UPDATER", 18, (String)CRAFTBUKKIT, "updater"),
        CRAFTBUKKIT_UTIL("CRAFTBUKKIT_UTIL", 19, (String)CRAFTBUKKIT, "util");*/

        private final String path;

        PackageType(String s, int n, String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }

        public Class<?> getClass(String className) throws ClassNotFoundException {
            return Class.forName(this + "." + className);
        }

        public String toString() {
            return this.path;
        }

        public static String getServerVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }
    }
}