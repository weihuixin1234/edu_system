package com.example.commonutils.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.commonutils.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gqq
 * @since 2023-03-18
 */
public interface UserService extends IService<User> {

    List<User> getUserList(User user);

    boolean checkUserNameUnique(User user);

    Boolean deleteUserById(Long userId);

    Boolean updateUserStatus(String userId, String status);

    String importUser(List<User> userList, boolean updateSupport, String operName);
}
