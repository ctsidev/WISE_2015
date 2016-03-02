package edu.ucla.wise.client.web;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import edu.ucla.wise.commons.StudySpace;
import edu.ucla.wise.commons.StudySpaceMap;
import edu.ucla.wise.commons.User;
import edu.ucla.wise.commons.WISEApplication;

public class WebUserUtils {

	static final Logger LOGGER = Logger.getLogger(WebUserUtils.class);
	
	public static User getUserFromUrlParams(WiseHttpRequestParameters parameters){
		User user = null;
		String spaceIdEncode = parameters.getEncodedStudySpaceId();
		String msgId = parameters.getEncodedMessageId();
		LOGGER.debug("t='"+spaceIdEncode+"',msg='"+msgId+"'");
		if(!Strings.isNullOrEmpty(spaceIdEncode)&&!Strings.isNullOrEmpty(msgId))
		{
			/* decode study space ID */
			String spaceId = WISEApplication.decode(spaceIdEncode);

			StudySpace studySpace = StudySpaceMap.getInstance().get(spaceId);
			if(studySpace!=null){
				user=studySpace.getUser(msgId);	
			}	
		}
		return user;
	}
	
}
