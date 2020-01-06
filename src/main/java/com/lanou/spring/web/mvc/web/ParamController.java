package com.lanou.spring.web.mvc.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/param")
public class ParamController {

      @RequestMapping("/requestParam")
        public  String testRequestParam(@RequestParam String name , Model model){
         model.addAttribute("name", name);
          model.addAttribute("type","请求参数");
          return  "param_show";
     }

    @RequestMapping("/requestheader")
    public String requestHeaderParamShow(@RequestHeader String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("type", "请求头参数");
        return  "param_show";
    }

    @RequestMapping("/sessionscope")
    public String sessionScopeParamShow(@SessionAttribute String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("type", "会话作用域参数");
        return  "param_show";
    }

    @RequestMapping("/cookie")
    public String cookieParamShow(@CookieValue String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("type", "cookie参数");
        return "param_show";
    }

    @RequestMapping("/checkbox_param")
    public String getCheckboxParam(@RequestParam("hobby")String hobbyStr, Model model) {
//        String hobbyStr = Arrays.toString(hobby.toArray());
        model.addAttribute("name", hobbyStr);
        model.addAttribute("type", "checkbox的值");
        return "param_show";
    }

}
