package com.example.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.commonutils.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-03-18
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> getUserList(User user);

    User checkUserNameUnique(String name);

    boolean deleteUserById(Long userId);

    void updateUserStatus(String userId, String status);

    User selectUserByUserName(String name);

    Boolean insertUser(User user);

    Boolean updateUser(User user);
}
