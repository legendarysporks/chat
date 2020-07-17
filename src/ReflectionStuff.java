import genericstuff.GenericServer;

import java.lang.reflect.*;

public class ReflectionStuff {

	public static void main(String[] args) {
		dumpClassInfo(GenericServer.class);
		Server server = new Server();
		Class<Server> serverClass = (Class<Server>) server.getClass();
		dumpClassInfo(serverClass);
		try {
			Server otherServer = serverClass.getConstructor().newInstance();
			dumpClassInfo(otherServer.getClass());
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static void dumpClassInfo(Class<?> clazz) {
		String name = clazz.getName();
		System.out.println("Class: " + name);

		String packageName = clazz.getPackage().getName();
		Class<?> superClazz = clazz.getSuperclass();
		Class<?>[] interfaces = clazz.getInterfaces();
		String mods = Modifier.toString(clazz.getModifiers());

		System.out.println("Constructors");
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			dumpMethodInfo(constructor);
			System.out.println("\t" + constructor.toString());
		}

		System.out.println("\nMethods");
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			dumpMethodInfo(method);
			System.out.println("\t" + method.toString());
		}

		System.out.println("\nFields");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			System.out.println("\t" + field.toString());
		}

		System.out.println();
		System.out.println();
	}

	public static void dumpMethodInfo(Executable method) {
		String name = method.getName();
		String mods = Modifier.toString(method.getModifiers());
		int paramCount = method.getParameterCount();
		Class<?>[] paramTypes = method.getParameterTypes();
		for (Class<?> paramType : paramTypes) {
			name = paramType.getName();
		}
		Class<?>[] exceptionTypes = method.getExceptionTypes();
		for (Class<?> exceptionType : exceptionTypes) {
			name = exceptionType.getName();
		}
	}
}
