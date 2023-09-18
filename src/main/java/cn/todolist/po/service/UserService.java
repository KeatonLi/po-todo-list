package cn.todolist.po.service;

import cn.todolist.po.model.User;
import cn.todolist.po.model.vo.LoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    LoginVO userLogin(String username, String password);

    void userRegister(User user);
}
