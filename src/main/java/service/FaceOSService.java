package service;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prosecenium.core.ConfigConstant;
import com.prosecenium.core.dllinterface.FaceDetect;
import com.prosecenium.core.dllinterface.FaceVerify;
import com.prosecenium.core.dllinterface.FaceVerify.rr_face_primary_t;
import com.prosecenium.core.dllinterface.FaceVerify.rr_feature_t;
import com.prosecenium.core.util.ConfigUtil;
import com.prosecenium.core.util.ImageConvertUtil;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * 人脸识别(win7)
 * @author taochuang
 * @since 17.12.2
 *
 */
@Service
public class FaceOSService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceOSService.class); 
	/**
	 * 人脸检测句柄
	 */
	public static final Pointer DETECT_HANDLE;
	/**
	 * 人脸对比句柄
	 */
	public static final Pointer VERIFY_HANDLE;
	/**
	 * 人脸信息结构体
	 */
	private static final FaceDetect.rr_face_t P_FACE_ARRAY;
	/**
	 * 人脸信息结构体的地址
	 */
	private static final Pointer POINTER;
	/**
	 * 人脸信息结构体的地址的指针
	 */
	private static final PointerByReference POINTER_BY_REFERENCE;
	
	static{
		if( ConfigUtil.getBoolean(ConfigConstant.INITIALIZE_FACEOS)){
			DETECT_HANDLE =  FaceDetect.faceDetect.rr_fd_create_detector(ConfigUtil.getString(ConfigConstant.MODEL_PATH, ConfigUtil.DEFAULT_MODEL_PATH), FaceDetect.faceDetect.rr_fd_get_version());
			VERIFY_HANDLE =  FaceVerify.faceVerify.rr_fv_create_verifier(ConfigUtil.getString(ConfigConstant.MODEL_PATH, ConfigUtil.DEFAULT_MODEL_PATH), FaceVerify.faceVerify.rr_fv_get_version());
			P_FACE_ARRAY = new FaceDetect.rr_face_t();
			POINTER = P_FACE_ARRAY.getPointer();
			FaceDetect.faceDetect.rr_fd_release_detect_result(POINTER);
			POINTER_BY_REFERENCE = new PointerByReference(POINTER);
		} else {
			DETECT_HANDLE = null;
			VERIFY_HANDLE= null;
			P_FACE_ARRAY= null;
			POINTER= null;
			POINTER_BY_REFERENCE= null;
		}
	}
	
	private FaceOSService() {}
	
	/**
	 * 人脸检测对比
	 * @param pathOne 第一张图片文件的绝对路径
	 * @param pathTwo 第二张图片文件的绝对路径
	 * @return 0.0-100.0 成功，人脸相似度；-1.0 失败，表示有图片未能识别出人像或一次识别出多个人像
	 * <p>注意：建议摄像头截取图片传入pathOne，身份证头像图片传入pathTwo，可提高识别速度</p>
	 * <p>注意：经反复测试，两张不同人脸的图片相似度基本在0.0-0.2之间，两张相同人脸的图片相似度基本在0.6以上，建议以0.5为相似标准</p>
	 */
	public static float comparePicture(String pathOne, String pathTwo){
		String[] theTwoPath = new String[]{pathTwo, pathOne};
		rr_feature_t[] features = (rr_feature_t[])new rr_feature_t().toArray(2);
		byte[] imageData = null;
		BufferedImage bufferedImage = null;
		IntByReference faceCount = new IntByReference();
		float confidence = -1.0f;
		for(int i = 0; i < 2; i++) {
			try {
				bufferedImage = ImageIO.read(new File(theTwoPath[i]));
				imageData = ImageConvertUtil.getMatrixBGR(bufferedImage);
			} catch (Exception e) {
				e.printStackTrace();
				return -2;
			}
			for(int j = 0;j < 11; j++) {
				FaceDetect.faceDetect.rr_fd_detect(DETECT_HANDLE, imageData, FaceDetect.RR_IMAGE_BGR8UC3,
						bufferedImage.getWidth(), bufferedImage.getHeight(), POINTER_BY_REFERENCE, faceCount);
				if(faceCount.getValue() != 1) {
					LOGGER.info("人脸识别失败，第" + i + "张图片识别出的人脸数量为" + faceCount.getValue());
					return -1;
				}
				P_FACE_ARRAY.read();
				System.out.println("人脸信息："+P_FACE_ARRAY);
				if(P_FACE_ARRAY.yaw == 0 && P_FACE_ARRAY.confidence != confidence) {
					confidence = P_FACE_ARRAY.confidence;
					break;
				}
				LOGGER.info("未能读取到内存中人脸数据，请重试");
				if(j==10) {
					System.out.println("jjjjjjjjjjjjjjj");
//					FaceDetect.faceDetect.rr_fd_release_detect_result(POINTER);
					return -4;
				}
			}
			FaceDetect.faceDetect.rr_fd_release_detect_result(POINTER);
			rr_face_primary_t.ByReference primary = new rr_face_primary_t.ByReference();
			primary.landmarks=P_FACE_ARRAY.landmarks;
			primary.rect=P_FACE_ARRAY.rect;
			FaceVerify.faceVerify.rr_fv_extract_feature(VERIFY_HANDLE, imageData, 
					FaceDetect.RR_IMAGE_BGR8UC3, bufferedImage.getWidth(), bufferedImage.getHeight(), primary, features[i]);
		}
		float ret = FaceVerify.faceVerify.rr_fv_compare_features(features[0], features[1]);
		System.out.println("ret:"+ret);
		return ret;
	}

	/**
	 * 断开句柄
	 * <p>建议关闭服务器时调用</p>
	 */
	public static void closeHandle() {
		FaceDetect.faceDetect.rr_fd_destroy_detector(DETECT_HANDLE);
		FaceVerify.faceVerify.rr_fv_destroy_verifier(VERIFY_HANDLE);
	}

}
