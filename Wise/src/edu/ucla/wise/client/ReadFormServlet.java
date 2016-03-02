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

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import edu.ucla.wise.client.web.UrlGenerator;
import edu.ucla.wise.client.web.WiseHttpRequestParameters;
import edu.ucla.wise.commons.Interviewer;
import edu.ucla.wise.commons.SurveyorApplication;
import edu.ucla.wise.commons.User;
import edu.ucla.wise.commons.WISEApplication;

/**
 * ReadFormServlet is used to update the results of the user taking the survey
 * and redirecting them to a correct page based on the action.
 * 
 */
@WebServlet("/survey/readform")
public class ReadFormServlet extends AbstractUserSessionServlet {
	private static final Logger LOGGER = Logger.getLogger(ReadFormServlet.class);
	static final long serialVersionUID = 1000;

	/**
	 * Creates a Html page with the input address as the new page.
	 * 
	 * @param newPage
	 *            Url of the new page.
	 * @return String Html of the new page.
	 */
	public String pageReplaceHtml(String newPage) {
		return "<html>" + "<head><script LANGUAGE='javascript'>" + "top.location.replace('" + newPage + "');"
				+ "</script></head>" + "<body></body>" + "</html>";
	}

	/**
	 * Updates the answers of users and redirect the survey to next page
	 * correctly.
	 * 
	 * @param req
	 *            HTTP Request.
	 * @param res
	 *            HTTP Response.
	 * @throws ServletException
	 *             and IOException.
	 */
	@Override
	public String serviceMethod(User user, HttpSession session, WiseHttpRequestParameters requestParams) {
		StringBuilder response = new StringBuilder();
		/*
		 * get all the fields values from the form and save them in the hash
		 * table
		 */

		Map<String, Object> params = requestParams.getFormParameters();
		String action = requestParams.getAction();
		if (Strings.isNullOrEmpty(action)) {
			/* if no action value is specified, fill in default */
			action = "NEXT";
		}

		/* User jumping to page selected from progress bar */
		switch (action.toLowerCase()){
		case "linkpage":
		{/*
		 * the next page will be the page clicked by the user or the
		 * interviewer
		 */
			user.readAndAdvancePage(params, false);
			String linkPageId = requestParams.getNextPage();
			user.setPage(linkPageId);
			response.append(UrlGenerator.generateViewPageUrl(user.getCurrentPage().getId(),requestParams));
		}
		break;
		case "interrupt":
		{
			/* Detect interrupt states; forward to appropos page */
			user.readAndAdvancePage(params, false);
			user.setInterrupt();
			session.invalidate();
			response.append(this.pageReplaceHtml(UrlGenerator.getInterruptUrl()));
		}
		break;
		case "timeout":
		{
			/* if it is an timeout event, then show the timeout info */
			user.readAndAdvancePage(params, false);
			user.setInterrupt();
			session.invalidate();
			response.append(this.pageReplaceHtml(UrlGenerator.getTimeoutUrl()));

		}
		break;
		case "abort":
		{
			/*
			 * if it is an abort event (entire window was closed), then record
			 * event; nothing to show
			 */
			user.readAndAdvancePage(params, false);
			user.setInterrupt();

			/*
			 * should force user object to be dropped & connections to be
			 * cleaned up
			 */
			session.invalidate();
		}
		break;
		default:
		{
			/*
			 * either done or continuing; go ahead and advance page give user
			 * submitted http params to record & process
			 */
			user.readAndAdvancePage(params, true);
		}
		}

		String newPage="";
		if (user.completedSurvey()) {

			/* check if it is an interview process */
			Interviewer inv = (Interviewer) session.getAttribute("INTERVIEWER");
			if (inv != null) {

				/* record interview info in the database */
				inv.setDone();

				/* remove the current user info */
				session.removeAttribute("USER");

				/* redirect to the show overview page */
				newPage = SurveyorApplication.getInstance().getSharedFileUrl() + "interview/Show_Assignment.jsp";
			} else {

				/*
				 * redirect the user to the forwarding URL specified in survey
				 * xml file
				 */
				if ((user.getCurrentSurvey().getForwardUrl() != null)
						&& !user.getCurrentSurvey().getForwardUrl().equalsIgnoreCase("")) {

					// for example:
					// forward_url="http://localhost:8080/ca/servlet/begin?t="
					newPage = user.getCurrentSurvey().getForwardUrl();
					// if the EDU ID (study space ID) is specified in survey
					// xml,
					// then add it to the URL
					if ((user.getCurrentSurvey().getEduModule() != null)
							&& !user.getCurrentSurvey().getEduModule().equalsIgnoreCase("")) {
						// new_page = new_page +
						// "/"+user.getCurrentSurvey().study_space.dir_name+"/servlet/begin?t="

						newPage = newPage + "/" + user.getCurrentSurvey().getStudySpace().dirName + "/survey?t="
								+ WISEApplication.encode(user.getCurrentSurvey().getEduModule()) + "&r="
								+ WISEApplication.encode(user.getId());

					} else {
						/* otherwise the link will be the URL plus the user ID */
						newPage = newPage + "?s=" + WISEApplication.encode(user.getId()) + "&si="
								+ user.getCurrentSurvey().getId() + "&ss="
								+ WISEApplication.encode(user.getCurrentSurvey().getStudySpace().id);
						LOGGER.info(newPage + ReadFormServlet.class.getName());
					}
				} else {

					/* Setting the User state to completed. */
					user.setComplete();

					// -1 is default if no results are going to be reviewed.
					if (user.getCurrentSurvey().getMinCompleters() == -1) {
						newPage = SurveyorApplication.getInstance().getSharedFileUrl() + "thank_you";
					} else {
						/*
						 * go to results review, send the view result email only
						 * once when it reaches the min number of completers
						 */
						int currentNumbCompleters = user.checkCompletionNumber();
						String review = "false";

						if (currentNumbCompleters >= user.getCurrentSurvey().getMinCompleters()) {
							review = "view_results";
						}

						/*
						 * redirect to the thank you html with the review link
						 * for the current user and future completers
						 */
						newPage = SurveyorApplication.getInstance().getSharedFileUrl() + "/thank_you?review=" + review;
					}
				}

			} // end of else (not interview)
			response.append(this.pageReplaceHtml(newPage));
		} else {

			/*
			 * continue to the next page form the link to the next page
			 */
			response.append(UrlGenerator.generateViewPageUrl(user.getCurrentPage().getId(),requestParams));
		}
		return response.toString();
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}