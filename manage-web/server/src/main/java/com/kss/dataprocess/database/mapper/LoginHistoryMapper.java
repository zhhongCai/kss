package com.kss.dataprocess.database.mapper;

import com.kss.dataprocess.database.po.LoginHistoryPo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface LoginHistoryMapper extends Mapper<LoginHistoryPo> {

    @Select("select * from login_history where user_id= #{userId} order by id desc limit 1")
    public LoginHistoryPo getLastLogin(Long userId);

}
