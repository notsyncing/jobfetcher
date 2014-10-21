/**
 * 
 */

function on_page_load()
{
	$("#job_list").text("正在载入...");
	
	$.jsonRPC.setup(
			{
				endPoint: "/jobfetcher/jobs"
			});
	
	$.jsonRPC.request('fetchAll', 
			{
				success: function (result) {
					$.jsonRPC.request('getAllJobs',
							{
								success: function (result) {
									print_jobs(result.result);
								},
								error: function (result) {
									$("#job_list").text("错误！" + result);
								}
							});
				},
				error: function (result) {
					$("#job_list").text("错误！" + result);
				}
			});
}

function sort_jobs_by_date(job_list)
{
	job_list.sort(
			function (a, b) {
				return a.date - b.date;
			});
}

function print_jobs(job_list)
{
	$("#job_list").empty();
	
	sort_jobs_by_date(job_list);
	
	for (var i = 0; i < job_list.length; i++) {
		var job_item = $("#job_info_template").clone(true);
		job_item.attr("id", "job_" + i);
		
		var job_date = new Date(job_list[i].date);
		
		if (job_date < new Date().setHours(0, 0, 0, 0)) {
			continue;
		}
		
		var job_date_str = job_date.format("yyyy-MM-dd");
		
		job_item.find(".name").text(job_list[i].name);
		job_item.find(".time").text(job_date.format("hh:mm"));
		job_item.find(".location").text(job_list[i].location);
		job_item.find(".info").text(job_list[i].info);
		job_item.find(".info").prop("href", job_list[i].info);
		
		var job_date_item = $("#job_list").find("#job_" + job_date_str);
		if (!job_date_item.length) {
			job_date_item = $("#job_day_template").clone(true);
			job_date_item.attr("id", "job_" + job_date_str);
			job_date_item.find(".date").text(job_date_str);
			$("#job_list").append(job_date_item);
		}
		
		job_date_item.find(".container").append(job_item);
	}
}

function on_job_day_clicked(o)
{
	var c = $(o).parent().find(".container");

	if (c.is(":visible")) {
		c.hide();
	} else {
		c.show();
	}
}