package com.officine.losto.session;

import org.springframework.beans.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

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

    public static ApplicationContext getCtx() {
        return ctx;
    }

    public static void setCtx(ApplicationContext ctx) {
        SpringContext.ctx = ctx;
    }
}
