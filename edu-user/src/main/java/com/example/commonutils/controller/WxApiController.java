package com.example.commonutils.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.entity.R;
import com.example.commonutils.entity.User;
import com.example.commonutils.exception.ServiceException;
import com.example.commonutils.service.UserService;
import com.example.commonutils.utils.JwtTokenUtil;
import com.example.commonutils.utils.constant.ConstantWxCodeUtil;
import com.example.commonutils.utils.http.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private String tokenHead = "Bearer";

    @GetMapping("login")
    @ResponseBody
    public R genQrConnect(HttpSession session){
        //微信开放平台授权
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //回调地址 获取业务服务器重定向地址
        String redirectUrl = ConstantWxCodeUtil.WX_OPEN_REDIRECT_URL;
        try{
            redirectUrl = URLEncoder.encode(redirectUrl , "UTF-8"); //url编码
        }catch (UnsupportedEncodingException e){
            throw new ServiceException(e.getMessage() , 20001);
        }

        //防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-" , "");//一般情况下会使用一个随机数
        //String state = "imhelen" ; //填写在ngrok的前置域

        //采用redis等进行缓存state使用sessionId为key30分钟后过期，可配置
        //键:"wechar-open-state-" + httpServletRequest.getSession().getId()
        //值:state
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxCodeUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );
        return R.ok().data("baseUrl" , qrcodeUrl) ;
    }

    /**
     * 1、获取回调参数
     * 2、从redis中读取state进行比对，异常则拒绝调用
     * 3、向微信的授权服务发起请求，使用临时票据换取access_token
     * 4、使用上一步后去的openid查询数据库，判断当前用户是否已注册，如果已注册则直接进行登录操作
     * 5、如果未注册，则使用openid和access_token向微信的资源服务器发起请求，请求获取微信的用户信息
     *  5.1、将获取到的用户信息存入数据库
     *  5.2、然后进行登录操作
     * @param code
     * @param state
     * @return
     */
    //获取扫描人信息，添加数据
    @GetMapping("callback")
    //@ResponseBody
    public String callback(String code , String state){

        try {
            //System.out.println("code: " +  code);
            //System.out.println("state: " + state);

            /**
             * 从redis中将state获取出来，和当前传入的state作比较
             * 如果一致则放行，如果不一致则抛出异常，非法访问
             * 向认证服务器发送请求换取access_token
             *
             */
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxCodeUtil.WX_OPEN_APP_ID,
                    ConstantWxCodeUtil.WX_OPEN_APP_SECRET,
                    code
            );
            //请求这个拼接好的地址，得到返回两个值access_token和openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //System.out.println("accessTokenInfo: " + accessTokenInfo);

            //从accessTokenInfo字符串获取出来两个值 access_token和openid
            //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
            HashMap mapAccessToken = JSONObject.parseObject(accessTokenInfo, HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");

            //把扫描人信息添加数据库里面
            //判断数据表里面是否存在相同微信信息，根据openid判断
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("openid" , openid);
            //wrapper.eq("pwd" , password);
            User user = userService.getOne(wrapper);
            //User user = list.get(0);
            //System.out.println(user);
            if(user == null){
                //拿到得到的access_token和openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //System.out.println("userInfo: " + userInfo);

                //获取返回userInfo字符串扫描人信息
                HashMap userInfoMap = JSONObject.parseObject(userInfo, HashMap.class);
                String nickname = (String)userInfoMap.get("nickname");
                String headimgurl = (String)userInfoMap.get("headimgurl");
                user = new User();
                user.setOpenid(openid);
                user.setName(nickname);
                user.setNickname(nickname);
                user.setAvatar(headimgurl);
                user.setRole("2");
                userService.save(user);
            }

            //通过类名可以看出来，用户名密码进行认证,就是我们见的最多的认证方式通过用户名和密码进行登录
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            /**
             * 上一步返回了Authentication对象就使用SecurityContextHolder.getContext().setAuthentication方法存储
             * 该对象，其它过滤器中会通过SecurityContextHolder来获取当前用户信息
             */
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //生成token
            String token = tokenHead + jwtTokenUtil.generateToken(user);
            System.out.println(token);
            //HashMap<String, String> tokenMap = new HashMap<>();
            //tokenMap.put("token" , token);
            //tokenMap.put("tokenHead" , tokenHead);

            return "redirect:http://localhost:8081?token=" + token;
            //return R.ok().data("data1" , user).data("data2" ,200).data("data" , tokenMap);
        } catch (Exception e) {
            throw new ServiceException("登录失败" , 20001);
        }

        //return "redirect:http://localhost:3000";
    }
}
