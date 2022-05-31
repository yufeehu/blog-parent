package com.hyh.blog.util;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author huyuhui
 * 七牛云上传文件工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuUtils {
    /**
     * 暂时使用测试域名
     */
    public static final String url = "http://rcounza44.hn-bkt.clouddn.com/";
    /**
     * 密钥,生成上传凭证，然后准备上传
     */
    private String accessKey;
    private String secretKey;
    /**
     * 修改上传名称为自己的空间名称
     */
    private String bucket;

    public boolean upload(MultipartFile file, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                log.info("key =====>{}", putRet.key);
                log.info("hash =====>{}", putRet.hash);
                return true;
            } catch (QiniuException ex) {
                Response r = ex.response;
                log.error(r.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
