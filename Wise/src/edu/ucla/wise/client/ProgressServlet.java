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
package edu.ucla.wise.client;

import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.ucla.wise.client.web.WiseHttpRequestParameters;
import edu.ucla.wise.commons.Interviewer;
import edu.ucla.wise.commons.User;

/**
 * ProgressServlet is a class used to display the sub menus to the left of the
 * survey to review the completed pages of the survey.
 * 
 */
@WebServlet("/survey/progress")
public class ProgressServlet extends AbstractUserSessionServlet {
	private static final Logger LOGGER = Logger.getLogger(ProgressServlet.class);
	static final long serialVersionUID = 1000;

    @Override
    public String serviceMethod(User user, HttpSession session, WiseHttpRequestParameters requestParams) {
    	StringBuilder response = new StringBuilder();
        Hashtable<String, String> completedPages = user.getCompletedPages();

        /* get the interviewer if it is on the interview status */
        Interviewer inv = (Interviewer) session.getAttribute("INTERVIEWER");

        /* for interviewer, he can always browse any pages */
        if (inv != null) {
            user.getCurrentSurvey().setAllowGoback(true);
        }

        /*
         * check if the allow goback setting is ture, then user could go back to
         * view the pages that he has went through
         */
        if (user.getCurrentSurvey().isAllowGoback()) {
            response.append(user.getCurrentSurvey().printProgress(user.getCurrentPage()));
        } else {

            /*
             * otherwise, print out the page list without linkages to prevent
             * user from going back
             */
            response.append(user.getCurrentSurvey().printProgress(user.getCurrentPage(), completedPages));
        }

       return response.toString();
    }

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}
