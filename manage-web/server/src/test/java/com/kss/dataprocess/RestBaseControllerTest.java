package com.kss.dataprocess;

import com.alibaba.dubbo.common.io.Bytes;
import com.beust.jcommander.internal.Lists;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.kss.commons.RequestPage;
import com.kss.commons.ResponsePage;
import com.kss.dataprocess.application.bean.file.response.ResponseFile;
import com.kss.dataprocess.application.bean.login.RequestLoginDto;
import com.kss.dataprocess.application.bean.login.TokenResponse;
import com.kss.dataprocess.application.bean.user.request.RequestUserAddDto;
import com.kss.dataprocess.application.bean.user.response.ResponseUserDto;
import com.kss.dataprocess.web.RestTempBean;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by caijiacheng on 16/06/2017.
 */

public class RestBaseControllerTest {

    //简单测试所有的Base的服务的Rest接口,只测试主要流程. 忽略分支.

    public static Logger LOG = LoggerFactory.getLogger(RestBaseControllerTest.class);

    static String AUTH_TOKEN;

    public static String PREFIX = "http://localhost:8080";

    public static Random RANDOM = new Random(System.currentTimeMillis());

    public static RequestPage ALLDATA = new RequestPage(1, 999);

    public static RestTempBean BEANS = new RestTempBean();

    static Integer RANDOM_INT() {
        return 1000 + RANDOM.nextInt(9999);
    }

    static public class AuthInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(
                HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            HttpHeaders headers = request.getHeaders();
            if (!Strings.isNullOrEmpty(AUTH_TOKEN)) {
                LOG.info("set Authorization => ", AUTH_TOKEN);
                headers.add("Authorization", AUTH_TOKEN);
            }

            return execution.execute(request, body);
        }
    }

    public static RestTemplate getTestRestTemplate() {
        RestTemplate testRestTemplate = new RestTemplate();
        testRestTemplate.setInterceptors(Collections.singletonList(new AuthInterceptor()));
        //testRestTemplate.setMessageConverters(ImmutableList.of(new FastJsonHttpMessageConverter4()));
        testRestTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                try {
                    super.handleError(response);
                } catch (RestClientResponseException exception) {
                    LOG.error("get exception: {}", new String(exception.getResponseBodyAsByteArray(), "UTF-8"));
                    throw exception;
                }
            }
        });

        return testRestTemplate;
    }

    @DataProvider(name = "userData")
    public Object[][] userData() {
        List<RequestUserAddDto> users = Lists.newArrayList();
        users.add(new RequestUserAddDto("code" + RANDOM_INT(), "Aa123456", "Aa123456", "name", "134567891", "工长"));
        users.add(new RequestUserAddDto("code" + RANDOM_INT(), "Aa123456", "Aa123456", "name", "134567891", "工长"));
        Object[][] userdatas = new Object[users.size()][];
        IntStream.range(0, users.size()).forEach(item -> userdatas[item] = new Object[]{users.get(item)});
        return userdatas;
    }

    @BeforeTest(enabled = true)
    static public void login() {
        RequestLoginDto requestLoginDto = new RequestLoginDto();
        requestLoginDto.setLoginId("admin");
        requestLoginDto.setPassword("Aa123456");
        ResponseEntity<TokenResponse> responseEntity =
                getTestRestTemplate().postForEntity(PREFIX + "/login/login", requestLoginDto, TokenResponse.class);
        Assertions.assertThat(responseEntity.getStatusCode().equals(HttpStatus.OK));
        Assertions.assertThat(responseEntity.getBody()).hasFieldOrProperty("token");
        AUTH_TOKEN = responseEntity.getBody().getToken();
    }

    @Test(dataProvider = "userData", groups = {"prepare.data"})
    public void prepareUsers(RequestUserAddDto userinfo) {
        LOG.info("test1!");
        ResponseEntity<ResponseUserDto> responseEntity =
                getTestRestTemplate().postForEntity(PREFIX + "/api/base/user", userinfo, ResponseUserDto.class);
        Assertions.assertThat(responseEntity.getStatusCode().equals(HttpStatus.OK));
        BEANS.getUsers().put(responseEntity.getBody().getId(), responseEntity.getBody());
    }

    @Test(dependsOnMethods = {"prepareUsers"})
    public void checkUsers() {
        ParameterizedTypeReference<ResponsePage<ResponseUserDto>> ref =
                new ParameterizedTypeReference<ResponsePage<ResponseUserDto>>() {
                };
        ResponseEntity<ResponsePage<ResponseUserDto>> responseEntity =
                getTestRestTemplate().exchange(PREFIX + "/api/base/user/list",
                        HttpMethod.POST,
                        new HttpEntity<>(ALLDATA),
                        ref);
        Assertions.assertThat(responseEntity.getStatusCode().equals(HttpStatus.OK));

        Assertions.assertThat(responseEntity.getBody().getData().size()).isEqualTo(
                responseEntity.getBody().getTotal());
//        LOG.info("responseEntiy=> {}", responseEntity.getBody());
        BEANS.getUsers().values().forEach(user -> Assertions.assertThat(
                Iterables.any(responseEntity.getBody().getData(),
                responseUserDto -> responseUserDto.getId().equals(user.getId()))));

        //TODO: testcase Get By ID
        BEANS.getUsers().entrySet().parallelStream().forEach(item -> {
            ResponseEntity<ResponseUserDto> entity =
                    getTestRestTemplate().getForEntity(PREFIX + "/api/base/user/" + item.getKey(),
                            ResponseUserDto.class);
            Assertions.assertThat(entity.getStatusCode().equals(HttpStatus.OK));
            Assertions.assertThat(item.getValue()).isEqualTo(entity.getBody());
        });
        //TODO: delete By ID

    }

    @DataProvider(name = "uploadDatas")
    public Object[][] uploadDatas() throws IOException {
        List<String> res = ImmutableList.of(/*"中文.jpg",*/
                "084008tpaeqpeh0z5tkzts.jpg",
                "BingWallpaper-2016-12-09.jpg");
        LOG.info("all tmpe files => {}", res);

        Path tmpdir = Files.createTempDirectory("upload");
        List<String> files = Lists.newArrayList();
        for (String name : res) {
            try (InputStream ins = ClassLoader.getSystemResourceAsStream(name)){
                Path tmp_file = Paths.get(tmpdir.toString(), name);
                Files.copy(ins, tmp_file);
                files.add(tmp_file.toString());
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error("", e);
            }
        }
        LOG.info("all tmpe files => {}", files);

        Object[][] filesdata = new Object[files.size()][];
        IntStream.range(0, files.size()).forEach(item -> filesdata[item] = new Object[]{files.get(item)});
        return filesdata;
    }

    @Test(dataProvider = "uploadDatas",  groups = {"prepare.data"})
    public void testUpload(String file) {

        LOG.info("filename => {}", file);
        ParameterizedTypeReference<List<ResponseFile>> ref =
                new ParameterizedTypeReference<List<ResponseFile>>() {
                };
        Resource resource = new FileSystemResource(file);
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
                parts, headers);
        parts.add("file", resource);
        ResponseEntity<List<ResponseFile>> responseEntity = getTestRestTemplate().exchange(PREFIX + "/file/upload", HttpMethod.POST,
                requestEntity,
                ref);
        Assertions.assertThat(responseEntity.getStatusCode().equals(HttpStatus.OK));

        BEANS.getUploadfiles().put(file, responseEntity.getBody().get(0));
        LOG.info("upload beans => {}", responseEntity.getBody().get(0));
    }


    @Test(dependsOnMethods = {"testUpload"})
    public void checkDownload() throws IOException {
        Path tmpdir = Files.createTempDirectory("download");

        //FIXME: 下载的速度有些慢?
        BEANS.getUploadfiles().entrySet().forEach(item -> {
            Path tofile = Paths.get(tmpdir.toString(), item.getValue().getFileName());
            RequestCallback requestCallback = request -> request.getHeaders()
                    .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            ResponseExtractor<Void> responseExtractor = response -> {
                Files.copy(response.getBody(), tofile);
                return null;
            };
            String url = String.format("%s/file/download?fileKey=%s&fileName=%s", PREFIX,
                    item.getValue().getFileKey(), item.getValue().getFileName() );
            LOG.info("begin to download => {}", item.getValue().toString());

            getTestRestTemplate().execute(url, HttpMethod.GET, requestCallback, responseExtractor);
            try {
//                LOG.info("filename => {}", item.getValue().toString());
                LOG.info("Assert : {} === {}", tofile.toString(), item.getKey());
                Assertions.assertThat(Bytes.getMD5(tofile.toFile())).isEqualTo(Bytes.getMD5(new File(item.getKey())));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        //FIXME: add batchDownload and check

    }

    //下载的速度会有问题
    @Test(dependsOnMethods = {"testUpload"})
    public void checkBatchDownload() throws IOException {
        Path tmpdir = Files.createTempDirectory("batchdownload");
        Path tofile = Paths.get(tmpdir.toString(), "tmp.zip");
        RequestCallback requestCallback = request -> request.getHeaders()
                .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
        ResponseExtractor<Void> responseExtractor = response -> {
            Files.copy(response.getBody(), tofile);
            return null;
        };

        String fileKey = Joiner.on(";").join(
                BEANS.getUploadfiles().values().stream().map(
                        item -> item.getFileKey()).collect(Collectors.toList()));
        String fileName = Joiner.on(";").join(
                BEANS.getUploadfiles().values().stream().map(
                        item -> item.getFileName()).collect(Collectors.toList()));

        String url = String.format("%s/file/batchDownload?fileKeys=%s&fileNames=%s", PREFIX,
                fileKey, fileName);
        getTestRestTemplate().execute(url, HttpMethod.GET, requestCallback, responseExtractor);

    }



    @Test(dependsOnGroups = {"prepare.data"})
    public void test() {
        LOG.info("test2!");
    }

    @AfterTest
    static public void logout() {
        LOG.info("logout");

        //TODO: remove all base data

        BEANS = new RestTempBean();
    }


}
