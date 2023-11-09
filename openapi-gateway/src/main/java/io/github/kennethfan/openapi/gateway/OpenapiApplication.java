package io.github.kennethfan.openapi.gateway;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan
@ComponentScan(basePackages = {"io.github.kennethfan.openapi.*"})
@MapperScan(value = {"io.github.kennethfan.openapi.gateway.dao.**.mapper"})
@Slf4j
public class OpenapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenapiApplication.class, args);
        log.info("......服务启动成功!");
    }
}
