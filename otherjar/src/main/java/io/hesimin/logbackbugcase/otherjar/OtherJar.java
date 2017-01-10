package io.hesimin.logbackbugcase.otherjar;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author hesimin 17-1-10
 */
public class OtherJar {
    private static final Logger logger = LoggerFactory.getLogger(OtherJar.class);
    private static final String logback_resource_file = "logback_other.xml";
    private static final String log4j_resource_file = "";

    static {
        // codes of third-party jar.

        String logConfigFilePath = System.getProperty("configFile", System.getenv("configFile"));
        try {
            ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
            Class classType = iLoggerFactory.getClass();
            if (classType.getName().equals("org.slf4j.impl.Log4jLoggerFactory")) {
                Class<?> DOMConfigurator = null;
                Object DOMConfiguratorObj = null;
                DOMConfigurator = Class.forName("org.apache.log4j.xml.DOMConfigurator");
                DOMConfiguratorObj = DOMConfigurator.newInstance();
                if (null == logConfigFilePath) {
                    Method configure = DOMConfiguratorObj.getClass().getMethod("configure", URL.class);
                    URL url = OtherJar.class.getClassLoader().getResource(log4j_resource_file);
                    configure.invoke(DOMConfiguratorObj, url);
                } else {
                    Method configure = DOMConfiguratorObj.getClass().getMethod("configure", String.class);
                    configure.invoke(DOMConfiguratorObj, logConfigFilePath);
                }

            } else if (classType.getName().equals("ch.qos.logback.classic.LoggerContext")) {
                Class<?> joranConfigurator = null;
                Class<?> context = Class.forName("ch.qos.logback.core.Context");
                Object joranConfiguratoroObj = null;
                joranConfigurator = Class.forName("ch.qos.logback.classic.joran.JoranConfigurator");
                joranConfiguratoroObj = joranConfigurator.newInstance();
                Method setContext = joranConfiguratoroObj.getClass().getMethod("setContext", context);
                setContext.invoke(joranConfiguratoroObj, iLoggerFactory);
                if (null == logConfigFilePath) {
                    URL url = OtherJar.class.getClassLoader().getResource(logback_resource_file);
                    Method doConfigure =
                            joranConfiguratoroObj.getClass().getMethod("doConfigure", URL.class);
                    doConfigure.invoke(joranConfiguratoroObj, url);
                } else {
                    Method doConfigure =
                            joranConfiguratoroObj.getClass().getMethod("doConfigure", String.class);
                    doConfigure.invoke(joranConfiguratoroObj, logConfigFilePath);
                }

            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void print() {
        logger.info("OtherJar logger");
    }
}
