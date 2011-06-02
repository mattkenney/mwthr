<%@ page contentType="text/html; charset=UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
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
<meta content="initial-scale=1.0" name="viewport" />
<link href="/images/logo.png" rel="apple-touch-icon" />
<style type="text/css">
<% pageContext.include("/css/default.css"); %>
</style>
<title>mwthr</title>
</head>
<body>
<div>
<noscript>
<table class="summary updating">
<tbody>
<tr><td class="label" rowspan="2">Choose location:</td><td><a href="/icao/<c:out value="${fn:toLowerCase(radar.icao)}" />">County/Area - <c:out value="${cwa.name}" />, <c:out value="${cwa.state}" /></a></td></tr>
<tr><td><a href="/icao/">other...</a></td></tr>
</tbody>
</table>
</noscript>
<form action="/" id="location" method="post">
<input name="lat" type="hidden" value="" />
<input name="lon" type="hidden" value="" />
</form>
<input id="icao" name="icao" type="hidden" value="/icao/<c:out value="${fn:toLowerCase(radar.icao)}" />" />
<input id="name" name="name" type="hidden" value="County/Area - <c:out value="${cwa.name}" />, <c:out value="${cwa.state}" />" />
</div>
<script type="text/javascript">
/* <![CDATA[ */
<% pageContext.include("/js/geolocation.js"); %>
/* ]]> */
</script>
</body>
</html>
