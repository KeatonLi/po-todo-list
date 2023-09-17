package cn.todolist.po.service;

import cn.todolist.po.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    Boolean userLogin(String username, String password);

    void userRegister(User user);
}
