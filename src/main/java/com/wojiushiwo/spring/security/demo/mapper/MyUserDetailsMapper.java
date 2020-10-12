package com.wojiushiwo.spring.security.demo.mapper;

import com.wojiushiwo.spring.security.demo.model.MyUserDetails;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MyUserDetailsMapper {

    /**
     * 根据userName查询用户信息
     *
     * @param userName
     * @return
     */
    MyUserDetails findByUserName(String userName);

    /**
     * 根据userName查询角色
     *
     * @param userName
     * @return
     */
    @Select("select role_code\n" +
            "from sys_role a\n" +
            "join sys_user_role b on a.id=b.role_id\n" +
            "join sys_user c on b.user_id=c.id\n" +
            "where c.username=#{userName}")
    List<String> findRoleByUserName(String userName);


    /**
     * 根据用户角色查询用户权限
     */
    @Select({
            "<script>",
            "SELECT url ",
            "FROM sys_menu m ",
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id ",
            "LEFT JOIN sys_role r ON r.id = rm.role_id ",
            "WHERE r.role_code IN ",
            "<foreach collection='roleCodes' item='roleCode' open='(' separator=',' close=')'>",
            "#{roleCode}",
            "</foreach>",
            "</script>"
    })
    List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);

}
