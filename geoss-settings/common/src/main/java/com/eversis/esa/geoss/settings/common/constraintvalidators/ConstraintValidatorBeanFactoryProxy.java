package com.eversis.esa.geoss.settings.common.constraintvalidators;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * The type Constraint validator bean proxy.
 */
@Component
class ConstraintValidatorBeanFactoryProxy implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Gets bean.
     *
     * @param <T> the type parameter
     * @param requiredType the required type
     * @return the bean
     */
    static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}
