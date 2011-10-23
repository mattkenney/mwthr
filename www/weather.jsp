<%@ page contentType="text/html; charset=UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
/*
 * Copyright 2011 Matt Kenney
 *
 * This file is part of mwthr.
 *
 * mwthr is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * mwthr is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mwthr.  If not, see <http://www.gnu.org/licenses/>.
*/
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta content="text/html;charset=UTF-8" http-equiv="Content-type" />
<!-- source code: https://github.com/mattkenney/mwthr -->
<meta content="initial-scale=1.0, maximum-scale=4.0" name="viewport" />
<link href="/images/logo.png" rel="apple-touch-icon" />
<script type="text/javascript">
/* <![CDATA[ */
<% pageContext.include("/js/geoupdate.js"); %>
/* ]]> */
</script>
<style type="text/css">
<% pageContext.include("/css/default.css"); %>
</style>
<title>Mobile Weather - mwthr.com</title>
</head>
<body>
<div class="overall" id="overall">
<table class="summary summary-top">
<tbody>
<tr><td><c:out value="${place.nameshort}, ${place.state}" /></td><td class="option"><a href="/icao/">change</a></td></tr>
<tr><td colspan="2">Now: <c:if test="${empty current.temp_f}">?</c:if><c:out value="${current.temp_f}" /> &#176;F<c:if test="${!empty current.weather}">, <c:out value="${current.weather}" /></c:if></td></tr>
<tr><td colspan="2">Next 24 hours: <c:if test="${empty forecast.temperature}">?</c:if><c:out value="${forecast.temperature}" /> &#176;F<c:if test="${!empty forecast['probability-of-precipitation']}">, <c:out value="${forecast['probability-of-precipitation']}" />% PoP</c:if></td></tr>
<tr><td>Radar: <c:out value="${radar.name}" /></td><td class="option"><a href="<c:out value="${radar.statusurl}" />" target="_blank">status</a></td></tr>
</tbody>
</table>
<table class="summary summary-middle tabs">
<tbody>
<tr>
<c:choose>
<c:when test="${duration==24}">
<td class="tab"><div class="tab-div"><a href="now" class="tab-a">Radar</a></div></td>
<td class="tab"><div class="tab-div tab-active">24 Hour</div></td>
<td class="tab"><div class="tab-div"><a href="120" class="tab-a">5 Day</a></div></td>
<td class="tab"><div class="tab-div"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td class="tab"><div class="tab-div"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<div class="forecast">
<img alt="24 hour forecast" class="forecast-image" src="<c:out value="${graph.graph24}" />" /></div>
</c:when>
<c:when test="${duration==120}">
<td class="tab"><div class="tab-div"><a href="now" class="tab-a">Radar</a></div></td>
<td class="tab"><div class="tab-div"><a href="24" class="tab-a">24 Hour</a></div></td>
<td class="tab"><div class="tab-div tab-active">5 Day</div></td>
<td class="tab"><div class="tab-div"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td class="tab"><div class="tab-div"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<div class="forecast">
<img alt="5 day forecast" class="forecast-image" src="<c:out value="${graph.graph120}" />" /></div>
</c:when>
<c:when test="${ocean=='atl'}">
<td class="tab"><div class="tab-div"><a href="now" class="tab-a">Radar</a></div></td>
<td class="tab"><div class="tab-div"><a href="24" class="tab-a">24 Hour</a></div></td>
<td class="tab"><div class="tab-div"><a href="120" class="tab-a">5 Day</a></div></td>
<td class="tab"><div class="tab-div tab-active">Atlantic</div></td>
<td class="tab"><div class="tab-div"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<div class="summary summary-ocean">
<img alt="Atlantic tropical cyclone activity" class="summary-ocean-image" src="http://www.nhc.noaa.gov/tafb_latest/danger_atl_latestBW_sm2.gif" />
</div>
</c:when>
<c:when test="${ocean=='pac'}">
<td class="tab"><div class="tab-div"><a href="now" class="tab-a">Radar</a></div></td>
<td class="tab"><div class="tab-div"><a href="24" class="tab-a">24 Hour</a></div></td>
<td class="tab"><div class="tab-div"><a href="120" class="tab-a">5 Day</a></div></td>
<td class="tab"><div class="tab-div"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td class="tab"><div class="tab-div tab-active">Pacific</div></td>
</tr>
</tbody>
</table>
<div class="summary summary-ocean">
<img alt="Eastern pacific tropical cyclone activity" class="summary-ocean-image" src="http://www.nhc.noaa.gov/tafb_latest/danger_pac_latestBW_sm2.gif" />
</div>
</c:when>
<c:otherwise>
<td class="tab"><div class="tab-div tab-active">Radar</div></td>
<td class="tab"><div class="tab-div"><a href="24" class="tab-a">24 Hour</a></div></td>
<td class="tab"><div class="tab-div"><a href="120" class="tab-a">5 Day</a></div></td>
<td class="tab"><div class="tab-div"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td class="tab"><div class="tab-div"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<div class="summary-radar">
<noscript><img alt="<c:out value="${radar.icao}" /> Radar" class="radar-image" src="<c:out value="${radar.baseurl}" />Loop.gif" /></noscript>
<input id="jsloop" name="jsloop" type="hidden" value="<c:out value="${radar.baseurl}" />" />
<script type="text/javascript">
/* <![CDATA[ */
<% pageContext.include("/js/radarloop.js"); %>
/* ]]> */
</script>
</div>
</c:otherwise>
</c:choose>
<table class="subtle">
<tbody>
<tr><td>Like? Don't like? Let me know: matt<img alt="" src="/images/at.png" />mwthr.com</td></tr>
</tbody>
</table>
</div>
</body>
</html>
