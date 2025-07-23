package com.officine.losto.backend.springcontext.session;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
	private static ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;

	}
	
	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

	public static Object getBean(String name) {
		return ctx.getBean(name);
	}

}
