package de.uni_koeln.ub.drc.ui.util;

import java.text.MessageFormat;

public class ImplementationLoader {

	public static Object newInstance(final Class<?> type) {
		String name = type.getName();
		Object result = null;
		try {
			result = type.getClassLoader().loadClass(name + "Impl")
					.newInstance();
		} catch (Throwable t) {
			String txt = "Could not load implementation for {0}";
			String msg = MessageFormat.format(txt, new Object[] { name });
			throw new RuntimeException(msg, t);
		}
		return result;
	}
}
