package com.app.jobfetcher.Servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.jobfetcher.Commons.JobInfo;
import com.app.jobfetcher.Fetchers.FetcherManager;
import com.googlecode.jsonrpc4j.JsonRpcServer;

/**
 * Servlet implementation class JobServlet
 */
@WebServlet("/jobs")
public class JobServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private JobService service;
	private JsonRpcServer json_rpc_server;

    @Override
	public void init() throws ServletException 
    {
		super.init();
		
		service = JobService.getInstance();
		json_rpc_server = new JsonRpcServer(service, JobService.class);
	}

	/**
     * Default constructor. 
     */
    public JobServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		json_rpc_server.handle(request, response);
	}

}
