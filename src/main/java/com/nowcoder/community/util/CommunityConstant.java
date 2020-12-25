package com.nowcoder.community.util;

public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * default login ticket expire time
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * longer ticket expire time with rememberMe clicked
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * entity type: post
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * entity type: comment
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * entity type: user
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * topics type: comment
     */
    String TOPIC_COMMENT = "comment";

    /**
     * topics type: like
     */
    String TOPIC_LIKE = "like";

    /**
     * topics type: follow
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * System User Id
     */
    int SYSTEM_USER_ID = 1;
}
