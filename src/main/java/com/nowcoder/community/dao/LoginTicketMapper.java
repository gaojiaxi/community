package com.nowcoder.community.dao;


import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
@Deprecated
public interface LoginTicketMapper {


    @Insert({
            "INSERT INTO login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);


    @Select({
            "SELECT id,user_id,ticket,status,expired ",
            "FROM login_ticket WHERE ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);


    @Update({
            "UPDATE login_ticket set status=#{status} ",
            "WHERE ticket=#{ticket}"
    })
    int updateStatus(String ticket, int status);
}
