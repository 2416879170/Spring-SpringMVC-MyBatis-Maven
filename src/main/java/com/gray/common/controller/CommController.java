package com.gray.common.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.gray.util.FileOperate;

@Controller
@RequestMapping("/common")
public class CommController {
	private final Logger logger = LoggerFactory.getLogger(CommController.class);
	/**
	 * �ϴ�ͼƬ��������
	 * @param filenameId
	 * @param mFile
	 * @return
	 */
	@RequestMapping("/uploadTempImg.do")
	@ResponseBody
	public String uploadTempImg(String filenameId, int width, int height, @RequestParam("fileUpload") CommonsMultipartFile mFile) {
		String msgParamError = "�ϴ���Ƭʧ�ܣ���ˢ�º����ԣ�";
		String msgPhotoMaxSize = "�ϴ���Ƭ���ܳ���2M��";
		String msgFail = "�ϴ���Ƭʧ�ܣ�";

		// �쳣������������
		if (StringUtils.isEmpty(filenameId) || mFile.isEmpty()) {
			return msgParamError;
		}
		
		// ��ʼ��ͼƬ��ʾ��Ϣ
		if (mFile != null) {
			long fileSize = mFile.getFileItem().getSize();
			if (fileSize > 1024*1024*2) {
				return msgPhotoMaxSize;
			}
		}

		// ͼƬ�ϴ���tempĿ¼
	    try {
			String urlPath = FileOperate.compressPic(mFile, width, height, filenameId);
			return urlPath;
		} catch (Exception e) {
			logger.error(msgFail, e);
			return msgFail;
		}
	}
}
