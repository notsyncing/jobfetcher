package com.app.jobfetcher.Commons;

import java.util.Date;

public class JobInfo 
{
	private String name;
	private String location;
	private Date date;
	private String info;
	
	public JobInfo(String n, String l, Date d, String i)
	{
		setName(n);
		setLocation(l);
		setDate(d);
		setInfo(i);
	}
	
	public String toString()
	{
		return name + " " + location + " " + date + " " + info;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}
}
