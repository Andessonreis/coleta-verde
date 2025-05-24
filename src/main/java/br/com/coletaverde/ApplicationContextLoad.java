package br.com.coletaverde;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Utility class to provide static access to the Spring {@link ApplicationContext}.
 * Should be used carefully to avoid tight coupling and poor testability.
 */
@Component
public class ApplicationContextLoad implements ApplicationContextAware {

    private static volatile ApplicationContext context;

    /**
     * Sets the ApplicationContext when initialized by the Spring container.
     *
     * @param applicationContext the Spring ApplicationContext
     * @throws BeansException if context assignment fails
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextLoad.context = applicationContext;
    }


    /**
     * Gets the current Spring {@link ApplicationContext}.
     *
     * @return the active ApplicationContext
     * @throws IllegalStateException if the context has not been initialized
     */
    public static ApplicationContext getApplicationContext() {
        if (context == null) {
            throw new IllegalStateException("ApplicationContext has not been initialized yet.");
        }
        return context;
    }

    /**
     * Retrieves a Spring-managed bean by its type.
     *
     * @param requiredType the class of the bean
     * @param <T>          the type parameter
     * @return the bean instance
     */
    public static <T> T getBean(Class<T> requiredType) {
        return getApplicationContext().getBean(requiredType);
    }

    /**
     * Retrieves a Spring-managed bean by its name and type.
     *
     * @param name         the name of the bean
     * @param requiredType the class of the bean
     * @param <T>          the type parameter
     * @return the bean instance
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return getApplicationContext().getBean(name, requiredType);
    }
}
