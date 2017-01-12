package io.hesimin.logbackbugcase.app;

import io.hesimin.logbackbugcase.otherjar.OtherJar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hesimin 17-1-10
 */
public class Main {

    public static void main(String[] args) throws Exception {
        OtherJar.print();
        new Thread(new Runnable() {
            private  final Logger logger = LoggerFactory.getLogger(this.getClass());
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    logger.info(i + "");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}
