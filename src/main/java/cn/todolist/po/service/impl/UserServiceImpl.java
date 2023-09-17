package cn.todolist.po.service.impl;

import cn.todolist.po.enums.RespStatusEnum;
import cn.todolist.po.exception.CommonException;
import cn.todolist.po.mapper.UserMapper;
import cn.todolist.po.model.User;
import cn.todolist.po.service.UserService;
import cn.todolist.po.utils.MD5Utils;
import cn.todolist.po.utils.SnowFlakeUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    SnowFlakeUtil snowFlakeUtil;


    @Override
    public Boolean userLogin(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new CommonException(RespStatusEnum.ERROR_500.getCode(), "用户名或者密码输入为空");
        }
        User user = this.lambdaQuery().eq(User::getUsername, username)
                .eq(User::getPassword, MD5Utils.string2MD5(password))
                .one();
        if (Objects.nonNull(user)) {
            return true;
        } else {
            throw new CommonException(RespStatusEnum.ERROR_500.getCode(), "用户名或密码错误");
        }
    }

    @Override
    public void userRegister(User user) {
        Long count = this.lambdaQuery().eq(User::getUsername, user.getUsername()).count();
        if (count > 0) {
            throw new CommonException(RespStatusEnum.ERROR_500.getCode(), "用户名已存在");
        } else {
            user.setId(snowFlakeUtil.getNextId());
            user.setPassword(MD5Utils.string2MD5(user.getPassword()));
            this.save(user);
        }
    }
}
