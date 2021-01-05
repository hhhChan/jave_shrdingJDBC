package com.can.config;


import com.can.shiro.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //shiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro内置过滤器 anon无需认证 authc必须认证 user拥有记住我功能才能用 perms 拥有某个权限 role拥有某个角色权限
        //拦截
        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权

        filterMap.put("/emps","perms[user:xx]");
        filterMap.put("/emps/**","perms[user:xx]");

        filterMap.put("/","anon");
        filterMap.put("/user/login","anon");
        filterMap.put("/user/logout", "logout");
        filterMap.put("/**","authc");
        bean.setFilterChainDefinitionMap(filterMap);
        //设置登录请求
        bean.setLoginUrl("/");
        //未授权页面
        bean.setUnauthorizedUrl("/noauth");
        return  bean;
    }


    //DefaultWebSecurityManager
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);
        return  securityManager;
    }


    //realm
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }



}
