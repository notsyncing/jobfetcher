package com.app.jobfetcher.Servlets;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.jobfetcher.Fetchers.FetcherManager;
import com.app.jobfetcher.Fetchers.FetcherSWJTU;
import com.app.jobfetcher.Fetchers.FetcherUESTC;

/**
 * Servlet implementation class FetcherServlet
 */
@WebServlet(urlPatterns = { "/fetcher" }, loadOnStartup = 1)
public class FetcherServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private FetcherManager fm;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetcherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	public void init() throws ServletException 
	{
		super.init();
		
		fm = FetcherManager.getInstance();
		
		// TODO: Add new fetchers here
		fm.add(FetcherSWJTU.getInstance());
		fm.add(FetcherUESTC.getInstance());
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				fm.fetchAllThreaded(true);
			}
			
		}, 0, 1000 * 60 * 60 * 5);
	}

	
}
