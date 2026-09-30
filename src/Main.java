import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args) {
        TestClass obj = new TestClass();
        processAnnotatedMethods(obj);
    }

    private static void processAnnotatedMethods(Object obj) {
        Class<?> objClass = obj.getClass();
        for (Method method : objClass.getDeclaredMethods()) {
            if (isValid(method)) {
                executeAnnotated(obj, method);
            }
        }
    }

    private static boolean isValid(Method method) {
        if (!method.isAnnotationPresent(RepeatCount.class)) {
            return false;
        }
        int modifiers = method.getModifiers();
        return Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers);
    }

    private static void executeAnnotated(Object obj, Method method) {
        RepeatCount annotation = method.getAnnotation(RepeatCount.class);
        int times = annotation.value();

        method.setAccessible(true);
        Object[] argsArray = generateArguments(method.getParameterTypes());

        System.out.println("\nМетод: " + method.getName() + ", повторов: " + times);

        for (int i = 0; i < times; i++) {
            System.out.print("Вызов №" + (i + 1) + ": ");
            try {
                method.invoke(obj, argsArray);
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static Object[] generateArguments(Class<?>[] paramTypes) {
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = generateArgument(paramTypes[i]);
        }
        return args;
    }

    private static Object generateArgument(Class<?> type) {
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }

        String typeName = type.getName();

        switch (typeName) {
            case "int":
            case "java.lang.Integer":
                return 1;
            case "double":
            case "java.lang.Double":
                return 1.1;
            case "boolean":
            case "java.lang.Boolean":
                return true;
            case "java.lang.String":
                return "string";
            default:
                try {
                    var constructor = type.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                } catch (Exception e) {
                    try {
                        var constructors = type.getDeclaredConstructors();
                        if (constructors.length > 0) {
                            var constructor = constructors[0];
                            constructor.setAccessible(true);
                            Object[] constructorArgs = generateArguments(constructor.getParameterTypes());
                            return constructor.newInstance(constructorArgs);
                        }
                    } catch (Exception _) {}

                    return new Object();
                }
        }
    }
}