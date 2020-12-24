package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        // null value exception
        if(user==null){
            throw new IllegalArgumentException("user can not be null!");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","user name can not be empty!");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","password can not be empty!");
            return map;
        }

        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","email address can not be empty!");
            return map;
        }

        // check username availability
        User u = userMapper.selectByName(user.getUsername());
        if (u!=null){
            map.put("usernameMsg","username already exists!");
            return map;
        }

        // check email availability
        String test = user.getEmail();
        u = userMapper.selectByEmail(user.getEmail());
        if (u!=null){
            map.put("emailMsg","email address already exists!");
            return map;
        }

        // register user
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        // send activation email
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/commnunity/activation/userId/activationCode
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();

        context.setVariable("url", url);

        String content = templateEngine.process("/mail/activation", context);

        mailClient.sendMail(user.getEmail(), "Activation email from nowcoder", content);
        return map;
    }
    public int activation(int userId, String code){
            User user = userMapper.selectById(userId);
            if (user.getStatus() == 1){
                return ACTIVATION_REPEAT;
            }
            else if(user.getActivationCode().equals(code)){
                userMapper.updateStatus(userId, 1);
                return ACTIVATION_SUCCESS;
            }else{
                return ACTIVATION_FAILURE;
            }

    }

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg","username can not be empty!");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","password can not be empty!");
            return map;
        }

        // verify account
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsg", "username does not exist!");
            return map;
        }

        // check non-activated user
        if (user.getStatus()==0){
            map.put("usernameMsg","This account is not activated yet, please check your email for activation!");
            return map;
        }

        // verify password
        password = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","wrong password!");
            return map;
        }

        //generate login ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        // loginTicketMapper.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);



        map.put("ticket",loginTicket.getTicket());
        return map;
    }
    public void logout(String ticket){
        //loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket){
//        return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId, headerUrl);
    }

    public int updatePassword(int userId, String password){
        return userMapper.updatePassword(userId, password);
    }

    public User findUserByName(String username){
        return userMapper.selectByName(username);
    }


}

