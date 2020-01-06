package com.lanou.spring.web.mvc.dao;

import com.lanou.spring.web.mvc.bean.User;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UseDao {
    List<User> queryAll();
    int insertUser(User user);
    User findById(Integer id);
    int deleteById(Integer id);
    int update(User user);
}
