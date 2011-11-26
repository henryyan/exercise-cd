<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>场馆概况</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />

		<link rel="stylesheet" type="text/css" href="../css/all.css"/>
		<link rel="stylesheet" type="text/css" href="../css/nyroModal.css" />
		<link rel="stylesheet" type="text/css" href="../information/info.css" />
		<link rel="stylesheet" type="text/css" href="../css/summary.css" />
		<style type="text/css">
		ul,li {
			margin: 0;
			padding: 0;
			list-style: none;
		}
		.singleInfo {
			display: block;
		}
		.singleInfo .infoHead {text-align: center;}
		.singleInfo .title {
			cursor: default;
			color: black;
			text-align: center;
		}
		</style>

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.nyroModal.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="js/data.js"></script>
	</head>
	<body>
		<div id="content">
			<div id="message">
				<div id="stadiumpic">
					<a href="${ctx }/venue/pictures/${user.venueInfo.id}/${user.venueInfo.photoUrl}" title="${pic.pictureName }" class="nyroModal">
						<img src="${ctx }/venue/pictures/${user.venueInfo.id}/thumbnails/120/${user.venueInfo.photoUrl}" width="100" height="100" border="0"/>
					</a>
				</div>
				<div id="stadiumname">
					<div id="sn_one">${user.venueInfo.venueName }场馆管理员</div>
					<div id="sn_two">当天日期：<%=DateUtil.getSysdate(DateUtil.TYPE_DATE) %></div>
					<div id="sn_weather">当天天气：8°C~14°C 多云 北风3~4级</div>
				</div>
			</div>
			<div id="list">
				<table width="356px" height="80px">
					<thead>
						<tr>
							<td width="300px">当日运营</td>
							<td><a href="#">+更多</a></td>
						</tr>
					</thead>
					<tbody style="background-color: #FFFFFF;">
						<tr>
							<td colspan="2">
								运营：（<span class="occupancy"></span>）场
							</td>
						</tr>
						<tr>
							<td colspan="2">
								闲置：（<span class="idling"></span>）场
							</td>
						</tr>
						<tr>
							<td colspan="2">
								收入金额：（<span class="income"></span>）元
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="list">
				<table width="356px" height="80px">
					<thead>
						<tr>
							<td width="300px">
								会员中心
							</td>
							<td>
								<a href="#">+更多</a>
							</td>
						</tr>
					</thead>
					<tbody style="background-color: #FFFFFF;">
						<tr>
							<td colspan="2">
								会员总数：（<span class="countMemberCard"></span>）人
							</td>
						</tr>
						<tr>
							<td colspan="2">
								有效会员：（<span class="countEnableMemberCard"></span>）人
							</td>
						</tr>
						<tr>
							<td colspan="2">
								有预定活动的会员：（<span class="countMemberHasOrder"></span>）人
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="list">
				<table width="356px" height="80px">
					<thead>
						<tr>
							<td width="300px">最新活动</td>
							<td>
								<a href="#">+更多</a>
							</td>
						</tr>
					</thead>
					<tbody style="background-color: #FFFFFF;">
						<tr>
							<td colspan="2">
								<ul id="infoList" class="data"></ul>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="list">
				<table width="356px" height="80px">
					<thead>
						<tr>
							<td width="300px">
								待处理列表
							</td>
							<td>
								<a href="#">+更多</a>
							</td>
						</tr>
					</thead>
					<tbody style="background-color: #FFFFFF;">
						<tr>
							<td colspan="2">
								待验证：（<span class="verification"></span>）场
							</td>
						</tr>
						<tr>
							<td colspan="2">
								将作废：（<span class="invalid"></span>）场
							</td>
						</tr>
						<tr>
							<td colspan="2">
								已预订待处理场次：（<span class="tacticsActivity"></span>）场
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<div id="infoTemp" class="template" style="width: 600px">
			<div id="info" class="singleInfo">
				<div class="infoHead">
					<span class="title"></span>
					<span class="info"></span>
				</div>
				<div class="infoContent"></div>
			</div>
		</div>
	</body>
</html>
