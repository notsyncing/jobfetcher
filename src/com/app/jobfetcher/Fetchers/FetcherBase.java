package com.app.jobfetcher.Fetchers;

import java.util.ArrayList;

import com.app.jobfetcher.Commons.JobInfo;

public abstract class FetcherBase 
{
	private ArrayList<JobInfo> job_list = null;
	private boolean is_fetching = false;
	
	public abstract int doFetch(boolean refetch);

	public ArrayList<JobInfo> getJobList()
	{
		return job_list;
	}
	
	protected void setJobList(ArrayList<JobInfo> l)
	{
		job_list = l;
	}
	
	protected boolean isFetching()
	{
		return is_fetching;
	}
	
	protected void setFetching(boolean f)
	{
		is_fetching = f;
	}
}
