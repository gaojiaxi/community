package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param 注解用于给参数去别名
    // 如果只有一个参数， 并且在<if>里使用，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);


}
