package cc.oiuio.springbootloadproperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@SpringBootApplication
@Slf4j
public class SpringBootLoadPropertiesApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(SpringBootLoadPropertiesApplication.class, args);
        Environment environment = app.getEnvironment();
        log.debug("c.database.url={}", environment.getProperty("c.database.url"));
        log.debug("c.database.password={}", environment.getProperty("c.database.password"));
        log.debug("c.database.username={}", environment.getProperty("c.database.username"));

    }

    @Component
    class CustomCommandLineRunner implements CommandLineRunner {
        @Override
        public void run(String... args) {
//            new Thread(() -> {
//                try {
//                    Thread.currentThread().join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
        }
    }


}
