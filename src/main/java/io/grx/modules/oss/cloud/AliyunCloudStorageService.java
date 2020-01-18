package io.grx.modules.oss.cloud;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import cn.hutool.core.util.ZipUtil;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.common.comm.Protocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import io.grx.common.exception.RRException;

/**
 * 阿里云存储
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-26 16:22
 */
public class AliyunCloudStorageService extends CloudStorageService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private OSSClient client;

    public AliyunCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
        ClientConfiguration conf = new ClientConfiguration();
        if (StringUtils.startsWithIgnoreCase(config.getAliyunDomain(), "https")) {
            conf.setProtocol(Protocol.HTTPS);
        }
        client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret(), conf);
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            String remotePath = config.getAliyunPrefix() + "/" + path;
            client.putObject(config.getAliyunBucketName(), remotePath, inputStream);
            logger.info("Write file to Ali OSS: {}", remotePath);
        } catch (Exception e){
            throw new RRException("上传文件失败，请检查配置信息", e);
        }

        return path;
    }

    @Override
    public String upload(String bucket, InputStream inputStream, String path) {
        try {
            client.putObject(bucket, path, inputStream);
            logger.info("Write file to Ali OSS: {}, bucket: {}", path, bucket);
        } catch (Exception e){
            throw new RRException("上传文件失败，请检查配置信息", e);
        }

        return path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }

    /**
     * 文件下载
     *
     * @param path 文件路径，包含文件名
     * @return
     */
    @Override
    public InputStream get(final String path) {
        try {
            String uploadPath = path;
            if (StringUtils.isNotBlank(config.getAliyunPrefix())) {
                uploadPath = config.getAliyunPrefix() + "/" + path;
            }
            OSSObject ossObject = client.getObject(config.getAliyunBucketName(), uploadPath);
            if (ossObject != null) {
                InputStream inputStream = ossObject.getObjectContent();

                if (StringUtils.endsWithIgnoreCase(path, ".gz")) {
                    inputStream = new ByteArrayInputStream(ZipUtil.unGzip(inputStream));
                }
                return inputStream;
            }
        } catch (OSSException e) {
            logger.info("Error get file: {}, {}", path, e.getMessage());
        }
        return null;
    }

    @Override
    public InputStream get(final String bucket, final String path) {
        try {
            String uploadPath = path;
            OSSObject ossObject = client.getObject(bucket, uploadPath);
            if (ossObject != null) {
                InputStream inputStream = ossObject.getObjectContent();

                if (StringUtils.endsWithIgnoreCase(path, ".gz")) {
                    inputStream = new ByteArrayInputStream(ZipUtil.unGzip(inputStream));
                }
                return inputStream;
            }
        } catch (OSSException e) {
            logger.info("Error get file: {}, {}", path, e.getMessage());
        }
        return null;
    }

    @Override
    public String generatePresignedUrl(final String bucket, final String path, final long expirationInSeconds) {
        URL url = client.generatePresignedUrl(bucket, path, new Date(System.currentTimeMillis()
                + expirationInSeconds * 1000));
        return url.toString();
    }
}
