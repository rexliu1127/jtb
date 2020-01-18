package io.grx.common.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.grx.common.utils.DateUtils;
import io.grx.common.utils.R;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import io.grx.wx.utils.WechatUtils;
import sun.misc.BASE64Decoder;


@Controller
public class FileUploadController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${upload.path}")
    private String fileDirectory;

    @Autowired
    private WechatUtils wechatUtils;

    private CloudStorageService cloudStorageService;

    private static final long MIN_FILE_SIZE = 10 * 1000; // 最小图片要20K以上

    @ResponseBody
    @RequestMapping(value = "/upload")
    public R uploadImages(HttpServletRequest request, MultipartHttpServletRequest multipartRequest) throws Exception {
        // 获得文件
        MultipartFile file = multipartRequest.getFile("fileVal");
        // 获得文件名
        String filename = StringUtils.replace(file.getOriginalFilename(), " ", "_");
        // 文件路径
        String path = DateUtils.formateDate(new Date(), "yyyyMMdd");

        // 取得后缀
        String namePart = StringUtils.substringBefore(filename, ".");
        namePart = namePart.replaceAll("[^a-zA-Z0-9]", "");
        String extPart = StringUtils.substringAfter(filename, ".");
        String name = namePart + "_" + System.currentTimeMillis() + "." + StringUtils.defaultIfBlank(extPart, "jpg");
        String imgUrl = path + "/" + name;

        getCloudStorageService().upload(file.getInputStream(), "upload/" + imgUrl);

        return R.ok().put("path", imgUrl);
    }

    @RequestMapping(value = "/userimg/**")
    public void loadUserImage(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");

        String imagePath = StringUtils.substringAfter(request.getRequestURL().toString(), "/userimg/");
        InputStream is = null;
        try {
            is = getCloudStorageService().get("upload/" + imagePath);

            if (is == null) {
                is = new FileInputStream(fileDirectory + "/" + imagePath);
            }

            IOUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            logger.error("Failed to load image by path: " + imagePath, e);
        }
    }

    @RequestMapping(value = "/uploadBase64", method = RequestMethod.POST)
    @ResponseBody
    public R uploadBase64(@RequestParam String file, @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "fileName", required = false) String aliasName,
                          @RequestParam(value = "genUrl", required = false, defaultValue = "true") boolean genUrl) throws Exception {
//        Enumeration<String> en = request.getParameterNames();
//        while (en.hasMoreElements()) {
//            String key = en.nextElement();
//            logger.info("name={}, value={}", key, request.getParameter(key));
//        }
        FileOutputStream fos = null;
        try {
            String destFileName = StringUtils.replace(StringUtils.defaultIfBlank(name, aliasName), " ", "_");

            String imgUrl = destFileName;

            if (genUrl) {
                // 文件路径
                String path = DateUtils.formateDate(new Date(), "yyyyMMdd");

                // 取得后缀
                String namePart = StringUtils.substringBefore(destFileName, ".");
                namePart = namePart.replaceAll("[^a-zA-Z0-9]", "");
                String extPart = StringUtils.substringAfter(destFileName, ".");
                String fileName = namePart + "_" + System.currentTimeMillis() + "." + StringUtils.defaultIfBlank(extPart,
                        "jpg");

                imgUrl = path + "/" + fileName;
            }

            // 写入文件
            String fileContent = file;
            if (StringUtils.indexOf(fileContent, "base64,") > 0) {
                fileContent = StringUtils.substringAfter(fileContent, "base64,");
            }

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] files = decoder.decodeBuffer(fileContent);
            if (files.length < MIN_FILE_SIZE) {
                return R.error("图片过小, 请上传原图");
            }

            getCloudStorageService().upload(files, "upload/" + imgUrl);


            return R.ok().put("path", imgUrl);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }


    @RequestMapping(value = "/uploadWxImage", method = RequestMethod.POST)
    @ResponseBody
    public R uploadWxImage(@RequestParam String serverId, String name) throws Exception {
        InputStream is = null;
        try {
            String destFileName = StringUtils.replace(name, " ", "_");

            // 文件路径
            String path = DateUtils.formateDate(new Date(), "yyyyMMdd");
            // 取得后缀
            String namePart = StringUtils.substringBefore(destFileName, ".");
            namePart = namePart.replaceAll("[^a-zA-Z0-9]", "");
            String extPart = StringUtils.substringAfter(destFileName, ".");
            String fileName = namePart + "_" + System.currentTimeMillis() + "." + StringUtils.defaultIfBlank(extPart,
                    "jpg");
            String imgUrl = path + "/" + fileName;
//
//            if (dstFile.length() < MIN_FILE_SIZE) {
//                dstFile.delete();
//                return R.error("图片过小, 请上传原图");
//            }
            is = wechatUtils.getFileInputStream(serverId);
            getCloudStorageService().upload(is, "upload/" + imgUrl);

            return R.ok().put("path", imgUrl);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private CloudStorageService getCloudStorageService() {
        if (cloudStorageService == null) {
            synchronized (this) {
                if (cloudStorageService == null) {
                    cloudStorageService = OSSFactory.build();
                }
            }
        }
        return cloudStorageService;
    }

}
