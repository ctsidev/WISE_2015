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
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import edu.ucla.wise.client.web.WiseHttpRequestParameters;
import edu.ucla.wise.commons.User;
import edu.ucla.wise.commons.databank.UserDBConnection;
import edu.ucla.wise.persistence.data.Answer;

/**
 * RepeatingSetReadInputServlet will handle saving survey page values sent
 * through AJAX calls currently implemented only for the repeating item set.
 * 
 */
@WebServlet("/survey/repeating_set_read_input")
public class RepeatingSetReadInputServlet extends AbstractUserSessionServlet {
	static final long serialVersionUID = 1000;
	private static Logger LOGGER = Logger.getLogger(RepeatingSetReadInputServlet.class);

	/**
	 * saves the data into repeating item set tables.
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
		try {

			String repeatTableName = requestParams.getRepeatTableName();
			String repeatTableRow = requestParams.getRepeatTableRow();

			if (!Strings.isNullOrEmpty(repeatTableRow)) {
				if ("null".equals(repeatTableRow)) {
					repeatTableRow = null;
				}
			}
			String repeatTableRowName = requestParams.getRepeatTableRowName();

			/*
			 * get all the fields values from the request and save them in the
			 * hash table
			 */
			this.putValuesInDatabase(repeatTableName, repeatTableRowName, 
					requestParams.getQuestionAnswersForRepeatSet(), user);

			return "";
		} catch (NullPointerException e) {
			LOGGER.error(e);
		} catch (PatternSyntaxException e) {
			LOGGER.error(e);
		}
		return "";
	}

	/**
	 * Saves the data from the repeating item set questions into the database by
	 * calling method from user DB connection.
	 * 
	 * @param tableName
	 *            Repeating item set table name.
	 * @param rowId
	 *            Row id to which data has to be stored.
	 * @param rowName
	 *            Row name
	 * @param theUser
	 *            User's object whose data has to be saved.
	 * @param params
	 *            Answers for the repeating item set.
	 * @param paramTypes
	 *            Types of the repeating item set table columns.
	 * @return int returns the inserted key.
	 */
	private void putValuesInDatabase(String repeatSetName, String instanceName, Map<String, Answer> answers,
			User theUser) {

		/* get database connection */
		UserDBConnection userDbConnection = theUser.getMyDataBank();

		/* send the table name and values to the database */
		userDbConnection.insertRepeatSetInstance(repeatSetName, instanceName, answers);

	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}
