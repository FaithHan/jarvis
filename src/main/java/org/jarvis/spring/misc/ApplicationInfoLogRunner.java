package org.jarvis.spring.misc;

import lombok.extern.slf4j.Slf4j;
import org.jarvis.misc.JVMUtils;
import org.jarvis.misc.OSUtils;
import org.jarvis.network.IPUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 功能：输出PID，JDK版本，打印web容器内网地址和外网地址
 */
@Slf4j
public class ApplicationInfoLogRunner implements CommandLineRunner, ApplicationContextAware {

    private static final String LOOPBACK_ADDRESS = "127.0.0.1";

    private static final String URL_FORMAT = "http://%s:%d";

    private final boolean openBrowser;

    private ApplicationContext applicationContext;

    private String localAddress;

    private String internetAddress;

    public ApplicationInfoLogRunner() {
        this(false);
    }

    public ApplicationInfoLogRunner(boolean openBrowser) {
        this.openBrowser = openBrowser;
    }

    @Override
    public void run(String... args) throws Exception {
        if (applicationContext instanceof WebServerApplicationContext) {
            initApplicationInfo();
            printApplicationInfo();
            if (this.openBrowser) {
                openBrowser();
            }
        }
    }

    private void initApplicationInfo() {
        int port = ((WebServerApplicationContext) applicationContext).getWebServer().getPort();
        this.localAddress = String.format(URL_FORMAT, LOOPBACK_ADDRESS, port);
        this.internetAddress = String.format(URL_FORMAT, IPUtils.getLocalIp(), port);
    }

    private void printApplicationInfo() {
        log.info("********************");
        log.info("Application PID:  " + new ApplicationPid());
        log.info("Application Java version: " + JVMUtils.JAVA_SPECIFICATION_VERSION);
        log.info("Application started at local address:  " + localAddress);
        log.info("Application started at internet address:  " + internetAddress);
        log.info("********************");
    }

    private void openBrowser() {
        OSUtils.browse(localAddress);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
