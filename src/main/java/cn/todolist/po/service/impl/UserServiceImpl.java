
package cn.todolist.po.service.impl;

import cn.todolist.po.enums.RespStatusEnum;
import cn.todolist.po.exception.CommonException;
import cn.todolist.po.mapper.UserMapper;
import cn.todolist.po.model.User;
import cn.todolist.po.model.vo.LoginVO;
import cn.todolist.po.service.UserService;
import cn.todolist.po.utils.JwtUtil;
import cn.todolist.po.utils.MD5Utils;
import cn.todolist.po.utils.SnowFlakeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    SnowFlakeUtil snowFlakeUtil;


    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     * @throws CommonException 共通异常
     */
    @Override
    public LoginVO userLogin(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new CommonException(RespStatusEnum.EMPTY_INPUT);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("password", MD5Utils.string2MD5(password));
        User user = super.getOne(queryWrapper);
        if (user == null) {
            throw new CommonException(RespStatusEnum.WRONG_PASSWORD);
        }
        return LoginVO.builder()
                .userId(user.getId())
                .token(JwtUtil.createToken(user))
                .build();
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    @Override
    public void userRegister(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        Long count = super.count(queryWrapper);
        if (count > 0) {
            throw new CommonException(RespStatusEnum.ERROR_500.getCode(), "用户名已存在");
        } else {
            user.setId(snowFlakeUtil.getNextId());
            user.setPassword(MD5Utils.string2MD5(user.getPassword()));
            this.save(user);
        }
    }
}