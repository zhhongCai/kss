package com.kss.manage.database.service.login;

import com.kss.manage.database.mapper.FileMapper;
import com.kss.manage.database.po.FileAttachPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl{

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileMapper fileMapper;

    public void add(FileAttachPo fileAttachPo){
        fileMapper.insert(fileAttachPo);
    }

    public FileAttachPo add(String fileKey){
        FileAttachPo fileAttachPo = new FileAttachPo();
        fileAttachPo.setFileKey(fileKey);
        fileAttachPo = fileMapper.selectOne(fileAttachPo);
        return fileAttachPo;
    }
    public List<FileAttachPo> getFileAttachs(List<String> fileKeys){
        if(null==fileKeys){
            return null;
        }
        List<FileAttachPo> fileAttachPoList = new ArrayList<>();
        fileKeys.forEach(fileKey->{
            FileAttachPo fileAttachPo = new FileAttachPo();
            fileAttachPo.setFileKey(fileKey);
            fileAttachPo = fileMapper.selectOne(fileAttachPo);
            if(null == fileAttachPo)return;
            fileAttachPoList.add(fileAttachPo);
        });
        return fileAttachPoList;
    }
    public FileAttachPo getFileAttachByFileKey(String fileKey){
        FileAttachPo fileAttachPo = new FileAttachPo();
        fileAttachPo.setFileKey(fileKey);
        fileAttachPo = fileMapper.selectOne(fileAttachPo);
        return fileAttachPo;
    }

    public void addBatch(List<FileAttachPo> fileAttachPoList) {
        if(null==fileAttachPoList){
            return;
        }
        fileAttachPoList.forEach(fileAttachPo -> {
            //表中的filekey不能重复，所以新增前先删除旧的。
            Example example = new Example(FileAttachPo.class);
            example.createCriteria().andEqualTo("fileKey",fileAttachPo.getFileKey());
            fileMapper.deleteByExample(example);
            fileMapper.insert(fileAttachPo);
        });
    }
}
