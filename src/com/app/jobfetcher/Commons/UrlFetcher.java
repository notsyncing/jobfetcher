package com.app.jobfetcher.Commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlFetcher 
{
	private BufferedReader reader = null;
	private String base_url;
	
	public UrlFetcher()
	{
		
	}
	
	public BufferedReader fetchUrl(String url, String encoding) throws IOException
	{
		if (reader != null) {
			reader.close();
		}
		
		base_url = url;
		URL u = new URL(url);
		reader = new BufferedReader(new InputStreamReader(u.openStream(), encoding));
		return reader;
	}
	
	public BufferedReader getReader()
	{
		return reader;
	}
	
	public String readLineUntilFirst(String pattern) throws IOException
	{
		String curr_line;
		
		while ((curr_line = reader.readLine()) != null) {
			if (curr_line.contains(pattern)) {
				return curr_line;
			}
		}
		
		return null;
	}
	
	public String getBaseUrl()
	{
		return base_url;
	}
	
	public String readLine() throws IOException
	{
		return reader.readLine();
	}
	
	public void close() throws IOException
	{
		if (reader != null) {
			reader.close();
			reader = null;
		}
	}
}
