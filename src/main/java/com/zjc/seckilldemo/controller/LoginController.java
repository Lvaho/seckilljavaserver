package com.zjc.seckilldemo.controller;

import com.zjc.seckilldemo.aop.ScreenAnnotation;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.vo.LoginVo;
import com.zjc.seckilldemo.vo.RespBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.model.IModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private IUserService userService;
    @ApiOperation(value = "前往网页版登录界面")
    @RequestMapping(value = "toLogin",method = RequestMethod.GET)
    public String toLogin(){
        return "login";
    }


    /**
     * 登录
     *
     * @return
     */
    @ApiOperation(value = "登录操作 传的密码为经过SM3加密加盐的形式 如：用户密码123456 先经过加盐处理变成 12123456c3 再经过SM3加密")
    @RequestMapping(value = "/doLogin",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doLogin(HttpServletRequest request, HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        return userService.login(request, response, loginVo);
    }

    /**
     * 前往登陆后页面
     * @param model
     * @param user
     * @return
     */
    @ScreenAnnotation
    @ApiOperation(value = "登陆后页面")
    @RequestMapping(value = "/logined",method = RequestMethod.GET)
    public String Logined(Model model, User user){
        String name = user.getNickname();
        model.addAttribute("name",name);
        return "logined";
    }
}
