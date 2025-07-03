package cn.todolist.po.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 数据库初始化配置类
 * 在应用启动时自动执行SQLite数据库初始化脚本
 */
@Component
@Slf4j
public class DatabaseInitConfig implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 检查数据库是否已经初始化（通过检查表是否存在）
            boolean isInitialized = checkIfDatabaseInitialized();
            
            if (!isInitialized) {
                log.info("检测到数据库未初始化，开始执行初始化脚本...");
                
                // 读取SQLite初始化脚本
                ClassPathResource resource = new ClassPathResource("DDL/init_sqlite.sql");
                String sql = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));

                // 分割SQL语句并执行
                String[] sqlStatements = sql.split(";");
                for (String statement : sqlStatements) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        try {
                            jdbcTemplate.execute(statement);
                            log.info("执行SQL语句成功: {}", statement.substring(0, Math.min(50, statement.length())) + "...");
                        } catch (Exception e) {
                            log.error("执行SQL语句失败: {}, 错误: {}", statement, e.getMessage());
                        }
                    }
                }
                
                log.info("SQLite数据库初始化完成");
            } else {
                log.info("数据库已经初始化，跳过初始化脚本执行");
            }
        } catch (Exception e) {
            log.error("SQLite数据库初始化失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 检查数据库是否已经初始化
     * @return true表示已初始化，false表示未初始化
     */
    private boolean checkIfDatabaseInitialized() {
        try {
            // 尝试查询todo_user表，如果表存在且能查询到结果，说明数据库已初始化
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM todo_user", Integer.class);
            return true;
        } catch (Exception e) {
            // 如果查询失败，说明表不存在，数据库未初始化
            return false;
        }
    }
}