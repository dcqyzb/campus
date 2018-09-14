package com.mit.campus.modular.system.controller;

import static com.mit.campus.core.support.HttpKit.getIp;

import java.util.List;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.mit.campus.core.base.controller.BaseController;
import com.mit.campus.core.common.exception.InvalidKaptchaException;
import com.mit.campus.core.log.LogManager;
import com.mit.campus.core.log.factory.LogTaskFactory;
import com.mit.campus.core.node.MenuNode;
import com.mit.campus.core.shiro.ShiroKit;
import com.mit.campus.core.shiro.ShiroUser;
import com.mit.campus.core.util.ApiMenuFilter;
import com.mit.campus.core.util.KaptchaUtil;
import com.mit.campus.core.util.Result;
import com.mit.campus.core.util.ToolUtil;
import com.mit.campus.modular.system.model.User;
import com.mit.campus.modular.system.service.IMenuService;
import com.mit.campus.modular.system.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 登录控制器
 *
 * @author fengshuonan
 * @Date 2017年1月10日 下午8:25:24
 */
@Controller
@Api(value="登录",tags={"登录"})
public class LoginController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value="/test")
    @ResponseBody
    public String test() {
    	return "test";
    }
    
    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        //获取菜单列表
        List<Integer> roleList = ShiroKit.getUser().getRoleList();
        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }
        List<MenuNode> menus = menuService.getMenusByRoleIds(roleList);
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        titles = ApiMenuFilter.build(titles);

        model.addAttribute("titles", titles);

        //获取用户头像
        Integer id = ShiroKit.getUser().getId();
        User user = userService.selectById(id);
        String avatar = user.getAvatar();
        model.addAttribute("avatar", avatar);

        return "/index.html";
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 
    * admin后台登录
    * @param @param username
    * @param @param password
    * @param @param remember
    * @param @param kaptcha 验证码
    * @param @return
    * @return String
    * @throws
    * @company mitesofor
    * @author shuyy
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value="admin后台登录", notes="admin后台登录")
    public String loginVali(String username, String password, String remember, @ApiParam(name="kaptcha", value="验证码") String kaptcha) {
/*        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");*/

        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
//            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return REDIRECT + "/";
    }
    
    /**
     * 
    * 
    *    数据展示前台项目登录
    * @param @param username
    * @param @param password
    * @param @param remember
    * @param @param kaptcha 验证码
    * @param @return
    * @return String
    * @throws
    * @company mitesofor
    * @author shuyy
     */
    @RequestMapping(value = "/dataDisplayLogin", method = RequestMethod.POST)
    @ApiOperation(value="数据展示前台项目登录", notes="数据展示前台项目登录")
    @ResponseBody
    @ApiImplicitParams({
    	  @ApiImplicitParam(name="kaptcha",value="验证码",dataType="string", paramType = "query")})
    public Result dataDisplayLogin(String username, String password, String remember, String kaptcha) {
/*        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");*/

        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
//            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
            	return Result.error("验证码错误");
//                throw new InvalidKaptchaException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }
        try {
        	currentUser.login(token);
		} catch(Exception e) {
			return Result.error("账号或密码错误");
		}

        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return Result.success(null, "登录成功");
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUser().getId(), getIp()));
        ShiroKit.getSubject().logout();
        deleteAllCookie();
        return REDIRECT + "/login";
    }
}
