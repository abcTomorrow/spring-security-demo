package com.wojiushiwo.spring.security.demo.smscode;

import com.wojiushiwo.spring.security.demo.mapper.MyUserDetailsMapper;
import com.wojiushiwo.spring.security.demo.model.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author myk
 * @create 2020/10/24 下午4:51
 */
@Component("smsUserDetailService")
public class SmsUseDetailServiceImpl implements UserDetailsService {
    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        //获取用户信息
        MyUserDetails myUserDetails = myUserDetailsMapper.findByUserMobile(mobile);

        if (Objects.isNull(myUserDetails)) {
            throw new RuntimeException("用户手机号不存在");
        }

        String username = myUserDetails.getUsername();
        //获取用户角色
        List<String> roles = myUserDetailsMapper.findRoleByUserName(username);
        //根据角色查找到用户的权限
        List<String> authorities = myUserDetailsMapper.findAuthorityByRoleCodes(roles);

        //为角色添加上ROLE_前缀
        roles = roles.stream().map(s -> "ROLE_" + s).collect(Collectors.toList());

        //用户角色 也是一种特殊的权限
        authorities.addAll(roles);

        //转成用逗号分隔的字符串，为用户设置权限标识
        myUserDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authorities)));

        return myUserDetails;
    }
}
