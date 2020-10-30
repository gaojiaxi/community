package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;
    @RequestMapping("hello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring Boot!";
    }

    @RequestMapping("data")
    @ResponseBody
    public String getDate(){
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // obtain request data
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));

        // return response data
        response.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = response.getWriter();)
        {
            writer.write("<h1>now coder community</h1>");

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // GET method

    // method1: obtain parameters using RequestParam
    // /students?current=1 & limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam(name = "current", required = false, defaultValue = "1") int current,
                              @RequestParam(name = "limit", required = false, defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }


    // method2: obtain parameters using PathVariable
    // /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    //Post请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // response html data

    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","zhang san");
        mav.addObject("age",30);
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","UBC");
        model.addAttribute("age",100);
        return "/demo/view";
    }



    // 响应JSON数据(异步请求）
    // Java对象 -> JSON字符串 -> JS对象

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp(){
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","zhang san");
        emp.put("age",23);
        emp.put("salary",8000);
        return emp;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps(){
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","zhang san");
        emp.put("age",23);
        emp.put("salary",8000);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","zhang si");
        emp.put("age",24);
        emp.put("salary",9000);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","zhang wu");
        emp.put("age",25);
        emp.put("salary",10000);
        list.add(emp);
        return list;
    }

    //cookie example
    @RequestMapping(path="/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        // new a cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // set cookie valid path
        cookie.setPath("/community/alpha");
        // set cookie expire time
        cookie.setMaxAge(60 * 10);
        // send cookie
        response.addCookie(cookie);
        return "set cookie";

    }

    //cookie get example
    @RequestMapping(path="/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get cookie";
    }

    // session example
    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set session";
    }

    // session get example
    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }





}
