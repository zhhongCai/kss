package com.kss.manage.web.user;

import com.google.common.collect.Lists;
import com.kss.commons.ResponsePage;
import com.kss.commons.security.RoleEnum;
import com.kss.commons.util.AjaxResult;
import com.kss.commons.util.BeanUtil;
import com.kss.commons.util.UserUtils;
import com.kss.manage.dto.UserDto;
import com.kss.manage.po.UserPo;
import com.kss.manage.po.UserRolePo;
import com.kss.manage.repository.UserRepository;
import com.kss.manage.repository.UserRoleRepository;
import com.kss.manage.security.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by caizh on 17-9-5.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("roleList", RoleEnum.values());
        UserPo user = (UserPo) UserUtils.getPricipal();
        model.addAttribute("user", user);
        return "user/list";
    }

    @RequestMapping("/tableData")
    @ResponseBody
    public ResponsePage<UserDto> tableData(String username, Integer page, Integer limit) {
        if(page > 0) {
            page = page -1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(page, limit, sort);
        Page<UserPo> userPage = null;
        if(StringUtils.isBlank(username)) {
            userPage = userRepository.findAll(pageRequest);
        } else {
            userPage = userRepository.findByUsernameLike(username, pageRequest);
        }
        ResponsePage<UserDto> data = new ResponsePage<>();
        if(userPage.hasContent()) {
            data.setData(Lists.newArrayList(BeanUtil.fromBeans(userPage.getContent(), UserDto.class)));
        }
        data.setCount((int)userPage.getTotalElements());
        data.setCode(ResponsePage.SUCCESS_CODE);
        return data;
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxResult<String> saveOrUpdate(UserPo userPo) {
        AjaxResult<String> result = new AjaxResult<>();

        if(userPo.getId() != null) {
            UserPo dbUser = userRepository.findOne(userPo.getId());
            dbUser.setUsername(userPo.getUsername());
            dbUser.setCode(userPo.getCode());
            dbUser.setPhone(userPo.getPhone());

            boolean ret = userService.save(dbUser);
            if(!ret) {
                result.setCode(AjaxResult.ERROR_CODE);
                result.setMsg("更新用户失败");
                return result;
            }
        } else {
            //TODO
            userPo.setCreateUser("admin");
            userPo.setCreateTime(new Date());
            userPo.setAccountNonExpired(true);
            userPo.setAccountNonLocked(true);
            userPo.setCredentialsNonExpired(true);
            userPo.setEnabled(true);
            boolean ret = userService.save(userPo);
            if(!ret) {
                result.setCode(AjaxResult.ERROR_CODE);
                result.setMsg("保存用户失败");
                return result;
            }
        }
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult<String> delete(String ids) {
        AjaxResult<String> result = new AjaxResult<>();
        List<UserPo> userList = Lists.newArrayList();
        for (String id : ids.split(",")) {
            UserPo user = new UserPo();
            user.setId(Long.parseLong(id));
            userList.add(user);
        }
        userRepository.delete(userList);

        return result;
    }

    @RequestMapping("/saveOrUpdateRole")
    @ResponseBody
    public AjaxResult<String> saveOrUpdateRole(UserPo user) {
        AjaxResult<String> result = new AjaxResult<>();

        UserPo dbUser = userRepository.findOne(user.getId());
        if(user == null) {
            result.setCode(AjaxResult.ERROR_CODE);
            result.setMsg("找不到用户");
            return result;
        }

        UserRolePo rolePo = dbUser.getRole();
        if(rolePo == null) {
            rolePo = new UserRolePo();
            rolePo.setRole(user.getRole().getRole());
            rolePo.setUser(user);
            rolePo.setCreateTime(new Date());
            rolePo.setCreateUser(UserUtils.getUsername());
        } else {
            rolePo.setRole(user.getRole().getRole());
            rolePo.setModifyTime(new Date());
            rolePo.setModifyUser(UserUtils.getUsername());
        }
        UserRolePo ret = userRoleRepository.save(rolePo);
        if(ret == null) {
            result.setCode(AjaxResult.ERROR_CODE);
            result.setMsg("保存分配给用户的角色失败");
        }

        return result;
    }

}
