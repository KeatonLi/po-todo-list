package cn.todolist.po.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;



@Component
public class MyBatisInsertAndUpdateHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createAt", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("updateAt", System.currentTimeMillis(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", System.currentTimeMillis(), metaObject);
    }
}
