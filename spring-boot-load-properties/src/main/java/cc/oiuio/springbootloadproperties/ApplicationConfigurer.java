package cc.oiuio.springbootloadproperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ApplicationConfigurer {
    public static final String SPRING_CONFIG_LOCATION = "spring.config.location";

    /**
     * 自定义配置加载，方法定义为static的，保证优先加载
     * @return
     */
    @Bean
    public static PropertyPlaceholderConfigurer properties() {
        log.debug("========== ApplicationConfigurer properties() =========");
        final PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setIgnoreResourceNotFound(true);
        final List<Resource> resourceLst = new ArrayList<>();

        if(System.getProperty(SPRING_CONFIG_LOCATION) != null){
            String configFilePath = System.getProperty(SPRING_CONFIG_LOCATION);
            String[] configFiles = configFilePath.split(",|;");

            FileSystemResource res =null;
            for (String configFile : configFiles) {
                if (configFile.startsWith("file:")){
                    resourceLst.add(new FileSystemResource(configFile));
                }else {
                    resourceLst.add( new ClassPathResource(configFile));
                }
            }
        }else {
//            resourceLst.add(new ClassPathResource("config/application.properties"));
            resourceLst.add(new ClassPathResource("kafka.properties"));
        }
        ppc.setLocations(resourceLst.toArray(new Resource[]{}));
        return ppc;
    }
}
