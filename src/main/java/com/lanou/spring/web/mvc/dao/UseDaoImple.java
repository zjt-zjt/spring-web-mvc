package com.lanou.spring.web.mvc.dao;

import com.lanou.spring.web.mvc.bean.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class UseDaoImple implements UseDao {
    // @Autowired
    private JdbcTemplate jdbcTemplate;
/*
        public static  List<User> list = new ArrayList<>();
     static {
          list.add(new User("张三"));
          list.add(new User("老王"));
          list.add(new User("老李"));
     }
*/


    @Override
    public List<User> queryAll() {
        String sql = "select id,num,name from student";
             List<User>  list=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(User.class));
             return list;
    }

    @Override
    public int insertUser(User user) {
        String sql = "insert into student  (id, num,name)  value(?,?,?)";
         return   jdbcTemplate.update(sql,user.getId(),user.getNum(),user.getName() );
    }

    @Override
    public User findById(Integer id) {
        String sql = "select id,num,name from student where id=?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "delete from student where id = ?";
        return   jdbcTemplate.update(sql,id);

    }

    @Override
    public int update(User user) {
        String sql = "update student set name = ? where id=?";
        return  jdbcTemplate.update(sql,user.getName(),user.getId());
    }
}
