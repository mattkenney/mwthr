<%
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
%><%@ page
    import="java.util.*,
            net.sf.javaml.core.kdtree.*,
            com.mwthr.nws.*,
            com.mwthr.web.*"
%><%!
static final KDTree radarTree = Nexrad.getKDTree();
static final Map radarMap = Nexrad.getMap();
%><%
Nexrad radar = null;
String icao = request.getParameter("icao");
if (icao == null)
{
    double[] where = GeoIP.getCoordinates(application, request);
    radar = (where == null) ? null : (Nexrad) radarTree.nearest(where);
}
else
{
    radar = (Nexrad) radarMap.get(icao);
}
if (radar == null || radar.icao == null || radar.icao.length() == 0 || radar.name == null || radar.name.length() == 0)
{
    pageContext.forward("national.jsp");
    return;
}
String title = radar.name.trim() + " RADAR";
String id = radar.icao.substring(1);
String srcLoop = "http://radar.weather.gov/ridge/lite/N0R/" + id + "_loop.gif";
String src = "http://radar.weather.gov/ridge/lite/N0R/" + id + "_0.png";
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%= title %></title>
<meta name="viewport" content="initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
</head>
<body>
<div>
<noscript><img id="radar" alt="<%= title %>" height="550" src="<%= srcLoop %>" width="600" /></noscript>
<input id="jsloop" name="jsloop" type="hidden" value="<%= src %>" />
</div>
<script type="text/javascript" src="/js/radarloop.js"></script>
</body>
</html>
