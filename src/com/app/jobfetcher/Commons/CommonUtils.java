package com.app.jobfetcher.Commons;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CommonUtils 
{
	public static String trimHtml(String s, String to)
	{
		if (s.isEmpty()) {
			return s;
		}
		
		String data = s.trim();
		String result1 = new String(data);
		
		int tag_start = -1, tag_end;
		
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == '<') {
				tag_start = i;
			}
			
			if (data.charAt(i) == '>') {
				tag_end = i + 1;
				
				if (tag_start >= 0) {
					result1 = result1.replace(data.substring(tag_start, tag_end), to);
					tag_start = -1;
				}
			}
		}
		
		String rep = to;
		if (to.equals("|")) {
			rep = "\\|";
		}
		
		result1 = result1.replace("&nbsp;", "");
		
		String[] a = result1.split(rep);
		String result2 = new String();
		
		for (String t : a) {
			if ((!t.isEmpty()) && (!t.equals(" "))) {
				result2 += t.trim() + to;
			}
		}

		if (result2.length() <= 0) {
			return null;
		}
		
		return result2.substring(0, result2.length() - to.length());
	}
	
	public static ArrayList<String> getHrefFromHtml(String html)
	{
		if (!html.contains("href")) {
			return null;
		}
		
		ArrayList<String> urls = new ArrayList<String>();
		
		int start = 0, href_start, href_end;
		while ((href_start = html.indexOf("href=", start)) >= 0) {
			start = href_start + 1;
			href_end = html.indexOf('"', href_start + 6);
			String s = html.substring(href_start + 6, href_end);
			urls.add(s);
		}
		
		return urls;
	}
	
	public static String getAbsoluteUrl(String base_url, String rel_url) throws MalformedURLException
	{
		URL base = new URL(base_url);
		URL abs = new URL(base, rel_url);
		return abs.toString();
	}
	
	public static boolean hasJobName(ArrayList<JobInfo> l, String name)
	{
		for (JobInfo j : l) {
			if (j.getName().equals(name)) {
				return true;
			}
		}
		
		return false;
	}
}
