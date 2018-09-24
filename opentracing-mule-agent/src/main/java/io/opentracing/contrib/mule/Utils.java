package io.opentracing.contrib.mule;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jose Montoya
 */

@SuppressWarnings("unchecked")
public class Utils {
	public static void registerHooks(ClassLoader loader, Tracer tracer) throws ClassNotFoundException {
		try {
			Class hooksClass = loader.loadClass("reactor.core.publisher.Hooks");
			Method tracerOperator = loader.loadClass("io.opentracing.contrib.reactor.TracedSubscriber")
					.getMethod("tracedOperator", io.opentracing.Tracer.class);
			hooksClass.getMethod("onEachOperator", java.util.function.Function.class)
					.invoke(null, tracerOperator.invoke(null, tracer));
			hooksClass.getMethod("onLastOperator", java.util.function.Function.class)
					.invoke(null, tracerOperator.invoke(null, tracer));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void onNextWithActiveSpan(Object subscriber, Object event, Span span, Tracer tracer) {
		try {
			try (Scope inScope = tracer.scopeManager().activate(span, false)) {
				subscriber.getClass().getMethod("onNext", Object.class).invoke(subscriber, event);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static Object getEventContext(Object event) {
		try {
			return event.getClass().getMethod("getContext", null).invoke(event, null);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	private static boolean isInstance(Class clazz, Object event) {
		return clazz.isInstance(event);
	}
}
