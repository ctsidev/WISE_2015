package edu.ucla.wise.client.web;

import com.google.common.base.Strings;

import edu.ucla.wise.commons.SurveyorApplication;
import edu.ucla.wise.commons.WiseConstants;
import edu.ucla.wise.utils.HttpUtils;

public class UrlGenerator {

	public static String getSurveyCookieErrorPage(){
		return "<HTML><HEAD><TITLE>Begin Page</TITLE>"
				+ "<LINK href='"
				+ SurveyorApplication.getInstance().getSharedFileUrl()
				+ "style.css' type=text/css rel=stylesheet>"
				+ "<body><center><table>"
				// + "<body text=#000000 bgColor=#ffffcc><center><table>"
				+ "<tr><td>Error: WISE can't seem to store your identity in the browser. You may have disabled cookies.</td></tr>"
				+ "</table></center></body></html>";
	}

	public static String generateBodyReloadUrl(String newUrl){
		StringBuilder response = new StringBuilder();
		response.append("<html>");
		response.append("<head></head>");
		response.append("<body ONLOAD=\"self.location = '" + newUrl + "';\"></body>");
		response.append("</html>");
		return response.toString();
	}
	
	public static String generateViewPageUrl(String pageId){
		return "view_form?p=" + pageId;
	}
	
	public static String generateViewPageUrl(String pageId, WiseHttpRequestParameters req){
		String msgId = req.getEncodedMessageId();
		String studyId = req.getEncodedStudySpaceId();
		if(!Strings.isNullOrEmpty(msgId)&&!Strings.isNullOrEmpty(studyId)){
			return ""+HttpUtils.createURL("view_form", new String[][]{{"msg",msgId},{"t",studyId},{"p",pageId}});	
		}else
		{
			return generateViewPageUrl(pageId);
		}
	}
	
	public static String getInterruptUrl(){
		return SurveyorApplication.getInstance().getSharedFileUrl() + "interrupt" + WiseConstants.HTML_EXTENSION;
	}
	
	public static String getTimeoutUrl(){
		return SurveyorApplication.getInstance().getSharedFileUrl() + "timeout" + WiseConstants.HTML_EXTENSION;
	}
}
