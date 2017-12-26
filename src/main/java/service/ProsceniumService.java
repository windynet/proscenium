package service;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.prosecenium.core.ConfigConstant;
import com.prosecenium.core.service.F1Service;
import com.prosecenium.core.service.TemicService;
import com.prosecenium.core.util.ConfigUtil;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ProsceniumService {
	
	//图片流保存至本地
	public boolean convertPicture(String base64Pictrue, HttpServletRequest request) {
		try{                 
            String data = "";            
            if(base64Pictrue == null || "".equals(base64Pictrue)){  
                throw new Exception("上传失败，上传图片数据为空");  
            }else{  
                String [] d = base64Pictrue.split("base64,");  
                if(d != null && d.length == 2){  
                    data = d[1];  
                }else{  
                    throw new Exception("上传失败，数据不合法");  
                }  
            }               
            String pictrueFileName = ConfigUtil.getString(ConfigConstant.SCREENSHOT_PATH, ConfigUtil.DEFAULT_SCREENSHOT_PATH);                  
            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包  
            byte[] bs = Base64Utils.decodeFromString(data);  
            try{  
                //使用apache提供的工具类操作流  
                FileUtils.writeByteArrayToFile(new File(pictrueFileName), bs);    
            }catch(Exception ee){  
                throw new Exception("上传失败，写入文件失败，"+ee.getMessage());
            }
            Thumbnails.of(pictrueFileName) 
			.scale(1f) 
			.outputQuality(0.5f) 
			.toFile(pictrueFileName);
            return true;                
        }catch (Exception e) {
        	e.printStackTrace();
            return false;  
        }          
	}
	
	//回收卡并清除卡中数据
	public boolean returnCard(int timeOut) {
		if(timeOut < 1) return false;
		F1Service.setEntryMode(50);
		int count = timeOut << 2;
		while(true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//提前0.5秒禁止进卡
			if(count == 2) F1Service.setEntryMode(48);
			if(F1Service.readyToReading() || --count < 0) break;
		}
		F1Service.setEntryMode(48);
		//防止第一次写卡失误
		if(!TemicService.writeData("00000000".getBytes(), "00000000".getBytes(), 0, 0, 0, true).equals("正确")) {
			 if(!TemicService.writeData("00000000".getBytes(), "00000000".getBytes(), 0, 0, 0, true).equals("正确")) {
				 if(F1Service.readyToReading()) F1Service.sendToPort();
				 return false;
			 }
		}
		F1Service.capture();
		return true;
	}
}
