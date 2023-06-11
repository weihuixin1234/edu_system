package com.example.commonutils.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.common.annotation.Log;
import com.example.commonutils.entity.Menu;
import com.example.commonutils.entity.R;
import com.example.commonutils.entity.Role;
import com.example.commonutils.entity.User;
import com.example.commonutils.service.MenuService;
import com.example.commonutils.service.RoleService;
import com.example.commonutils.service.UserService;
import com.example.commonutils.utils.CodeUtil;
import com.example.commonutils.utils.JwtTokenUtil;
import com.example.commonutils.utils.RedisUtils;
import com.example.commonutils.utils.StringUtils;
import com.example.commonutils.utils.core.BaseController;
import com.example.commonutils.utils.poi.ExcelUtil;
import com.example.commonutils.utils.sign.Base64;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gqq
 * @since 2023-03-18
 */
@RestController
@CrossOrigin
@RequestMapping("/scy")
public class UserController extends BaseController {
    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    CodeUtil codeUtil;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RoleService roleService;

    private String tokenHead = "Bearer";


    @PostMapping("/list/{pageNum}/{pageSize}")
    public R getList(@PathVariable Integer pageNum , @PathVariable Integer pageSize , @RequestBody(required = false) User user){
        PageHelper.startPage(pageNum , pageSize);
        List<User> list = userService.getUserList(user);
        PageInfo<User> userPageInfo = new PageInfo<>(list);
        return R.ok().data("result" ,userPageInfo);
    }

    @GetMapping("/userList")
    public R getAllUserList(User user){
        List<User> list = userService.getUserList(user);
        PageInfo<User> userPageInfo = new PageInfo<>(list);
        return R.ok().data("result" ,   userPageInfo );
    }

    @ApiOperation("判断用户名密码")
    @PostMapping("/login")
    public R login(@RequestBody(required = false) User user , HttpServletRequest request , HttpServletResponse response){
        String username = user.getName();
        String code = user.getCode().toUpperCase();
        String redisCode = "";
        try{
            redisCode = redisUtils.get(user.getUuid()).toString().toUpperCase();
        }catch (Exception e){
            System.out.println("空指针异常");
            e.printStackTrace();
            return R.error().message("验证码失效");
        }
        //先判断验证码code是否正确 1、先看session里面是否有验证码 2、看传过来的验证码是否和session中的验证码一致
        if(redisCode == null || !code.equals(redisCode)){
            return R.error().message("code error");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name" , username);
        //wrapper.eq("pwd" , password);
        List<User> list = userService.list(wrapper);
        if(list.size() > 0){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if(!passwordEncoder.matches(user.getPwd() , list.get(0).getPwd())){
                return R.ok().data("data1" , null).data("data2" , 201);
            }
            User person = list.get(0);
            //通过类名可以看出来，用户名密码进行认证,就是我们见的最多的认证方式通过用户名和密码进行登录
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(person, null, person.getAuthorities());
            /**
             * 上一步返回了Authentication对象就使用SecurityContextHolder.getContext().setAuthentication方法存储
             * 该对象，其它过滤器中会通过SecurityContextHolder来获取当前用户信息
             */
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //生成token
            String token = jwtTokenUtil.generateToken(person);
            HashMap<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token" , token);
            tokenMap.put("tokenHead" , tokenHead);
            //对一些加密字段进行解密

            return R.ok().data("data1" , person).data("data2" ,200).data("data" , tokenMap);
        }else {
            return R.ok().data("data1" , null).data("data2" , 201);
        }

    }

    @GetMapping("/code")
    public R code(HttpServletRequest request , HttpServletResponse response) throws IOException {
        response.setHeader("pragma" , "no-cache");
        response.setHeader("cache-control" , "no-cache");
        response.setHeader("expires" , "0");

        BufferedImage image = codeUtil.getImage();
        String codeKey = UUID.randomUUID().toString();
        String text = codeUtil.getCodeText();
        redisUtils.set(codeKey , text , (long)60 , TimeUnit.SECONDS);
        //HttpSession session = request.getSession(true);
        //session.setAttribute("code" , text);
        //Object code = session.getAttribute("code");
        //ServletOutputStream out = response.getOutputStream();
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        CodeUtil.output(image , os);
        //out.flush();
        //out.close();
        return R.ok().data("codeKey" , codeKey).data("img" , Base64.encode(os.toByteArray()));
    }

    @GetMapping("/info")
    public R getInfo(@RequestParam String token){
        //System.out.println(params);
        String authToken = token.substring(tokenHead.length());
        String username = jwtTokenUtil.getUserNameFormToken(authToken);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name" , username);
        List<User> list = userService.list(wrapper);
        if(list.size() > 0) {
            HashMap<String, Object> map = new HashMap<>();
            /**
             * 'editor-token': {
             *     roles: ['editor'],
             *     introduction: 'I am an editor',
             *     avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
             *     name: 'Normal Editor'
             *   }
             */
            //map.put("roles" , "editor");
            //map.put("introduction" , "I am an eidtor");
            map.put("avatar" , list.get(0).getAvatar());
            map.put("username" , list.get(0).getName());
            User person = list.get(0);
            List<Menu> menuList = menuService.getAllMenuByUserId(Integer.parseInt(person.getId()));
            map.put("menuList" , menuList);
            return R.ok().data("map" , map);
            //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //if (!passwordEncoder.matches(user.getPwd(), list.get(0).getPwd())) {
            //    return R.ok().data("data1", null).data("data2", 201);
            //}

        }
        return R.ok().data("data1", null).data("data2", 201);
    }
    @PostMapping("/logout")
    public R Logout(){
        return R.ok();
    }

    @GetMapping(value = {"/user" , "/user/{userId}"})
    public R getUserAndRole(@PathVariable(value = "userId" , required = false) Long userId){
        List<Role> list = roleService.getAllRoleList();
        User user = new User();
        Role role = new Role();
        if(StringUtils.isNotNull(userId)){
            user = userService.getById(userId);
            if(user.getRole() != null){
                role = roleService.getUserAndRoleById(user.getRole());
            }
        }
        return R.ok().data("user" , user).data("roleOptions" , list).data("role" , role);
    }

    @Log(modul = "用户模块" , type = "ADD" , desc = "添加用户")
    @PostMapping("/add")
    public R addUser(@Validated @RequestBody User user){
        String username = user.getName();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name" , username);
        List<User> list = userService.list(wrapper);
        if(list.size() != 0){
            return R.error().data("message" , username + "登录账号已存在");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = user.getPwd();
        user.setPwd(passwordEncoder.encode(password));
        userService.save(user);
        return R.ok().data("message" , "新增成功");
    }

    @Log(modul = "用户模块" , type = "UPDATE" , desc = "修改用户")
    @PutMapping("/update")
    public R updateUser(@Validated @RequestBody User user){
        //String username = user.getName();
        //QueryWrapper<User> wrapper = new QueryWrapper<>();
        //wrapper.eq("name" , username);
        //List<User> list = userService.list(wrapper);
        if(!userService.checkUserNameUnique(user)){
            return R.error().data("message" , user.getName() + "修改账号已存在");
        }
        userService.updateById(user);
        return R.ok().data("message", "修改成功");

    }
    //@Log(modul = "用户模块" , type = "DELETE" , desc = "删除用户")
    @DeleteMapping("/delete/{userId}")
    public Boolean deleteUser(@PathVariable(value = "userId") Long userId){

        //return R.ok().data("message" , "删除成功");
        return userService.deleteUserById(userId);
    }

    @PutMapping("/changeStatus")
    public R changeStatus(@RequestBody User user){
        String userId = user.getId();
        String status = user.getStatus();
        userService.updateUserStatus(userId , status);
        return R.ok().data("message" , "操作成功");
    }

    @Log(modul = "用户管理" , type = "EXPORT" , desc = "导出用户数据")
    @PostMapping("/export")
    public void export(@RequestBody User user , HttpServletResponse response ){
        List<User> userList = userService.getUserList(user);
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        util.exportExcel(response , userList  , "用户数据");
    }

    @Log(modul = "用户管理" , type = "IMPORT" ,desc = "导入数据")
    @PostMapping("/importData")
    public R importData(MultipartFile file , boolean updateSupport) throws Exception {
        ExcelUtil<User> util = new ExcelUtil<>(User.class);
        List<User> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList,updateSupport,operName);
        return R.ok().data("message" , message);
    }

}

