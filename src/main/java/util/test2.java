package util;

import java.io.IOException;

import com.prosecenium.core.ConfigConstant;
import com.prosecenium.core.dllinterface.FaceDetect;
import com.prosecenium.core.dllinterface.FaceVerify;
import com.prosecenium.core.dllinterface.FaceVerify.rr_feature_t;
import com.prosecenium.core.util.ConfigUtil;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import net.coobird.thumbnailator.Thumbnails;
import service.FaceOSService;

public class test2 {
	public static void main(String[] args) {
		try {
//			Thumbnails.of("D:/configuration/apache-tomcat-8_0_48/webapps/proscenium/resources/screenshot.png") 
//			.scale(1f) 
//			.outputQuality(0.5f) 
//			.toFile("D:/configuration/apache-tomcat-8_0_48/webapps/proscenium/resources/screenshot1.png");
			Thumbnails.of("D:/WorkSpace/proscenium/WebRoot/resources/screenshot.jpg") 
			.scale(1f) 
			.outputQuality(0.5f) 
			.toFile("D:/WorkSpace/proscenium/WebRoot/resources/screenshot.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
