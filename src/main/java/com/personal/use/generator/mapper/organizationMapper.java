package com.personal.use.generator.mapper;

import com.personal.use.generator.pojo.organization;
import com.personal.use.generator.pojo.organizationExample;
import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface organizationMapper {
    int countByExample(organizationExample example);

    int deleteByExample(organizationExample example);

    int insert(organization record);

    int insertSelective(organization record);

    List<organization> selectByExample(organizationExample example);

    int updateByExampleSelective(@Param("record") organization record, @Param("example") organizationExample example);

    int updateByExample(@Param("record") organization record, @Param("example") organizationExample example);
}