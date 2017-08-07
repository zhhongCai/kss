package com.kss.dataprocess.web.file;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.kss.commons.util.DateUtil;
import com.kss.dataprocess.application.bean.file.response.ResponseFile;
import com.kss.dataprocess.configuration.FastDFSClientWrapper;
import com.kss.dataprocess.database.service.login.FileServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FastDFSClientWrapper wrapper;
    @Autowired
    private FileServiceImpl fileService;

    @ApiOperation(value = "附件上传", notes = "附件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    List<ResponseFile> upload(@ApiParam(name = "上传的文件对象", required = true) MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("begin to upload...");
        Iterator<String> itr = request.getFileNames();
        List<ResponseFile> responseFiles = new ArrayList<ResponseFile>();
        while (itr.hasNext()) {
            MultipartFile mpf = request.getFile(itr.next());
            ResponseFile responseFile = wrapper.uploadFile(mpf);
            responseFiles.add(responseFile);
        }
        return responseFiles;
    }

/*    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiOperation(value = "附件删除", notes = "附件删除")
    public void fileDelete(String fileKey) throws Exception {
        logger.info("附件删除入参：fileKey=" + fileKey);
        wrapper.deleteFile(fileKey);
    }*/

    @GetMapping(value = "/download")
    @ApiOperation(value = "附件下载", notes = "附件下载" +
            "-参数放在URL上面，普通的get方法不是restful风格的")
    public void download(HttpServletResponse response, HttpServletRequest request,
                         @RequestParam String fileKey, @RequestParam String fileName) throws Exception {
        logger.info("begin to download..." + fileName);
        OutputStream output = null;
        InputStream inputStream = null;
        try {
            output = response.getOutputStream();
            fileName = StringUtils.isEmpty(fileName) ? DateUtil.dateFormatBrief.get().format(new Date()) : fileName;
            if (request.getHeader("User-Agent").toUpperCase().indexOf("FIREFOX") > 0) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");//firefox浏览器
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }

            response.reset();
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\";");

            wrapper.downloadFile(fileKey,output);

        } catch (Exception e) {
            logger.error("filedownloadError!" + e.getMessage(), e);
            throw new Exception("文件下载异常");
        } finally {
            logger.info("download done");

            if (inputStream != null) {
                inputStream.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    public static void toZip(Path tozip, Collection<Path> files) throws IOException {
        try (FileOutputStream fous = new FileOutputStream(tozip.toFile())) {
            try (ZipOutputStream zipOut = new ZipOutputStream(fous)) {
                files.forEach(file -> {
                    try {
                        zipOut.putNextEntry(new ZipEntry(file.getFileName().toString()));
                        Files.copy(file, zipOut);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }


    @GetMapping(value = "/batchDownload")
    @ApiOperation(value = "附件下载", notes = "附件下载" +
            "\n - 参数放在URL上面，普通的get方法不是restful风格的" +
            "\n - fileKeys 文件key集合 example（/group/temp1;/group/temp2）" +
            "\n - fileName 下载附件的名字，不填的话以当前时间字符命名" +
            "\n - example:http://localhost:8080/file/batchDownload?fileKeys=group1/M00/00/00/wKj7MVlCKkeAInxCAAzodQCbVVc523.jpg%E3%80%81group1/M00/00/00/wKj7MVlCKkeAInxCAAzodQCbVVc523.jpg&&fileName=attach")
    public void batchDownload(HttpServletResponse response, HttpServletRequest request,
                              @RequestParam String fileKeys,
                              @RequestParam(required = false) String fileNames) throws Exception {


        List<String> fileKeyList = Splitter.on(";").splitToList(fileKeys);
        List<String> fileNameList = Lists.newArrayList();
        if (fileNames != null) {
            fileNameList = Splitter.on(";").splitToList(fileNames);
        } else {
            fileNameList = fileKeyList.stream().map(
                    k -> Iterables.getLast(Splitter.on("/").splitToList(k))).collect(Collectors.toList());
        }

        Map<String, String> fileKeyName = Maps.newTreeMap();

        final List<String> finalFileNameList = fileNameList;
        IntStream.range(0, fileKeyList.size()).forEach(index -> {
            if (index < finalFileNameList.size()) {
                fileKeyName.put(fileKeyList.get(index), finalFileNameList.get(index));
            } else {
                fileKeyName.put(fileKeyList.get(index),
                        Iterables.getLast(Splitter.on("//").splitToList(fileKeyList.get(index))));
            }
        });

        logger.info("batchDownload => {}", fileKeyName);
        try {
            Path tempDir = Files.createTempDirectory("fin-web-file");
            List<Path> files = new ArrayList<Path>();
            fileKeyName.entrySet().forEach(item ->
            {
                try {

                    String filePath = wrapper.getFileUrl() + java.net.URLDecoder.decode(item.getKey(), "utf-8");
                    URL url = new URL(filePath);
                    logger.info("begin to download => {}", filePath);
                    //HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    Path tofile = Paths.get(tempDir.toString(), item.getValue());
                    wrapper.downloadFile(item.getKey(),tofile);
                    files.add(tofile);
                    logger.info("done to download => {}", filePath);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            });
            //创建文件输出流

            Path toZip = Files.createTempFile(tempDir, "fin", ".zip");
            logger.info("pack to zip => {}", toZip);
            toZip(toZip, files);
            //像客户端输出
            try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(toZip.toFile()))) {
                response.reset();
                response.setContentType("application/x-msdownload");
                response.addHeader("Content-Disposition", "attachment;fileName=\"" + toZip.getFileName() + FileTypeEnum.RAR.getSuffix() + "\";");
                try (OutputStream output = response.getOutputStream()) {
                    ByteStreams.copy(bin, output);
                }
            }
            try {
                files.forEach((path) -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                    }
                });

                Files.delete(tempDir);
            } catch (Exception e) {
                //skip

            }


        } catch (Throwable e) {
            throw new RuntimeException("文件下载异常", e);
        }
    }
}

