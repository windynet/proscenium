package util;

import com.prosecenium.core.ConfigConstant;
import com.prosecenium.core.dllinterface.FaceDetect;
import com.prosecenium.core.dllinterface.FaceVerify;
import com.prosecenium.core.pojo.IDCard;
import service.FaceOSService;
import com.prosecenium.core.service.IDCardService;
import com.prosecenium.core.service.WebcamService;
import com.prosecenium.core.util.ConfigUtil;

public class Test {
	public static void main(String[] args) {
		int i = 0;
		while(i < 10) {
			i++;
			IDCard idCard = IDCardService.readIDCard(5);
			System.out.println(idCard);
			WebcamService.getPicture(false);
			String pathone = "D:/idcard.bmp";
			String path2 = "D:/screenshot.jpg";
			FaceOSService.comparePicture(path2, pathone);
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
