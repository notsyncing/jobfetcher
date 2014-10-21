package com.app.jobfetcher.Servlets;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.app.jobfetcher.Commons.JobInfo;
import com.app.jobfetcher.Fetchers.FetcherManager;

public class JobService
{
	private static JobService instance = new JobService();
	
	private Logger log = Logger.getLogger("JobService");
	
	private FetcherManager fm;
	
	private JobService()
	{
		fm = FetcherManager.getInstance();
	}
	
	public static JobService getInstance()
	{
		return instance;
	}
	
	public JobInfo[] getAllJobs()
	{
		log.info("Getting all jobs...");
		
		ArrayList<JobInfo> l = new ArrayList<JobInfo>();
		
		for (int i = 0; i < fm.getCount(); i++) {
			ArrayList<JobInfo> al = fm.getJobList(i);
			if (al == null) {
				log.warning("No job data in " + fm.get(i).toString());
				continue;
			}
			
			l.addAll(fm.getJobList(i));
		}
		
		log.info("Got " + l.size() + " jobs.");
		
		return l.toArray(new JobInfo[0]);
	}
	
	public void fetchAll()
	{
		log.info("Fetching all jobs...");
		
		fm.fetchAll(false);
	}
	
	public void fetchAllThreaded()
	{
		fm.fetchAllThreaded(false);
	}
}