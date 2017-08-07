package com.kss.dataprocess.web;

import com.google.common.collect.Maps;
import com.kss.dataprocess.application.bean.file.response.ResponseFile;
import com.kss.dataprocess.application.bean.user.response.ResponseUserDto;

import java.util.Map;

/**
 * Created by caizh on 17-8-5.
 */
public class RestTempBean {
    //<ResponseUserDto>
    Map<Long, ResponseUserDto> users = Maps.newConcurrentMap();
    Map<String, ResponseFile> uploadfiles = Maps.newConcurrentMap();

    public Map<Long, ResponseUserDto> getUsers() {
        return users;
    }

    public Map<String, ResponseFile> getUploadfiles() {
        return uploadfiles;
    }
}
