package io.grx.pay.yeepay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigestAlgEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.encrypt.DigitalSignatureDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.g3.sdk.yop.utils.InternalConfig;
import com.yeepay.g3.yop.sdk.api.StdApi;
import sun.misc.BASE64Decoder;


import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//import Decoder.BASE64Decoder;

public class YeepayService {

	//yop接口应用URI地址
	public static final String BASE_URL = "baseURL";
	public static final String TRADEORDER_URL = "tradeOrderURI";
	public static final String ORDERQUERY_URL = "orderQueryURI";
	public static final String REFUND_URL = "refundURI";
	public static final String REFUNDQUERY_URL = "refundQueryURI";
	public static final String MULTIORDERQUERY_URL = "multiOrderQueryURI";
	public static final String ORDERCLOSE_URL = "orderCloseURI";
	public static final String DIVIDEORDER_URL="divideOrderURI";
	public static final String DIVIDEORDERQUERY_URL="divideOrderQueryURI";
	public static final String FULLSETTLE_URL="fullSettleURI";
	public static final String CERTORDER_URL = "certOrderURI";
	public static final String CERTORDERQUERY_URL = "certOrderQueryURI";
	public static final String APICASHIER_URI = "APICASHIER";
	public static final String TRADEDAYDOWNLOAD_URI = "tradedaydownloadURI";
	public static final String TRADEMONTHDOWNLOAD_URI = "trademonthdownloadURI";
	public static final String REMITDAYDOWNLOAD_URI = "remitdaydownloadURI";
	
	
	//接口参数
	public static final String[] TRADEORDER = {"parentMerchantNo","merchantNo","orderId","orderAmount","timeoutExpress","requestDate","redirectUrl","notifyUrl","goodsParamExt","paymentParamExt","industryParamExt","memo","riskParamExt","csUrl","fundProcessType","divideDetail","divideNotifyUrl"};
	public static final String[] ORDERQUERY = {"parentMerchantNo","merchantNo","orderId","uniqueOrderNo"};
	public static final String[] REFUND = {"parentMerchantNo","merchantNo","orderId","uniqueOrderNo","refundRequestId","refundAmount","description","memo","notifyUrl","accountDivided"};
	public static final String[] REFUNDQUERY = {"parentMerchantNo","merchantNo","refundRequestId","orderId","uniqueRefundNo"};
	public static final String[] MULTIORDERQUERY = {"status","parentMerchantNo","merchantNo","requestDateBegin","requestDateEnd","pageNo","pageSize"};
	public static final String[] ORDERCLOSE = {"orderId","parentMerchantNo","merchantNo","uniqueOrderNo","description"};
	public static final String[] DIVIDEORDER={"parentMerchantNo","merchantNo","divideRequestId","orderId","uniqueOrderNo","divideDetail","infoParamExt","contractNo"};
	public static final String[] DIVIDEORDERQUERY={"parentMerchantNo","merchantNo","divideRequestId","orderId","uniqueOrderNo"};
	public static final String[] FULLSETTLE={"parentMerchantNo","merchantNo","orderId","uniqueOrderNo"};
	public static final String[] CERTORDER = {"merchantNo","requestNo","bankCardNo","idCardNo","userName","authType","requestTime","remark"};
	public static final String[] CERTORDERORDER = {"merchantNo","requestNo","ybOrderId"};
	
	//支付方式
	public static final String[] CASHIER = {"merchantNo","token","timestamp","directPayType","cardType","userNo","userType","ext"};
	public static final String[] APICASHIER = {"token","payTool","payType","userNo","userType","appId","openId","payEmpowerNo","merchantTerminalId","merchantStoreNo","userIp","version","extParamMap"};

	//获取对账类型
	public static final String TRADEDAYDOWNLOAD = "tradedaydownload";
	public static final String TRADEMONTHDOWNLOAD = "trademonthdownload";
	public static final String REMITDAYDOWNLOAD = "remitdaydownload";
		
	//获取对应的请求地址
	public static String getUrl(String payType){
		return Configuration.getInstance().getValue(payType);
	}
	
	//标准收银台——拼接支付链接
	public static String getUrl(Map<String,String> paramValues) throws UnsupportedEncodingException{
		StringBuffer url = new StringBuffer();
		url.append(getUrl("CASHIER"));
		StringBuilder stringBuilder = new StringBuilder();
		System.out.println(paramValues);
		for (int i = 0; i < CASHIER.length; i++) {
			String name = CASHIER[i];
			String value = paramValues.get(name);
			if(i != 0){
				stringBuilder.append("&");
			}
			stringBuilder.append(name+"=").append(value);
		}
		System.out.println("stringbuilder:"+stringBuilder);
		String sign = getSign(stringBuilder.toString());
		url.append("?sign="+sign+"&"+stringBuilder);
		return url.toString();
	}
	
	//获取父商编
	public static String getParentMerchantNo(){
		return Configuration.getInstance().getValue("parentMerchantNo");
	}
	
	//获取子商编
	public static String getMerchantNo(){
		return Configuration.getInstance().getValue("merchantNo");
	}
	
	//获取父商编私钥的字符串形式
	public static String getPrivateKey() {
		return Configuration.getInstance().getValue("privatekey");		
	}
	
	//获取密钥P12
	public static PrivateKey getSecretKey(){
		
		//return getPrivateKey();
		//以下写法默认读取json文件
//		PrivateKey isvPrivateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
//		return isvPrivateKey;
		
		PrivateKey privateKey = null;
		String priKey =Configuration.getInstance().getValue("privatekey");
		PKCS8EncodedKeySpec priPKCS8;
		try {
			priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(priKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			privateKey = keyf.generatePrivate(priPKCS8);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
		
	}
	
	//获取公钥
	public static PublicKey getPublicKey(){
		PublicKey publicKey = null;
		try {
			String publickey=Configuration.getInstance().getValue("publickey");
			java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
		     new BASE64Decoder().decodeBuffer(publickey));
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(bobPubKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
		   e.printStackTrace();
		} catch (IOException e) {
		   e.printStackTrace();
		}
		return publicKey;
//		PublicKey isvPublicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
//		
//		return isvPublicKey;
//		//return getPubKey();
		
	}
	
	//获取sign
	public static String getSign(String stringBuilder){
		String appKey = "OPR:"+getMerchantNo();
		
		PrivateKey isvPrivateKey = getSecretKey();
		
		DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
		digitalSignatureDTO.setAppKey(appKey);
		digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
		digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
		digitalSignatureDTO.setPlainText(stringBuilder.toString());
		String sign = DigitalEnvelopeUtils.sign0(digitalSignatureDTO,isvPrivateKey);
		return sign;
	}
	
	/**
	 * 请求YOP接口
	 * params 请求参数,parentMerchantNo除外
	 * uri 请求yop的应用URI地址
	 * paramSign 请求参数的验签顺序
	 * @throws IOException 
	 */
	public static Map<String, String> requestYop(Map<String, String> params, String uri, String[] paramSign,String path) throws IOException{
		Map<String, String> result = new HashMap<String, String>();
		String BASE_URL = getUrl("baseURL");
		String parentMerchantNo = YeepayService.getParentMerchantNo();
		String merchantNo = YeepayService.getMerchantNo();
		params.put("parentMerchantNo", parentMerchantNo);
		params.put("merchantNo", merchantNo);
		String privatekey=Configuration.getInstance().getValue("privatekey");
		
		YopRequest request = new YopRequest("OPR:"+merchantNo,privatekey);
		//YopRequest request = new YopRequest("OPR:"+merchantNo,path+"/src/config/yop_sdk_config_default.json",BASE_URL);

		for (int i = 0; i < paramSign.length; i ++) {
			String key = paramSign[i];
			request.addParam(key, params.get(key));
		}
		System.out.println(request.getParams());
		
		YopResponse response = YopRsaClient.post(uri, request);
		
		System.out.println(response.toString());
		if("FAILURE".equals(response.getState())){
			if(response.getError() != null)
			result.put("code",response.getError().getCode());
			result.put("message",response.getError().getMessage());
			return result;
		}
		if (response.getStringResult() != null) {
			result = parseResponse(response.getStringResult());
		}
		
		return result;
	}

	//将获取到的response解密完成的json转换成map
	public static Map<String, String> parseResponse(String response){
		
		Map<String,String> jsonMap  = new HashMap<>();
		jsonMap	= JSON.parseObject(response,
				new TypeReference<TreeMap<String,String>>() {});
		
		return jsonMap;
	}
	
	/**
	 * 支付成功，页面回调验签
	 * @param responseMap
	 * @return
	 */
	public static boolean verifyCallback(Map<String,String> responseMap){
		boolean flag = false;
		String merchantNo = responseMap.get("merchantNo");
		String parentMerchantNo = responseMap.get("parentMerchantNo");
		String orderId = responseMap.get("orderId");
		String signResp = responseMap.get("sign");
	    String s = "merchantNo="+merchantNo+"&parentMerchantNo="+parentMerchantNo+"&orderId="+orderId;
	    System.out.println("s===="+s);
	    String appKey = "OPR:"+getMerchantNo();
		PublicKey isvPublicKey = getPublicKey();
		DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
		digitalSignatureDTO.setAppKey(appKey);
		digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
		digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
		digitalSignatureDTO.setPlainText(s.toString());
		digitalSignatureDTO.setSignature(signResp);
		try {
			DigitalEnvelopeUtils.verify0(digitalSignatureDTO,isvPublicKey);
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}
	
	/**
	 * 异步回调验签
	 * @param response
	 * @return
	 */
	public static Map<String, String> callback(String response){
		DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
		dto.setCipherText(response);
		Map<String,String> jsonMap  = new HashMap<>();
	    try {
	        PrivateKey privateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
	        PublicKey publicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
	        dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
	        System.out.println(dto.getPlainText());
	        jsonMap = parseResponse(dto.getPlainText());
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jsonMap;
	}
	
	/**
	 * 新的下载对账单接口
	 * @param
	 * @return
	 */
	public static String download(Map<String, String> params, String path) {
		InputStream returnStream = null; //从yop返回的请求对账文件的流
		OutputStream outputStream = null; //输出到项目中的流
		String parentMerchantNo=getParentMerchantNo();
		String merchantNo = getMerchantNo();
		String method = params.get("method");
		String date=""; //日期格式  yyyy-mm-dd 或者月的格式 yyyy-mm
		String OPRkey=getPrivateKey();  //父商编私钥

		YopRequest request = new YopRequest("OPR:"+parentMerchantNo,OPRkey);
		
		YopResponse response=null; //获得一个yop response

		//配置参数，按月对账和按日对账后边的参数不同
		request.addParam("parentMerchantNo", parentMerchantNo);
		request.addParam("merchantNo", merchantNo);

		//按照月和日参数的不同向yop发起对账文件流的请求
		//arg0:接口的uri（参见手册）
		//arg1:配置好参数的请求对象
	
		String fileName = ""; 
		String filePath = "";
		try {
			if (method.equals(YeepayService.TRADEDAYDOWNLOAD)) { //交易日对账
				//本日交易次日生成对账文件，请勿用当日做参数，以日作为参数
				date=params.get("dateday");
				request.addParam("dayString",date);
				response=YopRsaClient.get(getUrl(TRADEDAYDOWNLOAD_URI),request);
				fileName = "tradeday-"+date+".csv";
			}else if(method.equals(YeepayService.TRADEMONTHDOWNLOAD)){  //交易月对账
				date=params.get("datemonth");
				request.addParam("monthString",date);
				response=YopRsaClient.get(getUrl(TRADEMONTHDOWNLOAD_URI), request);
				fileName = "trademonth-"+date+".csv";				
			}else { //出款日对账
				date=params.get("dateday");
				request.addParam("dayString",date);
				String dataType=params.get("dataType"); //出款具有不同类型
				request.addParam("dataType", dataType);
				response=YopRsaClient.get(getUrl(REMITDAYDOWNLOAD_URI),request);
				fileName = "remitday-"+dataType+"-"+date+".csv";
			}
			System.out.println(response.toString());
			if(!response.isSuccess()) { //访问失败
				filePath=response.getError().getMessage()+" "+response.getError().getSubMessage();
				return filePath;
			}
			 returnStream= response.getFile();
			 if(returnStream==null) {
				 System.out.println("空的呢！没交易啊!\n");
				 filePath="The status is 'SUCCESS' but it's a pity that the file is empty so cannot be downloaded.";
				 return filePath;
			 }
			filePath	= path + File.separator + fileName;
			System.out.println("filePath====="+filePath);
			File file=new File(filePath);
			if(!file.exists()) {
				file.createNewFile();
			}
			//测试能否输出结果
//			String result = new BufferedReader(new InputStreamReader(returnStream)).lines().collect(Collectors.joining(System.lineSeparator()));
//			System.out.println(result);
			
			outputStream = new FileOutputStream(file);
			byte[] bs = new byte[1024];
			int readNum;
			while ((readNum = returnStream.read(bs)) != -1) {
				outputStream.write(bs, 0, readNum);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		try {
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	public static Map<String, String> requestYOP(Map<String, String> params, String uri, String[] paramSign) throws IOException{
		Map<String, String> result = new HashMap<String, String>();
		String BASE_URL = getUrl("baseURL");
		String parentMerchantNo = YeepayService.getParentMerchantNo();
		String merchantNo = YeepayService.getMerchantNo();
		params.put("parentMerchantNo", parentMerchantNo);
		params.put("merchantNo", merchantNo);
		//第三种方式，传appkey和privatekey
		String privatekey=Configuration.getInstance().getValue("privatekey");
		YopRequest request = new YopRequest("OPR:"+merchantNo, privatekey);

		 //第一种方式，不传参数
		//YopRequest request  =  new YopRequest();
		/**
		 * 第二种方式：只传appkey
		 */		
//		YopRequest request  =  new YopRequest("OPR:"+merchantNo);
				
		for (int i = 0; i < paramSign.length; i ++) {
			String key = paramSign[i];
			request.addParam(key, params.get(key));
		}

		
		YopResponse response = YopRsaClient.post(uri, request);
		
		System.out.println(response.toString());
		if("FAILURE".equals(response.getState())){
			if(response.getError() != null)
			result.put("code",response.getError().getCode());
			result.put("message",response.getError().getMessage());
			return result;
		}
		if (response.getStringResult() != null) {
			result = parseResponse(response.getStringResult());
		}
		
		return result;
	}

	
	//----------------------------------------------------------------------------------------------------	
	/*以下均为不用的接口*/
	//----------------------------------------------------------------------------------------------------
	
	/**
	 * 下载对账单
	 * @param method
	 * @param parameters
	 * @return
	 */
	public static String yosFile(Map<String, String> params, String path) {
		StdApi apidApi = new StdApi();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String merchantNo = getMerchantNo();
		String method = params.get("method");
		String dateday = params.get("dateday");
		String datemonth=params.get("datemonth");
		String dataType = params.get("dataType");
		
		String fileName = ""; 
		String filePath = "";
		try {
			if (method.equals(YeepayService.TRADEDAYDOWNLOAD)) {
				System.out.println("1");
				inputStream = apidApi.tradeDayBillDownload(merchantNo, dateday);
				fileName = "tradeday-"+dateday+".csv";
				
			}else if(method.equals(YeepayService.TRADEMONTHDOWNLOAD)){
				System.out.println("2");
				inputStream = apidApi.tradeMonthBillDownload(merchantNo, datemonth);
				fileName = "trademonth-"+datemonth+".csv";
				
			}else if(method.equals(YeepayService.REMITDAYDOWNLOAD)){
				System.out.println("2");
				inputStream = apidApi.remitDayBillDownload(merchantNo, dateday, dataType);
				fileName = "remitday-"+dataType+"-"+dateday+".csv";
				
			}
			filePath	= path + File.separator + fileName;
			System.out.println("filePath====="+filePath);
			outputStream = new FileOutputStream(new File(filePath));
			byte[] bs = new byte[1024];
			int readNum;
			while ((readNum = inputStream.read(bs)) != -1) {
				outputStream.write(bs, 0, readNum);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
	
}
