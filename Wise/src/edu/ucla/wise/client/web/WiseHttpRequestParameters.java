/**
 * Copyright (c) 2014, Regents of the University of California
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.ucla.wise.client.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ucla.wise.admin.AdminUserSession;
import edu.ucla.wise.commons.SanityCheck;
import edu.ucla.wise.persistence.data.Answer;
import edu.ucla.wise.utils.HttpUtils;

/**
 * Use this class to get parameters from HTTP requests.
 */
public class WiseHttpRequestParameters {

	/**
	 * HttpRequest to be wrapped by this method.
	 */
	private final HttpServletRequest request;

	/**
	 * Constructor to create a wrapper around request.
	 * 
	 * @param request
	 */
	public WiseHttpRequestParameters(HttpServletRequest request) {
		this.request = request;
	}

	public String getAlphaNumericParameterValue(String parameter) {
		String value = SanityCheck.onlyAlphaNumeric(this.request.getParameter(parameter));
		return value;
	}

	public String getNonSanitizedStringParameter(String parameter) {
		return this.request.getParameter(parameter);
	}

	public String getEncodedStudySpaceId() {
		return this.getAlphaNumericParameterValue("t");
	}

	public String getPage() {
		return this.getAlphaNumericParameterValue("p");
	}
	
	public String getEncodedMessageId() {
		return this.getAlphaNumericParameterValue("msg");
	}

	public String getPageId() {
		return this.getAlphaNumericParameterValue("page");
	}
	public String getReason() {
		return this.getAlphaNumericParameterValue("reason");
	}
	public String getEncodedSurveyId() {
		return this.getAlphaNumericParameterValue("s");
	}
	
	public String getRepeatTableName(){
		return request.getParameter("repeat_table_name");
	}

	public String getRepeatTableRow(){
		return request.getParameter("repeat_table_row");
	}

	public String getRepeatTableRowName(){
		return request.getParameter("repeat_table_row_name");
	}
	
	public Map<String,Answer> getQuestionAnswersForRepeatSet(){
		HashMap<String, Answer> params = new HashMap<>();
		String name, value;
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            name = (String) e.nextElement();
            value = request.getParameter(name);
            if (!name.contains("repeat_table_name") && !name.contains("repeat_table_row")
                    && !name.contains("repeat_table_row_name")) {

                /*
                 * Parse out the proper name here here split the value into
                 * its constituents
                 */

                String[] typeAndValue = value.split(":::");
                if (typeAndValue.length == 2) {
                    params.put(name, Answer.getAnswer(typeAndValue[1], typeAndValue[0]));
                } else {
                    if (typeAndValue.length == 1) {
                        params.put(name, Answer.getAnswer("", typeAndValue[0]));
                    }

                }
            } else {
                ;// do nothing
            }
        }
        return params;
	}
	
	public AdminUserSession getAdminUserSessionFromHttpSession() {
		return (AdminUserSession) this.getSession(true).getAttribute("ADMIN_USER_SESSION");
	}

	public String getRequestIP(){
		String IP=request.getRemoteAddr();
		if(IP==null){
			IP="";
		}
		return IP;
	}

	public String getUserAgent(){
		String userAgent = request.getHeader("user-agent");
		if(userAgent==null){
			userAgent = "";
		}
		return userAgent;
	}

	public HttpSession getSession(boolean createNew) {
		return this.request.getSession(createNew);
	}
	
	public CharSequence appendEmailUrlParameters(String url){
		String[][] params = {{"msg",getEncodedMessageId()},{"t",getEncodedStudySpaceId()}};
		return HttpUtils.createURL(url,params);
	}
	
	public String getAction(){
		return getAlphaNumericParameterValue("action");
	}
	public String getNextPage(){
	return this.getAlphaNumericParameterValue("nextPage");
	}
	
	public Map<String, Object> getFormParameters(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		String n, v;
		Enumeration e = request.getParameterNames();

		while (e.hasMoreElements()) {
			n = (String) e.nextElement();
			v = request.getParameter(n);
			params.put(n, v);
		}
		return params;
	}
	
	public String getContextPath(){
		return request.getContextPath();
	}
	
	public String getRequestType(){
		return getAlphaNumericParameterValue("request_type");
	}

	public String getTableName(){
		return getAlphaNumericParameterValue("table_name");
	}

	public String getInstanceName(){
		return getAlphaNumericParameterValue("instance_name");
	}

	public String getInviteeId(){
		return getAlphaNumericParameterValue("invitee_id");
	}
}
