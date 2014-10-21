package com.app.jobfetcher.Fetchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.app.jobfetcher.Commons.CommonUtils;
import com.app.jobfetcher.Commons.ErrorCode;
import com.app.jobfetcher.Commons.JobInfo;
import com.app.jobfetcher.Commons.UrlFetcher;

public class FetcherSWJTU extends FetcherBase 
{
	private static FetcherSWJTU instance = new FetcherSWJTU();
	
	private static final String url = "http://jiuye.swjtu.edu.cn/jdjy/ArticleList/RecruitConferenceOfMore.aspx";
	private static final String encoding = "gb2312";
	
	private FetcherSWJTU()
	{
		
	}
	
	public static FetcherSWJTU getInstance()
	{
		return instance;
	}
	
	private Date strToDate(String s) throws ParseException
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
		return f.parse(s);
	}

	@Override
	public int doFetch(boolean refetch) 
	{
		if (!refetch) {
			if (getJobList() != null) {
				return ErrorCode.SUCCESS;
			}
		}
		
		setFetching(true);
		
		ArrayList<JobInfo> l = new ArrayList<JobInfo>();
		
		UrlFetcher fetcher = new UrlFetcher();
		
		try {
			fetcher.fetchUrl(url, encoding);
			
			if (fetcher.getReader() == null) {
				setFetching(false);
				return ErrorCode.FAILED;
			}
			
			fetcher.readLineUntilFirst("招聘会时间");
			fetcher.readLine();
			
			String line;
			while ((line = fetcher.readLine()) != null) {
				// Get name and date
				String info_str = CommonUtils.trimHtml(line, "|");
				if (info_str == null) {
					fetcher.close();
					break;
				}
				
				String[] info = info_str.split("\\|");
				JobInfo j = new JobInfo(info[0], "", strToDate(info[1]), "");
				System.out.println(j.getName());
				
				// Get location
				String loc_url = CommonUtils.getHrefFromHtml(line).get(0);
				UrlFetcher fetcher_loc = new UrlFetcher();
				fetcher_loc.fetchUrl(CommonUtils.getAbsoluteUrl(url, loc_url), encoding);
				String loc = fetcher_loc.readLineUntilFirst("地点：");
				String[] loc_data = CommonUtils.trimHtml(loc, "|").split("\\|");
				j.setLocation("西南交通大学 " + loc_data[3]);
				
				// Get information link
				fetcher_loc.readLineUntilFirst("相关招聘信息：");
				fetcher_loc.readLine();
				
				String info_string = fetcher_loc.readLine();
				ArrayList<String> info_urls = CommonUtils.getHrefFromHtml(info_string); 
				if (info_urls == null) {
					fetcher_loc.close();
					fetcher.readLine();
					continue;
				}
				
				String info_url = info_urls.get(0);
				info_url = CommonUtils.getAbsoluteUrl(url, info_url);
				j.setInfo(info_url);
					
				fetcher_loc.close();
				
				l.add(j);
				
				// Skip one line
				fetcher.readLine();
			}
			
			fetcher.close();
		} catch (Exception e) {
			e.printStackTrace();
			setFetching(false);
			return ErrorCode.FAILED;
		}
		
		setJobList(l);
		setFetching(false);
		return ErrorCode.SUCCESS;
	}

}
