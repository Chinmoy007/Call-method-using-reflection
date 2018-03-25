import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//((getMethod)|(getDeclaredMethod))
//invoke
public class CallMethodUsingReflection {
	public static Object callMethod1(Object object, String methodName, Object args[]) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Caveat: this solution doesn't support some method signatures (e.g.,
		// those with primitive types)
		Class<?> myClass = object.getClass();
		Class<?>[] ptypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			ptypes[i] = args[i].getClass();
		}
		Method method = myClass.getMethod(methodName, ptypes);
		Object returnVal = method.invoke(object, args);
		return returnVal;
	}

	public static Object callMethod2(Object object, String methodName, Object params[], Class[] types)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		// Uses getMethod
		Class<?> myClass = object.getClass();
		Method method = myClass.getMethod(methodName, types);
		Object returnVal = method.invoke(object, params);
		return returnVal;
	}

	public static Object callMethod3(Object object, String methodName, Object params[], Class[] types)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		// Uses getDeclaredMethod and climbs up the superclasses
		Method method = null;
		Class<?> myClass = object.getClass();
		NoSuchMethodException ex = null;
		while (method == null && myClass != null) {
			try {
				method = myClass.getDeclaredMethod(methodName, types);
			} catch (NoSuchMethodException e) {
				ex = e;
				myClass = myClass.getSuperclass();
			}
		}
		if (method == null)
			throw ex;
		Object returnVal = method.invoke(object, params);
		return returnVal;
	}

	public static Object callMethod4(Object object, String methodName, Object params[], Class[] types)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {
		Class tClass = object.getClass();

		Method gs1Method = tClass.getMethod(methodName, types);
		Object str1 = gs1Method.invoke(object, params);
		return str1;
	}// may not be right

	public static Object callMethod5(Object object, String methodName, Object params[], Class[] types)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException {
		String aClass;
		String aMethod;
		// we assume that called methods have no argument
		
		aClass = object.getClass().toString();// lineInput("\nClass : ");
		aMethod = methodName;
		String [] className = aClass.split("\\s+");
		// get the Class
		Class<?> thisClass = Class.forName(className[1]);
		// get an instance
		Object iClass = thisClass.newInstance();
		// get the method
		Method thisMethod = thisClass.getDeclaredMethod(aMethod, (Class[]) types);
		// call the method
		return thisMethod.invoke(iClass, params);

	}

	public static String lineInput(String prompt) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(prompt);
		return input.readLine();
	}

	public static void main(String args[]) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		Integer[] params = { 4, 5 };
		Class[] types = { int.class, double.class };
		Object retval = CallMethodUsingReflection.callMethod5(new Operations(), "publicSum", (Object[]) params, types);
		System.out.println(retval);
	}

}
