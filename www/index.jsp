<%@ page
    import="java.util.*,
            net.sf.javaml.core.kdtree.*,
            com.mwthr.nws.*,
            com.mwthr.web.*"
%><%!
static final KDTree radarTree = Nexrad.getKDTree();
static final Map radarMap = Nexrad.getMap();
%><%
String title = "National Radar";
String src = null;
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
if (radar != null && radar.icao != null && radar.icao.length() > 0)
{
    title = radar.icao + " Radar";
    src = "http://radar.weather.gov/ridge/lite/N0R/" + radar.icao.substring(1) + "_loop.gif";
}
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%= title %></title>
</head>
<body>
<% if (radar == null) { %>
<img alt="<%= title %>" border="0" height="282" src="http://radar.weather.gov/Conus/RadarImg/latest_Small.gif" width="600" usemap="#radarmap" />
<map name="radarmap">
<area shape=poly coords="77,0,73,25,65,36,7,26,12,0,77,0,77,0" href="index.jsp?icao=KATX" alt="Go to the Seattle/Tacoma, WA Doppler radar">
<area shape=poly coords="121,0,113,44,108,46,74,26,77,0,118,0,121,0,121,0" href="index.jsp?icao=KOTX" alt="Go to the Spokane, WA Doppler radar">
<area shape=poly coords="141,0,149,50,131,55,114,44,121,0,141,0,141,0" href="index.jsp?icao=KMSX" alt="Go to the Missoula, MT Doppler radar">
<area shape=poly coords="84,75,77,75,65,62,65,38,73,26,109,46,84,75,84,75" href="index.jsp?icao=KPDT" alt="Go to the Pendleton, OR Doppler radar">
<area shape=poly coords="7,26,66,37,66,62,18,65,7,63,7,26,7,26" href="index.jsp?icao=KRTX" alt="Go to the Portland, OR Doppler radar">
<area shape=poly coords="10,138,36,119,42,112,47,97,16,66,7,63,10,134,10,138,10,138" href="index.jsp?icao=KBHX" alt="Go to the Eureka, CA Doppler radar">
<area shape=poly coords="84,75,102,123,76,121,69,110,67,90,75,77,84,75,84,75" href="index.jsp?icao=KRGX" alt="Go to the Reno, NV Doppler radar">
<area shape=poly coords="130,55,126,85,81,76,109,45,113,44,130,55,130,55" href="index.jsp?icao=KCBX" alt="Go to the Boise, ID Doppler radar">
<area shape=poly coords="179,0,182,21,163,54,150,49,142,0,170,0,179,0,179,0" href="index.jsp?icao=KTFX" alt="Go to the Great Falls, MT Doppler radar">
<area shape=poly coords="76,75,66,89,47,96,16,65,63,62,74,73,76,75,76,75" href="index.jsp?icao=KMAX" alt="Go to the Medford, OR Doppler radar">
<area shape=poly coords="67,89,68,109,43,111,48,96,67,89,67,89" href="index.jsp?icao=KBBX" alt="Go to the Beal AFB, CA Doppler radar">
<area shape=poly coords="74,119,69,125,37,118,42,112,68,108,74,119" href="index.jsp?icao=KDAX" alt="Go to the Sacramento, CA Doppler radar">
<area shape=poly coords="69,126,64,141,22,168,10,138,37,119,69,126,69,126" href="index.jsp?icao=KMUX" alt="Go to the San Francisco Bay Area, CA Doppler radar">
<area shape=poly coords="77,147,64,140,69,127,74,122,100,122,88,147,77,147,77,147" href="index.jsp?icao=KHNX" alt="Go to the San Joaquin Valley, CA Doppler radar">
<area shape=poly coords="23,168,65,141,79,148,58,216,25,172,23,168,23,168" href="index.jsp?icao=KVBX" alt="Go to the Vandenberg AFB, CA Doppler radar">
<area shape=poly coords="87,148,92,156,80,184,57,214,79,148,87,148,87,148" href="index.jsp?icao=KVTX" alt="Go to the Los Angeles, CA Doppler rada">
<area shape=poly coords="108,125,114,156,93,156,88,148,101,123,108,125,108,125" href="index.jsp?icao=KEYX" alt="Go to the Edwards AFB, CA Doppler radar">
<area shape=poly coords="114,157,83,179,93,156,114,157,114,157" href="index.jsp?icao=KSOX" alt="Go to the Santa Ana Mtns, CA Doppler radar">
<area shape=poly coords="85,76,127,86,128,105,120,118,107,122,101,122,85,76,85,76" href="index.jsp?icao=KLRX" alt="Go to the Elko, NV Doppler radar">
<area shape=poly coords="119,118,144,143,142,153,137,159,118,159,114,157,106,123,119,118,119,118" href="index.jsp?icao=KESX" alt="Go to the Las Vegas, NV Doppler radar">
<area shape=poly coords="104,232,57,217,83,178,114,158,117,162,106,224,104,232,104,232" href="index.jsp?icao=KNKX" alt="Go to the San Diego, CA Doppler radar">
<area shape=poly coords="137,158,146,184,138,236,105,232,118,160,137,158,137,158" href="index.jsp?icao=KYUX" alt="Go to the Yuma, AZ Doppler radar">
<area shape=poly coords="149,49,163,54,167,60,167,78,127,84,130,57,149,49,149,49" href="index.jsp?icao=KSFX" alt="Go to the Pocatello/Idaho Falls, ID Doppler radar">
<area shape=poly coords="166,77,173,93,165,110,129,105,127,85,166,77,166,77" href="index.jsp?icao=KMTX" alt="Go to the Salt Lake City, UT Doppler radar">
<area shape=poly coords="164,109,171,128,146,146,119,118,129,106,164,109,164,109" href="index.jsp?icao=KICX" alt="Go to the Cedar City, UT Doppler radar">
<area shape=poly coords="170,131,182,137,184,164,171,167,142,155,144,145,170,131,170,131" href="index.jsp?icao=KFSX" alt="Go to the Flagstaff, AZ Doppler radar">
<area shape=poly coords="146,186,139,159,142,155,171,166,146,186,146,186" href="index.jsp?icao=KIWA" alt="Go to the Phoenix, AZ Doppler radar">
<area shape=poly coords="184,163,186,166,186,237,139,236,145,186,172,168,184,163,184,163" href="index.jsp?icao=KEMX" alt="Go to the Tucson, AZ Doppler radar">
<area shape=poly coords="234,0,234,33,217,50,183,22,179,0,234,0,234,0" href="index.jsp?icao=KGGW" alt="Go to the Glasgow, MT Doppler radar">
<area shape=poly coords="215,57,167,59,164,53,182,21,218,51,215,57,215,57" href="index.jsp?icao=KBLX" alt="Go to the Billings, MT Doppler radar">
<area shape=poly coords="211,57,214,63,200,90,172,93,166,78,167,58,211,57,211,57" href="index.jsp?icao=KRIW" alt="Go to the Riverton, WY Doppler radar">
<area shape=poly coords="206,101,210,115,209,127,180,136,171,131,165,110,173,93,200,90,206,101,206,101" href="index.jsp?icao=KGJX" alt="Go to the Grand Junction, CO Doppler radar">
<area shape=poly coords="209,125,221,138,219,156,188,167,185,164,182,136,208,128,209,125,209,125" href="index.jsp?icao=KABX" alt="Go to the Albuquerque, NM Doppler radar">
<area shape=poly coords="219,156,230,174,228,185,191,167,219,156,219,156" href="index.jsp?icao=KHDX" alt="Go to the Holloman AFB, NM Doppler radar">
<area shape=poly coords="226,184,227,215,219,239,186,237,186,166,226,184,226,184" href="index.jsp?icao=KEPZ" alt="Go to the El Paso, TX Doppler radar">
<area shape=poly coords="286,0,279,26,234,30,235,0,286,0,286,0" href="index.jsp?icao=KMBX" alt="Go to the Minot AFB, ND Doppler radar">
<area shape=poly coords="279,27,280,33,266,58,234,34,235,30,279,27,279,27" href="index.jsp?icao=KBIS" alt="Go to the Bismark, ND Doppler radar">
<area shape=poly coords="243,83,216,64,215,57,217,50,234,33,266,59,243,83,243,83" href="index.jsp?icao=KUDX" alt="Go to the Rapid City, SD Doppler radar">
<area shape=poly coords="243,88,239,95,206,101,200,89,215,63,242,82,243,88,243,88" href="index.jsp?icao=KCYS" alt="Go to the Cheyenne, WY Doppler radar">
<area shape=poly coords="242,94,241,106,210,115,208,102,242,94,242,94" href="index.jsp?icao=KFTG" alt="Go to the Denver/Boulder, CO Doppler radar">
<area shape=poly coords="240,107,248,127,241,134,221,137,210,126,210,116,240,107,240,107" href="index.jsp?icao=KPUX" alt="Go to the Pueblo, CO Doppler radar">
<area shape=poly coords="240,133,246,156,240,170,230,173,219,157,222,136,240,133,240,133" href="index.jsp?icao=KFDX" alt="Go to the Cannon AFB, NM Doppler radar">
<area shape=poly coords="242,169,261,175,254,197,227,217,228,186,231,174,242,169,242,169" href="index.jsp?icao=KMAF" alt="Go to the Midland/Odessa, TX Doppler radar">
<area shape=poly coords="327,0,322,32,313,45,307,45,281,32,279,26,287,0,327,0,327,0" href="index.jsp?icao=KMVX" alt="Go to the Fargo, ND Doppler radar">
<area shape=poly coords="307,46,281,69,266,59,282,32,307,46,307,46" href="index.jsp?icao=KABR" alt="Go to the Aberdeen, SD Doppler radar">
<area shape=poly coords="282,70,285,78,269,100,244,89,244,83,266,59,282,70,282,70" href="index.jsp?icao=KLNX" alt="Go to the North Platte, NE Doppler radar">
<area shape=poly coords="268,99,270,108,250,128,247,127,239,107,241,95,245,91,268,99,268,99" href="index.jsp?icao=KGLD" alt="Go to the Goodland, KS Doppler radar">
<area shape=poly coords="284,115,284,122,270,142,250,128,273,107,284,115,284,115" href="index.jsp?icao=KDDC" alt="Go to the Dodge City, KS Doppler radar">
<area shape=poly coords="270,141,265,156,246,156,241,134,248,127,250,128,270,141,270,141" href="index.jsp?icao=KAMA" alt="Go to the Amarillo, TX Doppler radar">
<area shape=poly coords="265,157,268,165,264,173,260,173,260,175,240,169,246,157,265,157,265,157" href="index.jsp?icao=KLBB" alt="Go to the Lubbock, TX Doppler radar">
<area shape=poly coords="276,196,281,215,274,230,245,255,219,239,228,217,253,199,276,196,276,196" href="index.jsp?icao=KDFX" alt="Go to the Laughlin AFB, TX Doppler radar">
<area shape=poly coords="370,43,349,50,322,32,326,0,372,0,370,43,370,43" href="index.jsp?icao=KDLH" alt="Go to the Duluth, MN Doppler radar">
<area shape=poly coords="349,51,342,69,321,68,313,46,323,32,349,51,349,51" href="index.jsp?icao=KMPX" alt="Go to the Minneapolis, MN Doppler radar">
<area shape=poly coords="321,69,319,75,290,80,287,77,282,70,307,47,312,47,321,69,321,69" href="index.jsp?icao=KFSD" alt="Go to the Sioux falls, SD Doppler radar">
<area shape=poly coords="289,80,299,100,294,110,285,114,272,108,269,100,286,79,289,80,289,80" href="index.jsp?icao=KUEX" alt="Go to the Grand Island, NE Doppler radar">
<area shape=poly coords="317,76,320,95,318,99,300,100,291,81,317,76,317,76" href="index.jsp?icao=KOAX" alt="Go to the Omaha, NE Doppler radar">
<area shape=poly coords="318,99,317,123,310,126,295,109,299,100,318,99,318,99" href="index.jsp?icao=KTWX" alt="Go to the Topeka, KS Doppler radar">
<area shape=poly coords="302,135,284,124,284,115,294,109,312,127,302,135,302,135" href="index.jsp?icao=KICT" alt="Go to the Wichita, KS Doppler radar">
<area shape=poly coords="300,135,284,146,272,140,284,124,300,135,300,135" href="index.jsp?icao=KVNX" alt="Go to the Vance AFB, OK Doppler radar">
<area shape=poly coords="301,136,312,156,311,161,293,161,285,146,301,136,301,136" href="index.jsp?icao=KTLX" alt="Go to the Oklahoma City, OK Doppler radar">
<area shape=poly coords="292,160,289,166,268,164,265,155,271,141,284,145,292,160,292,160" href="index.jsp?icao=KFDR" alt="Go to the Frederick, OK Doppler radar">
<area shape=poly coords="288,167,289,182,280,190,265,173,267,165,288,167,288,167" href="index.jsp?icao=KDYX" alt="Go to the Dyess AFB, TX Doppler radar">
<area shape=poly coords="279,190,276,196,252,199,262,174,265,174,279,190,279,190" href="index.jsp?icao=KSJT" alt="Go to the San Angelo, TX Doppler radar">
<area shape=poly coords="281,189,305,205,304,208,281,214,277,195,281,189,281,189" href="index.jsp?icao=KEWX" alt="Go to the Austin/San Antonio, TX Doppler radar">
<area shape=poly coords="305,209,318,230,273,232,284,215,305,209,305,209" href="index.jsp?icao=KCRP" alt="Go to the Corpus Christi, TX Doppler radar">
<area shape=poly coords="318,229,342,246,343,260,246,256,272,232,315,230,318,229,318,229" href="index.jsp?icao=KBRO" alt="Go to the Brownsville, TX Doppler radar">
<area shape=poly coords="315,185,305,206,282,190,287,183,315,185,315,185" href="index.jsp?icao=KGRK" alt="Go to the Central Texas Doppler radar">
<area shape=poly coords="295,161,313,161,315,165,314,184,288,184,288,167,295,161,295,161" href="index.jsp?icao=KFWS" alt="Go to the Dallas/Fort Worth, TX Doppler radar">
<area shape=poly coords="433,0,403,52,370,41,373,0,433,0,433,0" href="index.jsp?icao=KMQT" alt="Go to the Marquette, MI Doppler radar">
<area shape=poly coords="372,62,369,73,349,79,342,70,349,52,369,44,372,62,372,62" href="index.jsp?icao=KARX" alt="Go to the La Crosse, WI Doppler radar">
<area shape=poly coords="348,78,348,99,346,101,321,98,318,76,322,69,343,70,348,78,348,78" href="index.jsp?icao=KDMX" alt="Go to the Des Moines, IA Doppler radar">
<area shape=poly coords="345,113,321,127,317,124,318,102,323,98,345,102,345,113,345,113" href="index.jsp?icao=KEAX" alt="Go to the Kansas City/Pleasant Hill, MO Doppler radar">
<area shape=poly coords="312,156,302,136,311,126,317,124,327,136,312,156" href="index.jsp?icao=KINX" alt="Go to the Tulsa, OK Doppler radar">
<area shape=poly coords="327,136,338,142,334,159,315,164,313,160,313,154,327,135,327,136,327,136" href="index.jsp?icao=KSRX" alt="Go to the Western Arkansas/Ft. Smith, AR Doppler radar">
<area shape=poly coords="334,160,351,171,326,188,314,186,315,165,334,160,334,160" href="index.jsp?icao=KSHV" alt="Go to the Shreveport, LA Doppler radar">
<area shape=poly coords="314,186,325,188,343,245,319,230,305,207,314,186,314,186" href="index.jsp?icao=KHGX" alt="Go to the Houston/Galveston, TX Doppler radar">
<area shape=poly coords="325,189,355,197,357,260,343,261,343,241,325,189,325,189" href="index.jsp?icao=KLCH" alt="Go to the Lake Charles, LA Doppler radar">
<area shape=poly coords="351,172,357,188,353,197,324,190,351,172,351,172" href="index.jsp?icao=KPOE" alt="Go to the Fort Polk, LA Doppler radar">
<area shape=poly coords="358,260,384,263,388,256,388,253,377,186,357,188,355,199,357,256,358,260,358,260" href="index.jsp?icao=KLIX" alt="Go to the New Orleans/Baton Rouge, LA Doppler radar">
<area shape=poly coords="361,163,372,164,385,178,375,187,357,188,351,172,361,163,361,163" href="index.jsp?icao=KDGX" alt="Go to the Jackson/Brandon, MS Doppler radar">
<area shape=poly coords="354,134,361,163,351,170,334,161,339,142,354,134,354,134" href="index.jsp?icao=KLZK" alt="Go to the Little Rock, AR Doppler radar">
<area shape=poly coords="403,52,404,58,397,68,373,61,369,42,403,52,403,52" href="index.jsp?icao=KGRB" alt="Go to the Green Bay, WI Doppler radar">
<area shape=poly coords="397,68,397,75,375,83,369,73,373,61,397,68,397,68" href="index.jsp?icao=KMKX" alt="Go to the Milwaukee, WI Doppler radar">
<area shape=poly coords="375,83,375,88,362,99,348,100,349,79,368,74,375,83,375,83" href="index.jsp?icao=KDVN" alt="Go to the Quad Cities, IA Doppler radar">
<area shape=poly coords="361,100,377,116,363,133,355,133,344,112,345,102,347,100,361,100,361,100" href="index.jsp?icao=KLSX" alt="Go to the St. Louis, MO Doppler radar">
<area shape=poly coords="363,132,386,145,371,162,361,163,356,135,363,132,363,132" href="index.jsp?icao=KNQA" alt="Go to the Memphis, TN Doppler radar">
<area shape=poly coords="389,129,386,145,364,132,378,116,387,128,389,129" href="index.jsp?icao=KPAH" alt="Go to the Paducah, KY Doppler radar">
<area shape=poly coords="390,102,378,115,361,100,376,88,390,102,390,102" href="index.jsp?icao=KILX" alt="Go to the Central Illinois, IL Doppler radar">
<area shape=poly coords="396,76,399,80,400,93,390,103,376,87,376,84,396,76,396,76" href="index.jsp?icao=KLOT" alt="Go to the Chicago, IL Doppler radar">
<area shape=poly coords="391,103,401,113,400,124,388,128,378,116,391,103,391,103" href="index.jsp?icao=KVWX" alt="Go to the Evansville, IN Doppler radar">
<area shape=poly coords="401,126,404,130,391,148,386,148,389,127,401,126,401,126" href="index.jsp?icao=KHPX" alt="Go to the Fort Campbell, KY Doppler radar">
<area shape=poly coords="387,146,394,150,397,158,386,177,371,162,387,146,387,146" href="index.jsp?icao=KGWX" alt="Go to the Columbus AFB, MS Doppler radar">
<area shape=poly coords="400,184,395,246,389,254,377,186,387,176,400,184,400,184" href="index.jsp?icao=KMOB" alt="Go to the Mobile, AL Doppler radar">
<area shape=poly coords="413,225,394,249,399,185,415,192,413,225,413,225" href="index.jsp?icao=KEVX" alt="Go to the Northwest Florida Doppler radar">
<area shape=poly coords="412,163,398,157,387,178,398,183,412,163,412,163" href="index.jsp?icao=KBMX" alt="Go to the Birmingham, AL Doppler radar">
<area shape=poly coords="394,148,393,146,404,130,418,134,419,139,394,148,394,148" href="index.jsp?icao=KOHX" alt="Go to the Nashville, TN Doppler radar">
<area shape=poly coords="417,111,422,117,421,131,419,133,403,130,400,124,402,115,417,111,417,111" href="index.jsp?icao=KLVX" alt="Go to the Louisville, KY Doppler radar">
<area shape=poly coords="401,94,419,101,418,111,403,114,389,103,401,94,401,94" href="index.jsp?icao=KIND" alt="Go to the Indianapolis, IN Doppler radar">
<area shape=poly coords="406,58,424,66,423,82,401,80,398,76,398,68,406,58,406,58" href="index.jsp?icao=KGRR" alt="Go to the Grand Rapids/Muskegon, MI Doppler radar">
<area shape=poly coords="423,82,427,90,418,101,399,93,400,80,423,82,423,82" href="index.jsp?icao=KIWX" alt="Go to the Northern Indiana, IN Doppler radar">
<area shape=poly coords="419,139,424,151,413,161,397,158,396,149,419,139,419,139" href="index.jsp?icao=KHTX" alt="Go to the Northern Alabama Doppler radar">
<area shape=poly coords="471,0,456,48,423,66,405,59,404,52,434,0,471,0,471,0" href="index.jsp?icao=KAPX" alt="Go to the Gaylord, MI Doppler radar">
<area shape=poly coords="455,49,456,64,436,90,427,90,424,81,424,66,455,49,455,49" href="index.jsp?icao=KDTX" alt="Go to the Detroit, MI Doppler radar">
<area shape=poly coords="446,104,438,116,424,118,417,111,418,101,428,92,436,93,446,104,446,104" href="index.jsp?icao=KILN" alt="Go to the Wilmington, OH Doppler radar">
<area shape=poly coords="438,116,448,133,421,132,423,119,438,116,438,116" href="index.jsp?icao=KJKL" alt="Go to the Jackson, KY Doppler radar">
<area shape=poly coords="449,134,450,137,432,154,426,152,420,142,420,136,423,133,449,134,449,134" href="index.jsp?icao=KMRX" alt="Go to the Knoxville/Tri Cities, TN Doppler radar">
<area shape=poly coords="431,154,434,160,425,177,422,176,411,162,424,152,431,154,431,154" href="index.jsp?icao=KFFC" alt="Go to the Atlanta, GA Doppler radar">
<area shape=poly coords="420,176,399,184,412,164,420,176,420,176" href="index.jsp?icao=KMXX" alt="Go to the East Alabama Doppler radar">
<area shape=poly coords="421,177,424,179,428,183,418,192,398,184,421,177,421,177" href="index.jsp?icao=KEOX" alt="Go to the Fort Rucker, AL Doppler radar">
<area shape=poly coords="427,184,436,207,413,225,415,192,427,184,427,184" href="index.jsp?icao=KTLH" alt="Go to the Tallahassee, FL Doppler radar">
<area shape=poly coords="446,180,447,183,440,206,436,206,428,184,446,180,446,180" href="index.jsp?icao=KVAX" alt="Go to the Moody AFB, GA Doppler radar">
<area shape=poly coords="435,161,445,165,446,168,445,181,428,183,423,178,435,161,435,161" href="index.jsp?icao=KJGX" alt="Go to the Robins AFB, GA Doppler radar">
<area shape=poly coords="433,154,450,138,460,146,444,165,436,160,433,154,433,154" href="index.jsp?icao=KGSP" alt="Go to the Greer, SC Doppler radar">
<area shape=poly coords="450,239,455,232,522,226,502,259,474,270,450,239,450,239" href="index.jsp?icao=KAMX" alt="Go to the Miami, FL Doppler radar">
<area shape=poly coords="451,207,485,195,514,204,525,213,522,225,455,231,451,207,451,207" href="index.jsp?icao=KMLB" alt="Go to the Melbourne, FL Doppler radar">
<area shape=poly coords="484,194,450,208,440,206,448,183,484,194,484,194" href="index.jsp?icao=KJAX" alt="Go to the Jacksonville, FL Doppler radar">
<area shape=poly coords="469,167,483,193,449,183,445,180,446,168,469,167,469,167" href="index.jsp?icao=KCLX" alt="Go to the Charleston, SC Doppler radar">
<area shape=poly coords="446,163,460,147,466,149,469,154,469,167,448,168,446,163,446,163" href="index.jsp?icao=KCAE" alt="Go to the Columbia, SC Doppler radar">
<area shape=poly coords="488,153,513,205,484,193,469,167,469,151,488,153,488,153" href="index.jsp?icao=KLTX" alt="Go to the Wilmington, NC Doppler radar">
<area shape=poly coords="438,114,445,104,451,104,466,113,449,134,438,114,438,114" href="index.jsp?icao=KRLX" alt="Go to the Charleston, WV Doppler radar">
<area shape=poly coords="466,113,473,112,481,123,481,127,465,148,459,145,449,135,466,113,466,113" href="index.jsp?icao=KFCX" alt="Go to the Blacksburg, VA Doppler radar">
<area shape=poly coords="481,128,493,143,488,153,471,152,466,148,481,128,481,128" href="index.jsp?icao=KRAX" alt="Go to the Raleigh/Durham, NC Doppler radar">
<area shape=poly coords="501,119,525,141,493,142,480,125,501,119,501,119" href="index.jsp?icao=KAKQ" alt="Go to the Wakefield, VA Doppler radar">
<area shape=poly coords="493,143,526,141,563,154,525,212,513,204,488,151,493,143,493,143" href="index.jsp?icao=KMHX" alt="Go to the Morehead City, NC Doppler radar">
<area shape=poly coords="473,113,480,106,504,99,502,119,481,126,473,113,473,113" href="index.jsp?icao=KLWX" alt="Go to the Sterling, VA Doppler radar">
<area shape=poly coords="465,80,475,85,478,103,474,113,466,111,452,104,465,80,465,80" href="index.jsp?icao=KPBZ" alt="Go to the Pittsburgh, PA Doppler radar">
<area shape=poly coords="456,64,464,79,452,104,444,103,435,92,456,64,456,64" href="index.jsp?icao=KCLE" alt="Go to the Cleveland, OH Doppler radar">
<area shape=poly coords="492,80,504,98,478,106,474,87,492,80,492,80" href="index.jsp?icao=KCCX" alt="Go to the State Collegea, PA Doppler radar">
<area shape=poly coords="505,95,564,153,525,140,502,119,505,95,505,95" href="index.jsp?icao=KDOX" alt="Go to the Dover AFB, DE Doppler radar">
<area shape=poly coords="505,98,518,88,524,90,563,150,511,101,505,98,505,98" href="index.jsp?icao=KDIX" alt="Go to the Philadelphia, PA Doppler radar">
<area shape=poly coords="525,87,540,79,577,139,564,153,525,87,525,87" href="index.jsp?icao=KOKX" alt="Go to the Upton, NY Doppler radar">
<area shape=poly coords="493,79,495,71,515,75,516,83,518,88,505,99,493,79,493,79" href="index.jsp?icao=KBGM" alt="Go to the Binghamton, NY Doppler radar">
<area shape=poly coords="471,0,475,0,494,69,493,79,474,85,465,80,456,64,455,48,471,0,471,0" href="index.jsp?icao=KBUF" alt="Go to the Buffalo, NY Doppler radar">
<area shape=poly coords="522,64,516,74,495,70,475,0,503,0,522,64,522,64" href="index.jsp?icao=KTYX" alt="Go to the Montague, NY Doppler radar">
<area shape=poly coords="522,63,541,73,541,78,523,90,516,83,515,75,522,63,522,63" href="index.jsp?icao=KENX" alt="Go to the Albany, NY Doppler radar">
<area shape=poly coords="503,0,547,0,555,32,546,66,542,73,521,62,503,0,503,0" href="index.jsp?icao=KCXX" alt="Go to the Burlington, VT Doppler radar">
<area shape=poly coords="542,73,547,68,599,92,577,140,540,79,542,73,542,73" href="index.jsp?icao=KBOX" alt="Go to the Boston, MA Doppler radar">
<area shape=poly coords="556,31,600,85,598,91,546,68,556,31,556,31" href="index.jsp?icao=KGYX" alt="Go to the Portland, ME Doppler radar">
<area shape=poly coords="599,0,599,81,555,31,547,0,599,0,599,0" href="index.jsp?icao=KCBW" alt="Go to the Caribou, ME Doppler radar">
<area shape=poly coords="355,133,340,141,327,137,322,128,344,114,355,133,355,133" href="index.jsp?icao=KSGF" alt="Go to the Springfield, MO Doppler radar">
<area shape=poly coords="394,249,415,223,435,208,440,207,450,209,454,231,447,238,394,249,394,249" href="index.jsp?icao=KTBW" alt="Go to the Tampa Bay Area, FL Doppler radar">
<area shape=poly coords="394,249,448,238,473,270,383,263,394,249,394,249" href="index.jsp?icao=KBYX" alt="Go to the Key West, FL Doppler radar">
<!-- <area shape=poly coords="0,281,600,281,600,91,578,139,563,156,526,212,523,225,502,260,474,270,384,264,357,260,343,261,244,256,218,240,186,238,138,237,105,233,56,216,21,167,9,138,6,63,6,26,12,0,0,0,0,278,0,281" href="index_loop.php" alt="Click to loop this image"> -->
</map>
<% } else { %>
<img alt="<%= title %>" height="550" src="<%= src %>" width="600" />
<% } %>
</body>
</html>
