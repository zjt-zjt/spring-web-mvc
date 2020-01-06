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



@RequestMapping("/Servletfile")
@Controller
public class ServleFileUpload {
    @RequestMapping("/upload")
    public  String ServletUpload(@RequestParam("myfile") MultipartFile file, Model model, HttpServletRequest request){

        try {
               String path =  WebUtils.getRealPath(request.getServletContext(),"/upload/");
            file.transferTo(new File(path+file.getOriginalFilename()));
         /*   file.transferTo(new File(WebUtils.getRealPath(request.getServletContext(),"/upload/")+file.getOriginalFilename()));*/
        } catch (IOException e) {
            e.printStackTrace();
        }

      StringBuilder sb = new StringBuilder();
        sb.append("http://localhost:8080/upload/");
        sb.append(file.getOriginalFilename());
        model.addAttribute("path" ,sb.toString());
        model.addAttribute("name",file.getOriginalFilename());
        return "filelist";

    }





}
