package com.app.jobfetcher.Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.app.jobfetcher.Commons.ErrorCode;
import com.app.jobfetcher.Commons.JobInfo;
import com.app.jobfetcher.Fetchers.FetcherSWJTU;
import com.app.jobfetcher.Fetchers.FetcherUESTC;

public class FetcherTests 
{

	@Test
	public void testFetcherSWJTU()
	{
		assertEquals(FetcherSWJTU.getInstance().doFetch(true), ErrorCode.SUCCESS);
		ArrayList<JobInfo> l = FetcherSWJTU.getInstance().getJobList();
		assertNotEquals(l, null);
		assertNotEquals(l.size(), 0);
	}

	@Test
	public void testFetcherUESTC()
	{
		assertEquals(FetcherUESTC.getInstance().doFetch(true), ErrorCode.SUCCESS);
		ArrayList<JobInfo> l = FetcherSWJTU.getInstance().getJobList();
		assertNotEquals(l, null);
		assertNotEquals(l.size(), 0);
	}
}
