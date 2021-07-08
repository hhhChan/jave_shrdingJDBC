package com.can;

import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableLeafServer
public class SpringbootWebApplication {

    public static void main(String[] args){
        SpringApplication.run(SpringbootWebApplication.class, args);
    }
}
