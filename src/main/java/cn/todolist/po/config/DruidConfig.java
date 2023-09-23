package cn.todolist.po.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix ="spring.datasource")//和配置文件绑定
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }
    //获取后台监控
    @Bean
    //由于 SpringBoot 默认是以 jar 包的方式启动嵌入式的 Servlet 容器来启动 SpringBoot 的 web 应用，没有 web.xml 文件。所以想用使用 Servlet 功能，就必须要借用 Spring Boot 提供的 ServletRegistrationBean 接口。
    public ServletRegistrationBean servletRegistration(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");   //StatViewServlet用于展示Druid的统计信息

        //设置后台登录的账户和密码
        HashMap<String, String> initParameters = new HashMap<>();

        //增加配置 登录的key是固定的
        initParameters.put("loginUsername","admin");
        initParameters.put("loginPassword","123456");

        //设置谁可以访问
        initParameters.put("allow","");//任何人都可以访问

        //设置初始化参数
        bean.setInitParameters(initParameters);

        return bean;
    }
}