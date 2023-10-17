package cn.todolist.po;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(value = "cn.todolist.po.mapper")
@ServletComponentScan(basePackages = "cn.todolist.po.config")
public class PoTodoBoostrap {

    public static void main(String[] args) {
        SpringApplication.run(PoTodoBoostrap.class, args);
    }
}
