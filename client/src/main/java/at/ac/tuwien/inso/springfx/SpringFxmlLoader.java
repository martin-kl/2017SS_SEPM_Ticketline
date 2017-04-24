package at.ac.tuwien.inso.springfx;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.fxml.FXMLLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Bridge between Spring and JavaFX which allows to inject Spring beans in JavaFX controller.
 * <p>
 * Based on <a href="http://www.javacodegeeks.com/2013/03/javafx-2-with-spring.html">http://www.javacodegeeks.com/2013/03/javafx-2-with-spring.html</a>
 */
@Slf4j
@Component
public class SpringFxmlLoader {

    private final ApplicationContext applicationContext;

    public SpringFxmlLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Generate fxml loader based on the given URL.
     *
     * @param url the url
     * @return the FXML loader
     */
    private FXMLLoader generateFXMLLoader(String url) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(SpringFxmlLoader.class.getResource(url));
        fxmlLoader.setResources(BundleManager.getBundle());
        fxmlLoader.setControllerFactory(clazz -> {
            log.debug("Trying to retrieve spring bean for type {}", clazz.getName());
            Object bean = null;
            try {
                bean = applicationContext.getBean(clazz);
            } catch (NoSuchBeanDefinitionException e) {
                log.warn("No qualifying spring bean of type {} found", clazz.getName());
            }
            if (bean == null) {
                log.debug("Trying to instantiating class of type {}", clazz.getName());
                try {
                    bean = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("Failed to instantiate bean of type {}", clazz.getName(), e);
                    throw new RuntimeException("Failed to instantiate bean of type " + clazz.getName(), e);
                }
            }
            return bean;
        });
        return fxmlLoader;
    }

    /**
     * Loads object hierarchy from the FXML document given in the URL.
     *
     * @param url the url
     * @return the object
     */
    public Object load(String url) {
        try {
            return generateFXMLLoader(url).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and wraps an object hierarchy.
     *
     * @param url the url
     * @return the load wrapper
     */
    public LoadWrapper loadAndWrap(String url) {
        FXMLLoader fxmlLoader = generateFXMLLoader(url);
        try {
            return new LoadWrapper(fxmlLoader.load(), fxmlLoader.getController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The Class LoadWrapper.
     */
    public final class LoadWrapper {

        private final Object controller;
        private final Object loadedObject;

        /**
         * Instantiates a new load wrapper.
         *
         * @param loadedObject the loaded object
         * @param controller   the controller
         */
        public LoadWrapper(Object loadedObject, Object controller) {
            this.controller = controller;
            this.loadedObject = loadedObject;
        }

        /**
         * Gets the controller.
         *
         * @return the controller
         */
        public Object getController() {
            return controller;
        }

        /**
         * Gets the loaded object.
         *
         * @return the loaded object
         */
        public Object getLoadedObject() {
            return loadedObject;
        }

    }

}