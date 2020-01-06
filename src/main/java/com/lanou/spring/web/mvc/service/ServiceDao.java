package com.lanou.spring.web.mvc.service;

import com.lanou.spring.web.mvc.bean.User;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

public interface ServiceDao {
   List<User> queryUser();
   int saveUser(User user);
   User findById(Integer id);
   int deleteById(Integer id);
   int update(User user);


}
