package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.exception.GlobalException;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.mapper.MethodnameMapper;
import com.zjc.seckilldemo.mapper.UserMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.Methodname;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.util.*;
import com.zjc.seckilldemo.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2021-11-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements
        IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MethodnameMapper methodnameMapper;
    @Autowired
    private DepositMapper depositMapper;
    /**
     * 登录
     *
     * @param loginVo
     * @return
     */
    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse
            response, LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (null == user) {
            throw new GlobalException(RespBeanEnum.LOGINVO_ERROR);
        }
        //校验密码
        if (!SM3Util.formPassToDBPass(password,
                user.getSalt()).equals(user.getPassword())) {
            System.out.println(SM3Util.formPassToDBPass(password,user.getSalt()));
            System.out.println(user.getPassword());
            throw new GlobalException(RespBeanEnum.LOGINVO_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:" + ticket, JsonUtil.object2JsonStr(user));
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(ticket);
    }

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @param request
     * @param response
     * @return
     */

    @Override
    public User getByUserTicket(String userTicket, HttpServletRequest request,
                                HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        String userJson = (String) redisTemplate.opsForValue().get("user:" +
                userTicket);
        User user = JsonUtil.jsonStr2Object(userJson, User.class);
        if (null != user) {
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
    /**
     * 用户注册
     * @param nickname
     * @param mobile "ONLY ONE"
     * @param identity "ONLY ONE"
     * @param password "ALREADY SM3 AND SALT"
     * @return
     */
    @Override
    public RespBean register(@NotNull String nickname, @NotNull String mobile, @NotNull String identity, @NotNull String password) {
        User user=new User();
        user.setId(mobile);
        user.setSalt("1a2b3c4d");
        user.setNickname(nickname);
        user.setPassword(SM3Util.formPassToDBPass(password, user.getSalt()));
        user.setIdentity(identity);
        if (userMapper.selectUserByIdentity(identity) != null){
            return RespBean.error(RespBeanEnum.ID_ALREADY_REGISTER);
        }else if(userMapper.selectById(mobile) != null) {
            return RespBean.error(RespBeanEnum.MOBILE_ALREADY_REGISTER);
        }else {
            userMapper.insert(user);
            Deposit deposit =new Deposit();
            deposit.setDeposit(BigDecimal.ZERO);
            deposit.setIdentity(user.getIdentity());
            depositMapper.insert(deposit);
        }
        return RespBean.success("注册成功");
    }

    @Override
    public List<ViolationRecordVo> findViolationRecordVobyIdentity(String identity) {
        return userMapper.findUserViolationRecordVoByUseridentity(identity);
    }

    @Override
    public void recordScreenResult(boolean violation, String identity, String method_name, Timestamp time) {
        String record ="";
        if (violation){
            record="不通过";
        }else {
            record="通过";
        }
        QueryWrapper<Methodname> methodnameWrapper = new QueryWrapper<>();
        methodnameWrapper.eq("method_name",method_name);
        Methodname methodname = methodnameMapper.selectOne(methodnameWrapper);
        UserCallVo userCallVo = new UserCallVo();
        userCallVo.setMethod_id(methodname.getId());
        userCallVo.setResult(record);
        userCallVo.setTimestamp(time);
        userCallVo.setUser_identity(identity);
        userMapper.recordScreenResult(userCallVo);
    }


}

