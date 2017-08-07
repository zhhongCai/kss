package com.kss.dataprocess.application.service.file;

import com.kss.commons.FileAttachData;
import com.kss.commons.ResponseFileAttach;
import com.kss.commons.util.BeanUtils;
import com.kss.dataprocess.database.po.FileAttachPo;
import com.kss.dataprocess.database.service.login.FileServiceImpl;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileHandleService {
    @Autowired
    FileServiceImpl fileService;
    @Value("${fdfs.fileUrl}")
    private String fileUrl;

    public void addBatch(List<FileAttachData> fileAttachList) throws InstantiationException, IllegalAccessException {
        if(null==fileAttachList){
            return;
        }
        List<FileAttachPo> fileAttachPoList = BeanUtils.convertList(fileAttachList,FileAttachPo.class);
        fileService.addBatch(fileAttachPoList);
    }
    public List<ResponseFileAttach> getFileAttachList(List<String> fileKeys) throws InstantiationException, IllegalAccessException {
        List<FileAttachPo> fileAttachList=fileService.getFileAttachs(fileKeys);
        List<ResponseFileAttach> responseFileAttachList = Lists.newArrayList();
        if(null == fileAttachList){
            return responseFileAttachList;
        }
        responseFileAttachList =  BeanUtils.convertList(fileAttachList, ResponseFileAttach.class);
        responseFileAttachList.forEach(responseFileAttach->{
            responseFileAttach.setFileUrl(fileUrl);
        });
        return responseFileAttachList;
    }
}
