package com.lanou.spring.web.mvc.service;

import com.lanou.spring.web.mvc.bean.User;
import com.lanou.spring.web.mvc.dao.UseDao;
import com.lanou.spring.web.mvc.web.HelloConrtoller;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


import java.util.List;
@Setter
@Getter

public class ServiceDaoImple   implements  ServiceDao {

    private UseDao useDao;
    private HelloConrtoller helloConrtoller;
    @Override
    public List<User> queryUser() {
        return useDao.queryAll();
    }

   @Override
    public int saveUser(User user) {
        return useDao.insertUser(user);
    }

    @Override
    public User findById(Integer id) {
        return useDao.findById(id);
    }

    @Override
    public int deleteById(Integer id) {
        return useDao.deleteById(id);
    }

    @Override
    public int update(User user) {
        return useDao.update(user);
    }
}
