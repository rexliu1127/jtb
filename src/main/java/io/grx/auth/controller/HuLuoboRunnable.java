package io.grx.auth.controller;


import io.grx.auth.service.YiXiangService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 新建一个线程，调用灯塔-胡萝卜接口获取借贷报告
 */
public class HuLuoboRunnable implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private  Long userID;
    private String merchantNo;
    private  String name;
    private  String id_no;
    private String  mobile;

    @Autowired
    private YiXiangService yiXiangService;

    public HuLuoboRunnable(YiXiangService yiXiangService,Long userID,String merchantNo,String name,String id_no,String mobile)
    {
        this.userID = userID;
        this.merchantNo = merchantNo;
        this.name = name;
        this.id_no = id_no;
        this.mobile = mobile;
        this.yiXiangService = yiXiangService;

    }
    @Override
    public void run() {
        try
        {
            logger.info("HuLuoboRunnable is run ,nam:{},id_no:{},mobile:{}",name,id_no,mobile);
            //调用灯塔数据接口
            yiXiangService.saveYxReportByHuoluobo(userID,merchantNo,name,id_no,mobile);


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
