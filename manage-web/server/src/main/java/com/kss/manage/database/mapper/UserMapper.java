package com.kss.manage.database.mapper;

import com.kss.manage.database.po.UserPo;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UserPo> {

    @Update("UPDATE base_user set modify_user=#{modifyUser}, password=#{password}, modify_time=#{modifyTime} where id=#{id}")
    int updateUserPwd(UserPo userPo);
}
