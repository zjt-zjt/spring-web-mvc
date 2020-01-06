package com.lanou.spring.web.mvc.web;


import com.lanou.spring.web.mvc.bean.User;
import com.lanou.spring.web.mvc.service.ServiceDao;
import com.lanou.spring.web.mvc.service.ServiceDaoImple;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;
import sun.net.www.protocol.http.HttpURLConnection;

import java.util.List;

@Setter
@Getter
@Controller
public class HelloConrtoller {



   /* @RequestMapping("/")
    public String index() {
        return "index2";
    }*/

   @Autowired
    private  ServiceDaoImple serviceDao;


    @RequestMapping("/hello")
    public String test ( @RequestHeader("User-Agent") String userAgent, Model model){
        model.addAttribute("userAgent", userAgent);
        System.out.println("hello");
        //return "user";
        return "forward:/user";

    }



   /* @RequestMapping("/hello")
    public View test1 (@RequestHeader("User-Agent") String userAgent, Model model){
        model.addAttribute("userAgent", userAgent);
        System.out.println("hello");
        return new InternalResourceView("user");

    }*/


/*
    @RequestMapping("/hello")
    public String test ( @RequestHeader("User-Agent") String userAgent, Model model){
        model.addAttribute("userAgent", userAgent);
        System.out.println("hello");
       return "redirect:user";
    }*/


   /* @RequestMapping("/hello")
    public View test ( @RequestHeader("User-Agent") String userAgent, Model model){
        model.addAttribute("userAgent", userAgent);
        System.out.println("hello");
        return new RedirectView("user.jsp");
    }*/

    @RequestMapping("/hello_world")
    public String helloWorld(Model model) {
        model.addAttribute("msg", "hello world");
        return "hello_springmvc";
    }

   /* @RequestMapping("/user")
    public String toUserList(Model model) {
        List<User> userList = serviceDao.queryUser();
        model.addAttribute("userList", userList);
        System.out.println("进入user");
        return "user";
    }*/

    @RequestMapping("/test")
    public String toTest(Model model) {
        return "/WEB-INF/page/test";
    }

    @RequestMapping("/save")
    public ModelAndView saveUser(User user, ModelAndView mv) {
        int rows = serviceDao.saveUser(user);
        mv.addObject("msg", "成功保存"+rows+"条数据");
        mv.setViewName("hello_springmvc");
        return mv;
    }
}
