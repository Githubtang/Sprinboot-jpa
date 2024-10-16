package com.example.springboottyy.dto.mapper;

import com.example.springboottyy.dto.UserDto;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.model.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "dept.id", target = "deptId")
    @Mapping(source = "dept.deptName", target = "deptName")
    @Mapping(expression = "java(getRoleIds(user.getRoles()))", target = "roleIds")
    UserDto toDto(SysUser user);

    SysUser toEntity(UserDto userDto);

    default List<Long> getRoleIds(Set<SysRole> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream().map(SysRole::getId)
                .collect(Collectors.toList());

    }
}