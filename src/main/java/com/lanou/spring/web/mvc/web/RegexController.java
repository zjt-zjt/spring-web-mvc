package com.lanou.spring.web.mvc.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegexController {

    //在地址栏上拼上如上述类型的地址:/hello/download/string-3.1.2.jar
    @RequestMapping("/hello/download/{name:[a-z-]+}-{version:\\d\\.\\d\\.\\d}{ext:\\.[a-z]+}")
    public String download(@PathVariable String name, @PathVariable String version, @PathVariable("ext") String extName, Model model) {

        StringBuilder sb = new StringBuilder();
        sb.append("通过正则从地址栏中获取的参数: ");
        sb.append("<br />");
        sb.append("name: ");
        sb.append(name);
        sb.append("<br />");
        sb.append("version: ");
        sb.append(version);
        sb.append("<br />");
        sb.append("后缀: ");
        sb.append(extName);
        sb.append("<br />");

        model.addAttribute("msg", sb.toString());
        return "hello_springmvc";
    }

}
