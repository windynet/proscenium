package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.FaceOSService;
import service.ProsceniumService;

import com.prosecenium.core.ConfigConstant;
import com.prosecenium.core.pojo.IDCard;
import com.prosecenium.core.service.IDCardService;
import com.prosecenium.core.service.WebcamService;
import com.prosecenium.core.util.ConfigUtil;

@Controller
@RequestMapping("/proscenium")
public class ProsceniumController {
	
	@Autowired
	ProsceniumService prosceniumService;
	
	@Autowired
	FaceOSService faceOSService;

	//读取身份证信息
	@RequestMapping(value = "/readIDCard", method = RequestMethod.GET, produces = { "application/json;charset=utf-8" })
	@ResponseBody  
	public IDCard readIDCard(@RequestParam Integer timeOut) {
		System.out.println("start");
		IDCard idCard = IDCardService.readIDCard(timeOut);
		System.out.println("end");
		return idCard == null ? null : idCard;
	}
	
//	//人脸识别
//	@RequestMapping(value = "/faceScan", method = RequestMethod.POST)
//	@ResponseBody  
//	public Boolean faceScan(@RequestParam String base64Pictrue, HttpServletRequest request) {
//		if(!prosceniumService.convertPicture(base64Pictrue, request)) return false;
//		WebcamService.getPicture(false);
//		float point = FaceOSService.comparePicture(ConfigUtil.getString(ConfigConstant.SCREENSHOT_PATH, ConfigUtil.DEFAULT_SCREENSHOT_PATH),
//				ConfigUtil.getString(ConfigConstant.IDCARD_PATH, ConfigUtil.DEFAULT_IDCARD_PATH));
//		return point > 0.5f ? true : false;
//		return prosceniumService.convertPicture(base64Pictrue, request);
//		return true;
//	}
	
	//人脸识别
	@RequestMapping(value = "/faceScan1", method = RequestMethod.GET)
	@ResponseBody  
	public Boolean faceScan1(@RequestParam String base64Pictrue, HttpServletRequest request) {
//		if(!prosceniumService.convertPicture(base64Pictrue, request)) return false;
		WebcamService.getPicture(false);
		float point = FaceOSService.comparePicture(ConfigUtil.getString(ConfigConstant.SCREENSHOT_PATH, ConfigUtil.DEFAULT_SCREENSHOT_PATH),
				ConfigUtil.getString(ConfigConstant.IDCARD_PATH, ConfigUtil.DEFAULT_IDCARD_PATH));
		return point > 0.5f ? true : false;
	}
	
	//写房卡并送出
	@RequestMapping(value = "/sendCard", method = RequestMethod.GET)
	@ResponseBody  
	public String sendCard() {
		//TODO
		return "index";
	}
	
	//退房回收卡
	@RequestMapping(value = "/returnCard", method = RequestMethod.GET)
	@ResponseBody  
	public Boolean getCard(@RequestParam Integer timeOut) {
		return prosceniumService.returnCard(timeOut);
	}
	
	@RequestMapping(value = "/indexme", method = RequestMethod.GET)
	public String indexme() {
		WebcamService.getPicture(true);
		return "index";
	}
	
}