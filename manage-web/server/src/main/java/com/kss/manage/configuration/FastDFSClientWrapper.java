package com.kss.manage.configuration;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.io.ByteStreams;
import com.kss.manage.application.bean.file.response.ResponseFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Component
public class FastDFSClientWrapper {

    private final Logger logger = LoggerFactory.getLogger(FastDFSClientWrapper.class);

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 文件地址
     */
    @Value("${fdfs.fileUrl}")
    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
   public ResponseFile uploadFile(MultipartFile file) throws IOException {
        ResponseFile responseFile = null;
        if (!file.isEmpty()) {
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
            responseFile = new ResponseFile();
            responseFile.setFileKey(storePath.getFullPath());
            responseFile.setFileName(file.getOriginalFilename());
            responseFile.setFileUrl(fileUrl);
        }
        return responseFile;
    }

    public ResponseFile uploadFile(File file) throws IOException {
        ResponseFile responseFile = null;
        FileInputStream inputStream = null;
        try {
            if (file.exists()) {
                inputStream = new FileInputStream(file);
                 StorePath storePath = storageClient.uploadFile(inputStream,file.length(), FilenameUtils.getExtension(file.getName()), null);
                 responseFile = new ResponseFile();
                 responseFile.setFileKey(storePath.getFullPath());
                 responseFile.setFileName(file.getName());
                 responseFile.setFileUrl(fileUrl);
            }
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
        return responseFile;
    }

    /**
     * 上传文件
     * @param files 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public List<ResponseFile> uploadFiles(MultipartFile[] files) throws IOException {
        List<ResponseFile> fileList = new ArrayList<ResponseFile>();
        for(int i=1; i<=files.length; i++) {
            if (!files[i-1].isEmpty()) {
                StorePath storePath = storageClient.uploadFile(files[i-1].getInputStream(), files[i-1].getSize(), FilenameUtils.getExtension(files[i-1].getOriginalFilename()), null);
                ResponseFile responseFile = new ResponseFile();
                responseFile.setFileKey(storePath.getFullPath());
                responseFile.setFileName(files[i-1].getOriginalFilename());
                responseFile.setFileUrl(fileUrl);
                fileList.add(responseFile);
            }
        }
        return fileList;
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(String fileKey) {
        return fileUrl+fileKey;
    }

    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
    }

    public void downloadFile(String fileKey, OutputStream os) throws IOException {
        StorePath sp = StorePath.praseFromUrl(fileKey);
        String groupName = sp.getGroup();
        String pathName = sp.getPath();
        storageClient.downloadFile(groupName, pathName, ins -> {
            ByteStreams.copy(ins, os);
            return pathName;
        });
    }
    public void downloadFile(String fileKey, Path tofile) throws IOException {
        StorePath sp = StorePath.praseFromUrl(fileKey);
        String groupName = sp.getGroup();
        String pathName = sp.getPath();
        storageClient.downloadFile(groupName, pathName, ins -> {
                    Files.copy(ins, tofile);
            return pathName;
        });
    }

}

