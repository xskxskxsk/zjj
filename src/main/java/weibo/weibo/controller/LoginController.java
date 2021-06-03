package weibo.weibo.controller;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weibo.weibo.annotation.Audit;
import weibo.weibo.annotation.LoginUser;
import weibo.weibo.async.EventModel;
import weibo.weibo.async.EventProducer;
import weibo.weibo.async.EventType;
import weibo.weibo.service.UserService;
import weibo.weibo.util.Common;
import weibo.weibo.util.ResponseUtil;
import weibo.weibo.util.ReturnObject;
import weibo.weibo.util.WeiboUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "password", value = "密码", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "remember", value = "记住密码", required = false),
    })
    @ResponseBody
    @PostMapping("reg")
    public String reg(
                      @RequestParam("username") String userName,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(userName, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return WeiboUtil.getJSONString(0, "注册成功");
            } else {
                return WeiboUtil.getJSONString(1, "注册失败");
            }
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return WeiboUtil.getJSONString(1, "注册异常");
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "password", value = "密码", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "remember", value = "记住密码", required = false),
    })
    @ResponseBody
    @GetMapping("login")
    public Object login(
                        @RequestParam("username") String userName,
                        @RequestParam("password") String password,
                        HttpServletResponse httpServletResponse) {
        logger.debug("login: userName = "+userName);


        //String ip = IpUtil.getIpAddr(httpServletRequest);
        ReturnObject<String> jwt = userService.login(userName, password);

        if(jwt.getData() == null){
            logger.debug("login fail："+jwt.getErrmsg());
            return ResponseUtil.fail(jwt.getCode(), jwt.getErrmsg());
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseUtil.ok(jwt.getData());
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
