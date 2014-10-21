package com.app.jobfetcher.Fetchers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import com.app.jobfetcher.Commons.CommonUtils;
import com.app.jobfetcher.Commons.ErrorCode;
import com.app.jobfetcher.Commons.JobInfo;
import com.app.jobfetcher.Commons.UrlFetcher;

public class FetcherUESTC extends FetcherBase 
{
	private static FetcherUESTC instance = new FetcherUESTC();
	
	private Logger log = Logger.getLogger("FetcherUESTC");
	
	private static final String url = "http://www.jiuye.org/sort.php?sortid=3";
	private static final String encoding = "gb2312";
	
	private FetcherUESTC()
	{
		
	}
	
	public static FetcherUESTC getInstance()
	{
		return instance;
	}
	
	private Date strToDate(String y, String m, String d, String tm) throws ParseException
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return f.parse(y + "-" + m + "-" + d + " " + tm);
	}
	
	private void fetchInfo(JobInfo j, String line) throws IOException, ParseException
	{
		// Get date and location
		String loc_url = CommonUtils.getHrefFromHtml(line).get(0);
		UrlFetcher fetcher_loc = new UrlFetcher();
		String info_url = CommonUtils.getAbsoluteUrl(url, loc_url);
		fetcher_loc.fetchUrl(info_url, encoding);
		
		String date_str = fetcher_loc.readLineUntilFirst("招聘时间：");
		String date_data = CommonUtils.trimHtml(date_str, "|");
		String[] date_info = date_data.split("\\|");
		
		if (date_info[7].contains(":")) {
			j.setDate(strToDate(date_info[1], date_info[3], date_info[5], date_info[7]));
		} else {
			j.setDate(strToDate(date_info[1], date_info[3], date_info[5], date_info[7] + ":" + date_info[9]));
		}
		
		String loc;
		
		if (date_data.contains("招聘地点")) {
			loc = date_data.substring(date_data.indexOf("招聘地点"));
			j.setLocation("电子科技大学 " + loc);
		} else {
			loc = fetcher_loc.readLineUntilFirst("招聘地点：");
			String[] loc_data = CommonUtils.trimHtml(loc, "|").split("\\|");
			j.setLocation("电子科技大学 " + loc_data[0]);
		}
		
		// Get information link
		j.setInfo(info_url);
		
		fetcher_loc.close();
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
		int page_counter = 1;
		
		try {
			fetcher.fetchUrl(url, encoding);
			
			if (fetcher.getReader() == null) {
				setFetching(false);
				return ErrorCode.FAILED;
			}
			
			fetcher.readLineUntilFirst("images/jrz.jpg");
			fetcher.readLine();
			
			String line;
			// Get today
			while ((line = fetcher.readLine()) != null) {
				if (line.contains("images/mrz.jpg")) {
					// Get tomorrow
					fetcher.readLine();
					continue;
				}
				
				if (line.contains("rightm")) {
					// Get others
					break;
				}
				
				// Get name
				String info_str = CommonUtils.trimHtml(line, "|");
				if (info_str == null) {
					continue;
				}
				
				String[] info = info_str.split("\\|");
				
				if ((info[0] == null) || (info[0].isEmpty())) {
					continue;
				}
				
				JobInfo j = new JobInfo(info[0], "", new Date(), "");
				System.out.println(j.getName());
				
				try {
					fetchInfo(j, line);
				} catch (Exception e) {
					log.warning("Exception: " + e.getMessage());
					log.warning("Name: " + j.getName());
					log.warning("Line: " + line);
					continue;
				}
				
				l.add(j);
			}
			
			// Get pages
			fetcher.readLine();
			while ((line = fetcher.readLine()) != null) {
				if (line.contains("next")) {
					// Page completed
					page_counter++;
					
					if (page_counter > 5) {
						// Fetch 5 pages
						break;
					}
					
					// Go to next page
					fetcher.fetchUrl(url + "&p=" + page_counter, encoding);
					fetcher.readLineUntilFirst("rightm");
					fetcher.readLine();
					continue;
				}
				
				// Get name
				String info_str = CommonUtils.trimHtml(line, "|");
				if (info_str == null) {
					continue;
				}
				
				String[] info = info_str.split("\\|");
				
				if (info.length < 2) {
					continue;
				}
				
				JobInfo j = new JobInfo(info[1], "", new Date(), "");
				System.out.println(j.getName());
				
				try {
					fetchInfo(j, line);
				} catch (Exception e) {
					log.warning("Exception: " + e.getMessage());
					log.warning("Name: " + j.getName());
					log.warning("Line: " + line);
					continue;
				}
				
				if (!CommonUtils.hasJobName(l, j.getName())) {
					l.add(j);
				}
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
