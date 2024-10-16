package com.example.springboottyy.dto.mapper;

import com.example.springboottyy.dto.DeptDto;
import com.example.springboottyy.model.SysDept;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeptMapper {
    DeptDto toDto(SysDept dept);

    SysDept toEntity(DeptDto deptDto);
}
