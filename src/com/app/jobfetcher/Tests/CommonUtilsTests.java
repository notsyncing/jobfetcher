package com.app.jobfetcher.Tests;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.junit.Test;

import com.app.jobfetcher.Commons.CommonUtils;

public class CommonUtilsTests 
{
	
	@Test
	public void testTrimHtml() 
	{
		String src = "<td align=\"left\" style=\"font-size:12pt;height:12px;width:60%;\"><a href=\"../NewsShow/RecruitmentShow.aspx?id47=7149\" target=\"_blank\">四川威德福石化装备有限责任公司招聘会</a></td><td align=\"left\" style=\"font-size:12pt;height:12px;\">2014年10月21日10:00</td><td align=\"left\" style=\"font-size:12pt;height:12px;width:10%;\"><a href=\"../NewsShow/RecruitmentShow.aspx?id47=7149\" target=\"_blank\">详细信息</a></td>";
		String dst = "四川威德福石化装备有限责任公司招聘会|2014年10月21日10:00|详细信息";
		
		String s = CommonUtils.trimHtml(src, "|");
		assertEquals(s, dst);
	}
	
	@Test
	public void testGetHrefFromHtml()
	{
		String src = "<td align=\"left\" style=\"font-size:12pt;height:12px;width:60%;\"><a href=\"../NewsShow/RecruitmentShow.aspx?id47=7149\" target=\"_blank\">四川威德福石化装备有限责任公司招聘会</a></td><td align=\"left\" style=\"font-size:12pt;height:12px;\">2014年10月21日10:00</td><td align=\"left\" style=\"font-size:12pt;height:12px;width:10%;\"><a href=\"../NewsShow/RecruitmentShow.aspx?id47=7149\" target=\"_blank\">详细信息</a></td>";
		String url = "../NewsShow/RecruitmentShow.aspx?id47=7149";
		ArrayList<String> dst = new ArrayList<String>();
		dst.add(url);
		dst.add(url);
		
		ArrayList<String> l = CommonUtils.getHrefFromHtml(src);
		assertArrayEquals(l.toArray(), dst.toArray());
	}
	
	@Test
	public void testGetAbsoluteUrl() throws MalformedURLException
	{
		String base_url = "http://jiuye.swjtu.edu.cn/jdjy/ArticleList/RecruitConferenceOfMore.aspx";
		String rel_url = "../NewsShow/RecruitmentShow.aspx?id47=7149";
		String dst = "http://jiuye.swjtu.edu.cn/jdjy/NewsShow/RecruitmentShow.aspx?id47=7149";
		
		String s = CommonUtils.getAbsoluteUrl(base_url, rel_url);
		assertEquals(s, dst);
	}

}
