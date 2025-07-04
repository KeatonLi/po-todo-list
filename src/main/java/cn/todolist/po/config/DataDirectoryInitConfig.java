package cn.todolist.po.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 数据目录初始化配置类
 * 在数据源初始化之前确保数据目录存在
 * 使用最高优先级确保在其他Bean初始化前执行
 */
@Configuration
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataDirectoryInitConfig {

    @Value("${spring.datasource.url:jdbc:sqlite:/tmp/poTodoList/poTodo.db}")
    private String datasourceUrl;

    @PostConstruct
    public void initDataDirectory() {
        try {
            // 从数据源URL中提取数据库文件路径
            String dbPath = extractDatabasePath(datasourceUrl);
            if (dbPath != null) {
                // 获取数据库文件的父目录
                Path dbFilePath = Paths.get(dbPath);
                Path parentDir = dbFilePath.getParent();
                
                if (parentDir != null && !Files.exists(parentDir)) {
                    // 创建目录（包括所有必要的父目录）
                    Files.createDirectories(parentDir);
                    log.info("成功创建数据目录: {}", parentDir.toAbsolutePath());
                } else if (parentDir != null) {
                    log.info("数据目录已存在: {}", parentDir.toAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.error("创建数据目录失败: {}", e.getMessage(), e);
            // 不抛出异常，让应用继续启动，由后续的错误处理机制处理
        }
    }

    /**
     * 从JDBC URL中提取数据库文件路径
     * @param jdbcUrl JDBC连接URL
     * @return 数据库文件路径，如果无法提取则返回null
     */
    private String extractDatabasePath(String jdbcUrl) {
        if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:sqlite:")) {
            return null;
        }
        
        // 移除 "jdbc:sqlite:" 前缀
        String dbPath = jdbcUrl.substring("jdbc:sqlite:".length());
        
        // 处理相对路径和绝对路径
        if (dbPath.startsWith("./")) {
            // 相对路径，基于当前工作目录
            return dbPath;
        } else if (dbPath.startsWith("/")) {
            // 绝对路径，直接返回
            return dbPath;
        } else {
            // 其他格式，当作相对路径处理
            return "./" + dbPath;
        }
    }
}