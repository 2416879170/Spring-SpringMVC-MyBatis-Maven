package com.gray.user.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gray.annotation.Log;
import com.gray.interceptor.LogInterceptor;
import com.gray.user.entity.User;
import com.gray.user.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/test") 
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserServiceImpl userService;

@RequestMapping("/dologin.do") //url
@Log(oper="user login")
public String dologin(User user, Model model){
	logger.info("login ....");
	String info = loginUser(user);
	if (!"SUCC".equals(info)) {
		model.addAttribute("failMsg", "�û������ڻ��������");
		return "/jsp/fail";
	}else{
		model.addAttribute("successMsg", "��½�ɹ���");//���ص�ҳ��˵�д��Ĳ���
		model.addAttribute("name", user.getUsername());
		return "/jsp/success";//���ص�ҳ��
	}
  }

@RequestMapping("/logout.do")
public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException{
    Subject subject = SecurityUtils.getSubject();
    if (subject != null) {
    	try{
            subject.logout();
    	}catch(Exception ex){
    	}
    }
    response.sendRedirect("/index.jsp");
}

//@RequestMapping(value="/list", method=RequestMethod.GET)
//@ResponseBody
//public List<User> listData() {
//    return userService.queryAll();
//}
//@RequestMapping(value="/list/{username}", method=RequestMethod.GET)
//@ResponseBody
//public User findUserByUserName2(@PathVariable(value="username") String username) {
//	return userService.findByUserName(username);
//}

private String loginUser(User user) {
 		if (isRelogin(user)) return "SUCC"; // ����Ѿ���½���������µ�¼
 		
 		return shiroLogin(user); // ����shiro�ĵ�½��֤
}
private String shiroLogin(User user) {
		// ��װtoken�������ͻ���˾���ơ���ơ��ͻ���š��û����ƣ�����
	UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword().toCharArray(), null); 
	token.setRememberMe(true);
	
	// shiro��½��֤
	try {
		SecurityUtils.getSubject().login(token);
	} catch (UnknownAccountException ex) {
		return "�û������ڻ����������";
	} catch (IncorrectCredentialsException ex) {
		return "�û������ڻ����������";
	} catch (AuthenticationException ex) {
		return ex.getMessage(); // �Զ��屨����Ϣ
	} catch (Exception ex) {
		ex.printStackTrace();
		return "�ڲ����������ԣ�";
	}
	return "SUCC";
}

private boolean isRelogin(User user) {
	Subject us = SecurityUtils.getSubject();
		// ��֤ͨ����ʱ����Ҫ�ٴκ˶Բ�����������в�����δ�ı䣬�������¼�����Ա���סsession������Ǳ�ڴ��󣡣�- Steven 2014-12-13��
		if (us.isAuthenticated()) {
	       return true; // ����δ�ı䣬�������µ�¼��Ĭ��Ϊ�Ѿ���¼�ɹ�
		}
		return false; // ��Ҫ���µ�½
}
}
