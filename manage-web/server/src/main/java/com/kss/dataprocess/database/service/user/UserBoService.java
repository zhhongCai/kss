package com.kss.dataprocess.database.service.user;

import com.github.pagehelper.Page;

import java.util.List;
import java.util.Objects;

import com.kss.commons.exceptions.ServiceException;
import com.kss.dataprocess.database.bo.user.UserBo;
import com.kss.dataprocess.database.po.UserPo;
import com.kss.dataprocess.database.repository.UserDatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBoService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDatabaseRepository userDatabaseRepository;

    public int updateUser(UserBo userBo) throws ServiceException {
        // 查询是否存在登陆ID相同的用户
        if (userBo.getCode() != null && !isUniqueLoginId(userBo)) {
			logger.info("登陆ID已存在");
            throw new ServiceException("登陆ID已存在");
        }
        final UserPo userPo = new UserPo();
        BeanUtils.copyProperties(userBo, userPo);
        //todo 修改用户是否修改相关记录
        return userDatabaseRepository.updateByPrimaryKeySelective(userPo);
    }

    public int deleteUser(Long id) {
        // 删除用户是否需要其他操作或者校验
        return userDatabaseRepository.deleteUserById(id);
    }

    public UserBo getUserByNameAndPwd(UserBo userBo) throws ServiceException {
        return getOneFromUserList(userBo);
    }

    public Page<UserBo> getUserListByPageAndFilter(Integer pageNo, Integer pageSize, String searchContent) {
        Page<UserPo> userPos = userDatabaseRepository.selectUserAsListByPageAndFilter(pageNo, pageSize, searchContent);
        Page<UserBo> userBoPage = new Page<>();
        userBoPage.setPages(userPos.getPages());
        userBoPage.setPageSize(pageSize);
        userBoPage.setTotal(userPos.getTotal());
        userPos.getResult().forEach(userPo -> {
            UserBo userBo = new UserBo();
            BeanUtils.copyProperties(userPo, userBo);
            userBoPage.add(userBo);
        });
        return userBoPage;
    }

    public Long saveUser(UserBo userBo) throws ServiceException {
        // 查询是否存在登陆ID相同的用户
        if (!isUniqueLoginId(userBo)) {
			logger.info("登陆ID已存在");
            throw new ServiceException("登陆ID已存在");
        }
        // 做保存
        final UserPo userPo = new UserPo();
        BeanUtils.copyProperties(userBo, userPo);
        return userDatabaseRepository.saveUser(userPo);
    }

    public boolean isUniqueLoginId(UserBo userBo) {
        UserPo userPo = new UserPo();
        userPo.setCode(userBo.getCode());
        Long id = userBo.getId();
        userPo = userDatabaseRepository.selectOne(userPo);
        return null == userPo || Objects.equals(id, userPo.getId());
    }

    public UserBo findUserByPo(UserBo userBo) throws ServiceException {
        return getOneFromUserList(userBo);
    }

    private UserBo getOneFromUserList(UserBo userBo) throws ServiceException {
        final UserPo userPo = new UserPo();
        BeanUtils.copyProperties(userBo, userPo);
        List<UserPo> userPoList = userDatabaseRepository.selectUserAsListByPo(userPo);
        if (userPoList.size() <= 0) {
			logger.info("用户不存在");
            throw new ServiceException("用户不存在");
        }
        UserBo returnUserBo = new UserBo();
        BeanUtils.copyProperties(userPoList.get(0), returnUserBo);
        return returnUserBo;
    }

    public UserBo getUserById(Long id) {
        final UserPo userPo = userDatabaseRepository.getUserById(id);
        if (null == userPo) {
            return null;
        }
        final UserBo userBo = new UserBo();
        BeanUtils.copyProperties(userPo, userBo);
        return userBo;
    }

    public int updateUserPwd(UserBo userBo) throws ServiceException {
        // 校验旧密码是否正确
        final UserBo backUserBo = new UserBo();
        BeanUtils.copyProperties(userBo, backUserBo);
        userBo.setPassword(userBo.getOldPwd());
        if (!checkUserPwdById(userBo)) {
			logger.info("用户旧密码不正确");
            throw new ServiceException("用户旧密码不正确");
        }
        final UserPo userPo = new UserPo();
        BeanUtils.copyProperties(backUserBo, userPo);
        return userDatabaseRepository.updateUsrPwd(userPo);
    }

    private boolean checkUserPwdById(UserBo userBo) {
        UserPo userPo = new UserPo();
        userPo.setId(userBo.getId());
        userPo.setPassword(userBo.getPassword());
        int counts = userDatabaseRepository.getCountOfUserRecords(userPo);
        return counts > 0;
    }
}
