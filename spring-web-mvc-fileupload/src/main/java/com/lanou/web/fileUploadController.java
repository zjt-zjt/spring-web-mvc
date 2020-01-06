package com.lanou.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


/*
* 基于Apache Fileupload通用文件上传包刚开始没有target,
* 编译后 能创建target,并能在其中创建upload,能够上传上来文件.

 基于Servlet3.0标准API文件上传刚开始没有target,编译后,能创建target,
 * 但如果原来target中没有upload,则不能创建upload,
 * 并且上传会出现找不到路径问题.

* */
@Controller
@RequestMapping("/file")
public class fileUploadController {

    @RequestMapping("/upload")
public  String fileUpload(@RequestParam("myfile") MultipartFile file , HttpServletRequest request, Model model){
        try {
            file.transferTo(new File(WebUtils.getRealPath(request.getServletContext(),"/upload/")+file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
          StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://localhost:8080/upload/");
        stringBuilder.append(file.getOriginalFilename());
        model.addAttribute("path",stringBuilder.toString() );
        model.addAttribute("name",file.getOriginalFilename());
         return "filelist";
    }

}
