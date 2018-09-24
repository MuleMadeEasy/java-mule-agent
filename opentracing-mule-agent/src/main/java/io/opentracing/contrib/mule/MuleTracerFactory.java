package io.opentracing.contrib.mule;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerFactory;

public class MuleTracerFactory implements TracerFactory {
	public MuleTracerFactory() {
	}

	public Tracer getTracer() {
		return Configuration.fromEnv().getTracer();
	}

	protected Tracer resolve() {
		return getTracer();
	}
}