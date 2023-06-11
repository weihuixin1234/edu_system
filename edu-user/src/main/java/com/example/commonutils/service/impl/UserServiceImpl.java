package com.example.commonutils.service.impl;

import com.example.commonutils.entity.User;
import com.example.commonutils.exception.ServiceException;
import com.example.commonutils.mapper.UserMapper;
import com.example.commonutils.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.commonutils.utils.StringUtils;
import com.example.commonutils.utils.constant.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.validation.Validator;
import com.example.commonutils.annotation.bean.BeanValidators;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gqq
 * @since 2023-03-18
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    Validator validator;
    @Override
    public List<User> getUserList(User user) {
        return userMapper.getUserList(user);

    }

    @Override
    public boolean checkUserNameUnique(User user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : Long.parseLong(user.getId());
        User info = userMapper.checkUserNameUnique(user.getName());
        if (StringUtils.isNotNull(info) && Long.parseLong(info.getId()) != userId)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public Boolean deleteUserById(Long userId) {
        return userMapper.deleteUserById(userId);
    }

    @Override
    public Boolean updateUserStatus(String userId, String status) {
        userMapper.updateUserStatus(userId , status);
        return true;
    }

    @Override
    public String importUser(List<User> userList, boolean updateSupport, String operName) {

        if(StringUtils.isNull(userList)  || userList.size() == 0){
            throw new ServiceException("导入的用户数据不能为空 !!");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (User user : userList) {
            try{
                User u = userMapper.selectUserByUserName(user.getName());
                if(StringUtils.isNull(u)){
                    BeanValidators.validateWithException(validator , user);
                    //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    //user.setPwd(passwordEncoder.encode(user.getPwd()));
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号" + user.getName() + "导入成功");
                }else if(updateSupport){
                    BeanValidators.validateWithException(validator , user);
                    //checkUserNameUnique(u);
                    user.setId(u.getId());
                    userMapper.updateUser(u);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号" + user.getName() + "更新成功");
                }else{
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号" + user.getName() + "已存在");
                }
            }catch (Exception e){
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号" + user.getName() + "导入失败:";
                failureMsg.append(msg + e.getMessage());
                log.error(msg , e);
            }
        }
        if(failureNum > 0){
            failureMsg.insert(0 , "很抱歉 ， 导入失败！共" + failureNum + "条数据格式不正确， 错误如下：");
            //throw new ServiceException(failureMsg.toString());
            return failureMsg.toString();
        }else{
            successMsg.insert(0 , "恭喜您， 数据已全部导入成功！共" + successNum + "条，数据如下：");
        }
        return successMsg.toString();
    }
}
