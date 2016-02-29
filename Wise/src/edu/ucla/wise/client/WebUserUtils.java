package edu.ucla.wise.client;

import com.google.common.base.Strings;

import edu.ucla.wise.client.web.WiseHttpRequestParameters;
import edu.ucla.wise.commons.StudySpace;
import edu.ucla.wise.commons.StudySpaceMap;
import edu.ucla.wise.commons.User;
import edu.ucla.wise.commons.WISEApplication;

public class WebUserUtils {

	public static User getUserFromUrlParams(WiseHttpRequestParameters parameters){
		User user = null;
		String spaceIdEncode = parameters.getEncodedStudySpaceId();
		String msgId = parameters.getEncodedMessageId();
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
