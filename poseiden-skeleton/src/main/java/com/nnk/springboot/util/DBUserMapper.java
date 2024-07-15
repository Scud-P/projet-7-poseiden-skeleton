package com.nnk.springboot.util;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.domain.DTO.DBUserDTO;
import com.nnk.springboot.domain.parameter.DBUserParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DBUserMapper {
    DBUser toDBUser(DBUserParameter dbUserParameter);
    DBUserDTO toDBuserDTO(DBUser dbUser);
    DBUserParameter toDBUserParameter(DBUser dbUser);
}
