package com.kss.dataprocess.database.mapper;

import com.kss.dataprocess.database.po.UserPo;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UserPo> {

    @Update("UPDATE base_user set modify_user=#{modifyUser}, password=#{password}, modify_time=#{modifyTime} where id=#{id}")
    int updateUserPwd(UserPo userPo);
}
