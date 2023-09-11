package cn.todolist.po;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.todolist.po.mapper"})
public class PoTodoBoostrap {

  public static void main(String[] args) {
    SpringApplication.run(PoTodoBoostrap.class, args);
  }
}
