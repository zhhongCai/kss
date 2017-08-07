package com.kss.dataprocess.database.repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kss.dataprocess.database.mapper.UserMapper;
import com.kss.dataprocess.database.po.UserPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class UserDatabaseRepository {

    @Autowired
    private UserMapper userPoMapper;

    public List<UserPo> selectUserAsListByPo(UserPo userPo) {
        return userPoMapper.select(userPo);
    }

    public Page<UserPo> selectUserAsListByPageAndFilter(Integer pageNo, Integer pageSize, String searchContent) {
        Example userExample = new Example(UserPo.class);
        if (StringUtils.isNotBlank(searchContent)) {
            StringBuffer sb = new StringBuffer("code like '%").append(searchContent)
                    .append("%' or name like '%").append(searchContent).append("%' or phone like '%")
                    .append(searchContent).append("%' or department like '%").append(searchContent).append("%'");
            userExample.createCriteria().andCondition(sb.toString());
        }
        userExample.setOrderByClause("modify_time desc");
        PageHelper.startPage(pageNo, pageSize);
        return (Page<UserPo>) userPoMapper.selectByExample(userExample);
    }

    public int getCountOfUserRecords(UserPo userPo) {
        return userPoMapper.selectCount(userPo);
    }

    public int updateByPrimaryKeySelective(UserPo userPo) {
        return userPoMapper.updateByPrimaryKeySelective(userPo);
    }

    public int deleteUserById(Long id) {
        return userPoMapper.deleteByPrimaryKey(id);
    }

    public Long saveUser(UserPo userPo) {
        userPoMapper.insertSelective(userPo);
        return userPo.getId();
    }

    public UserPo getUserById(Long id) {
        return userPoMapper.selectByPrimaryKey(id);
    }

    public int updateUsrPwd(UserPo userPo) {
        return userPoMapper.updateUserPwd(userPo);
    }

    public UserPo selectOne(UserPo userPo) {
        return userPoMapper.selectOne(userPo);
    }
}
