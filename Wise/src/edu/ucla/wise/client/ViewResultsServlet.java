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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.ucla.wise.commons.Page;
import edu.ucla.wise.commons.Survey;
import edu.ucla.wise.commons.SurveyorApplication;
import edu.ucla.wise.commons.User;
import edu.ucla.wise.commons.WiseConstants;

/**
 * ViewResultsServlet class is used to view the survey results (with the summary
 * of data) by page (viewed by user through the Surveyor Application). URL:
 * /survey/view_results.
 * 
 * @author Douglas Bell
 * @version 1.0
 */

@WebServlet("/survey/view_results")
public class ViewResultsServlet extends AbstractUserSessionServlet {
	static final long serialVersionUID = 1000;
	public static final Logger LOGGER = Logger.getLogger(AbstractUserSessionServlet.class);

	/**
	 * Displays the results of the current page, if page id is not found first
	 * page is set as current page and the results are displayed.
	 * 
	 * 
	 * @param req
	 *            HTTP Request.
	 * @param res
	 *            HTTP Response.
	 * @throws ServletException
	 *             and IOException.
	 */

	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serviceMethod(User user, HttpSession session, Map<String, String[]> requestParams) {


		/* get the current page info */
		String pageId = requestParams.get("page")[0];
		Survey currentSurvey = user.getCurrentSurvey();
		/* if no page info, set the 1st page as the current page */
		if ((pageId == null) || pageId.equalsIgnoreCase("")) {
			pageId = currentSurvey.getPages()[0].getId();
		}

		/* get the page obj */
		Page p = currentSurvey.getPage(pageId);

		// view results of all invitees
		// String whereStr = "";
		// put the whereclause in session
		// session.removeAttribute("WHERECLAUSE");
		// session.setAttribute("WHERECLAUSE", whereStr);
		// display the results
		// out.println(p.render_results(theUser, whereStr));
		return p.toString();
	}
}
