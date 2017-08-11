package com.kss.manage.database.service.login;


import com.kss.commons.Constants;
import com.kss.manage.application.bean.login.TokenModel;
import com.kss.manage.application.service.login.TokenManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    private RedisTemplate<String, String> redis;

    @Autowired
    public void setRedis(RedisTemplate<String, String> redis) {
        this.redis = redis;
        //泛型设置成Long后必须更改对应的序列化方案
        redis.setKeySerializer(new StringRedisSerializer());
    }

    @Override
    public String createToken(long userId,String ip){

        try{
            //token加密生成（用户id+ip+时间）
            MessageDigest md = MessageDigest.getInstance("SHA");//SHA 或者 MD5
            Base64 base = new Base64();
            String userIdIpTime = String.valueOf(userId)+ip+String.valueOf(new Date());
            String token = base.encodeAsString(md.digest(userIdIpTime.getBytes("utf-8")));

            //存储到redis并设置过期时间
            redis.boundValueOps(token).set(String.valueOf(userId), Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
            return token;
        }catch (NoSuchAlgorithmException e){
            //uuid生成
            String token = UUID.randomUUID().toString().replace("-", "");
            return token;
        }catch (Exception e){
            e.printStackTrace();
            String token = UUID.randomUUID().toString().replace("-", "");
            return token;
        }
    }

    @Override
    public TokenModel checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String userId = redis.boundValueOps(token).get();
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redis.boundValueOps(token).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return new TokenModel(Long.valueOf(userId), token);
    }

    @Override
    public void deleteToken(String token) throws Exception{
        redis.delete(token);
    }

    @Override
    public Long getUserId(String token) throws Exception {
        String userId = redis.boundValueOps(token).get();
        return Long.valueOf(userId);
    }

}
