package com.app.jobfetcher.Fetchers;

import java.util.ArrayList;

import com.app.jobfetcher.Commons.JobInfo;

class FetcherThread implements Runnable
{
	private FetcherBase fetcher;
	private boolean need_refetch;
	
	public FetcherThread(FetcherBase f, boolean refetch)
	{
		fetcher = f;
		need_refetch = refetch;
	}

	@Override
	public void run() 
	{
		fetcher.doFetch(need_refetch);
	}
}

public class FetcherManager 
{
	private static FetcherManager instance = new FetcherManager();
	
	private ArrayList<FetcherBase> fetchers;
	
	private FetcherManager()
	{
		fetchers = new ArrayList<FetcherBase>();
	}
	
	public static FetcherManager getInstance()
	{
		return instance;
	}
	
	public void add(FetcherBase f)
	{
		fetchers.add(f);
	}
	
	public FetcherBase get(int index)
	{
		return fetchers.get(index);
	}
	
	public void remove(int index)
	{
		fetchers.remove(index);
	}
	
	public void fetchAll(boolean refetch)
	{
		for (FetcherBase f : fetchers) {
			f.doFetch(refetch);
		}
	}
	
	public void fetchAllThreaded(boolean refetch)
	{
		for (FetcherBase f : fetchers) {
			Thread t = new Thread(new FetcherThread(f, refetch));			
			t.start();
		}
	}
	
	public boolean isFetching(int index)
	{
		return fetchers.get(index).isFetching();
	}
	
	public int getCount()
	{
		return fetchers.size();
	}
	
	public ArrayList<JobInfo> getJobList(int index)
	{
		return fetchers.get(index).getJobList();
	}
}
