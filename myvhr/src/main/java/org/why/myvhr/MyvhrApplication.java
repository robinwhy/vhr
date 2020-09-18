package org.why.myvhr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "org.why.myvhr.mapper")
public class MyvhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyvhrApplication.class, args);
    }

}
