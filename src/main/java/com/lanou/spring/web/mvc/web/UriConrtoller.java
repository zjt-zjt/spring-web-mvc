package com.lanou.spring.web.mvc.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UriConrtoller {
    //string后面的名字和 @RequestMapping("/testPath{id}")中的名字一样时可以省略不写.
  @RequestMapping("/testPath{id}")
   public  String getParam(Model model , @PathVariable("id") String id){
      model.addAttribute("msg" ,"我是通过访问路径传递的参数：" + id);
      return "hello_springmvc";
   }
}
