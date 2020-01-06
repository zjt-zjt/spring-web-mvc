package com.lanou.spring.web.mvc.web;


import com.lanou.spring.web.mvc.bean.User;
import com.lanou.spring.web.mvc.service.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*该注解本身是一个组合注解，
由`@Controller`和`@ResponseBody`两个注解组成，
所以拥有这两个注解的作用。 在类上声明后，该类会变成一个Controller类
，同时，方法的返回值会作为响应体经过消息转换器直接响应给客户端，
而不会将其作为视图渲染。*/
@RestController
//@Controller
//@ResponseBody
@RequestMapping("/user")
public class UserController {
   @Autowired
   private ServiceDao serviceDao;
   // @RequestMapping(value = "/{id}", method = RequestMethod.GET) 和下面的作用等效
    @GetMapping("/{id}")
   public User  queryById( @PathVariable Integer id){
       return  serviceDao.findById(id);
   }
    @GetMapping("/")
   public List<User> queryAll(){
    return serviceDao.queryUser();
   }
    @PostMapping("/")
    public  int saveUser( User user){

        return  serviceDao.saveUser(user);
    }

     @PutMapping("/")
   public  int updateUser( User user){
         System.out.println(user.getName());
        return  serviceDao.update(user);
   }

     @DeleteMapping("/{id}")
   public int deleteUser(@PathVariable Integer id){
       return  serviceDao.deleteById(id) ;
   }


}
