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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.ucla.wise.client.web.WiseHttpRequestParameters;
import edu.ucla.wise.commons.User;
import edu.ucla.wise.commons.databank.UserDBConnection;

/**
 * RepeatingItemSetIOServlet will handle retrieving survey page values sent
 * through AJAX calls currently implemented only for the repeating item set.
 * 
 */
@WebServlet("/survey/repeating_item_io")
public class RepeatingItemSetIOServlet extends AbstractUserSessionServlet {
    static final long serialVersionUID = 1000;
    private static final Logger LOGGER = Logger.getLogger(RepeatingItemSetIOServlet.class);

    /**
     * Gets all the data that is there in the repeating item table and prepares
     * a Json object.
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
        /* get database connection */
        UserDBConnection userDbConnection = user.getMyDataBank();

        /* get the table name from request */
        String repeatTableName = requestParams.getRepeatTableName();

        /* get the table values as a string */
        String repeatTableValues = userDbConnection.getAllDataForRepeatingSet(repeatTableName);
        return repeatTableValues;
    }

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
}
