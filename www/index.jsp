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
<meta content="initial-scale=1.0, maximum-scale=4.0" name="viewport" />
<link href="/images/logo.png" rel="apple-touch-icon" />
<style type="text/css">
<% pageContext.include("/css/default.css"); %>
</style>
<title>Mobile Weather - mwthr.com</title>
</head>
<body>
<div class="overall" id="overall">
<table class="summary updating">
<tbody>
<tr><td class="label" colspan="2">Updating location...</td></tr>
<tr><td class="label" rowspan="2">Or choose location:</td><td><a href="/icao/<c:out value="${fn:toLowerCase(radar.icao)}" />"><c:out value="${place.nameshort}, ${place.state}" /></a></td></tr>
<tr><td><a href="/icao/">other...</a></td></tr>
</tbody>
</table>
</div>
<script type="text/javascript">
/* <![CDATA[ */
<% pageContext.include("/js/geolocation.js"); %>
/* ]]> */
</script>
</body>
</html>
