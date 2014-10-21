package com.app.jobfetcher.Fetchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import com.app.jobfetcher.Commons.CommonUtils;
import com.app.jobfetcher.Commons.ErrorCode;
import com.app.jobfetcher.Commons.JobInfo;
import com.app.jobfetcher.Commons.UrlFetcher;

public class FetcherSCU extends FetcherBase 
{
	private static FetcherSCU instance = new FetcherSCU();
	
	private Logger log = Logger.getLogger("FetcherSCU");
	
	private static final String url = "http://222.18.15.135/jiuye/news.php?start=0&type_id=4";
	private static final String encoding = "gb2312";
	
	private FetcherSCU()
	{
		
	}
	
	public static FetcherSCU getInstance()
	{
		return instance;
	}
	
	private Date strToDate(String s) throws ParseException
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
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
			for (int page = 0; page < 12; page++) {
				String f_url = url.replace("start=0", "start=" + (page * 22));
				fetcher.fetchUrl(f_url, encoding);
				
				if (fetcher.getReader() == null) {
					setFetching(false);
					return ErrorCode.FAILED;
				}
				
				String line = fetcher.readLineUntilFirst("ic1.jpg");
				String[] jobs_urls = CommonUtils.getHrefFromHtml(line).toArray(new String[0]);
				
				for (String s : jobs_urls) {
					try {
						String job_url = CommonUtils.getAbsoluteUrl(url, s);
						UrlFetcher fetcher_info = new UrlFetcher();
						fetcher_info.fetchUrl(job_url, encoding);
						
						String name_line = fetcher_info.readLineUntilFirst("定于");
						String[] name_data = CommonUtils.trimHtml(name_line, "|").split("\\|");
						JobInfo j = new JobInfo(name_data[0], "", new Date(), job_url);
						System.out.println(j.getName());
						
						String info_line = fetcher_info.readLineUntilFirst("招聘时间：");
						String info_line_data = CommonUtils.trimHtml(info_line, "|");
						String[] info_data = info_line_data.split("\\|");
						
						for (int i = 0; i < info_data.length; i++) {
							if (info_data[i].contains("招聘时间：")) {
								j.setDate(strToDate(info_data[i + 1]));
								j.setLocation("四川大学 " + info_data[i + 3]);
							}
						}
	
						fetcher_info.close();
					
						l.add(j);
					} catch (Exception e) {
						log.warning(e.getMessage());
						log.warning("Line: " + s);
						continue;
					}
				}
				
				fetcher.close();
			}
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
