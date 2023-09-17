package cn.todolist.po.service.impl;

import cn.todolist.po.enums.RespStatusEnum;
import cn.todolist.po.exception.CommonException;
import cn.todolist.po.mapper.UserMapper;
import cn.todolist.po.model.User;
import cn.todolist.po.service.UserService;
import cn.todolist.po.utils.MD5Utils;
import cn.todolist.po.utils.SnowFlakeUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
        LambdaQueryChainWrapper<User> wrapper = new LambdaQueryChainWrapper<>(userMapper);
        wrapper.eq(User::getUsername, username).eq(User::getPassword, MD5Utils.string2MD5(password));
        User user = userMapper.selectOne(wrapper);
        if (Objects.nonNull(user)) {
            return true;
        } else {
            throw new CommonException(RespStatusEnum.ERROR_500.getCode(), "用户名或密码错误");
        }
    }

    @Override
    public void userRegister(User user) {
        LambdaQueryChainWrapper<User> wrapper = new LambdaQueryChainWrapper<>(userMapper);
        Long count = wrapper.eq(User::getUsername, user).count();
        if (count > 0) {
            throw new CommonException(RespStatusEnum.ERROR_500.getCode(), "用户名已存在");
        } else {
            user.setId(snowFlakeUtil.getNextId());
            this.save(user);
        }
    }
}
