package cn.todolist.po.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class MyBatisInsertAndUpdateHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("create_at", new Timestamp(System.currentTimeMillis()), metaObject);
        this.setFieldValByName("update_at", new Timestamp(System.currentTimeMillis()), metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Timestamp(System.currentTimeMillis()), metaObject);
    }
}
