<%@ page contentType="text/html; charset=UTF-8"
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
<meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
<meta name="viewport" content="initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
<title>mwthr: USA</title>
</head>
<body>
<img alt="radar" border="0" height="282" src="http://radar.weather.gov/Conus/RadarImg/latest_Small.gif" width="600" usemap="#radarmap" />
<map name="radarmap">
<area shape="poly" coords="77,0,73,25,65,36,7,26,12,0,77,0,77,0" href="/icao/katx" alt="Go to the Seattle/Tacoma, WA Doppler radar">
<area shape="poly" coords="121,0,113,44,108,46,74,26,77,0,118,0,121,0,121,0" href="/icao/kotx" alt="Go to the Spokane, WA Doppler radar">
<area shape="poly" coords="141,0,149,50,131,55,114,44,121,0,141,0,141,0" href="/icao/kmsx" alt="Go to the Missoula, MT Doppler radar">
<area shape="poly" coords="84,75,77,75,65,62,65,38,73,26,109,46,84,75,84,75" href="/icao/kpdt" alt="Go to the Pendleton, OR Doppler radar">
<area shape="poly" coords="7,26,66,37,66,62,18,65,7,63,7,26,7,26" href="/icao/krtx" alt="Go to the Portland, OR Doppler radar">
<area shape="poly" coords="10,138,36,119,42,112,47,97,16,66,7,63,10,134,10,138,10,138" href="/icao/kbhx" alt="Go to the Eureka, CA Doppler radar">
<area shape="poly" coords="84,75,102,123,76,121,69,110,67,90,75,77,84,75,84,75" href="/icao/krgx" alt="Go to the Reno, NV Doppler radar">
<area shape="poly" coords="130,55,126,85,81,76,109,45,113,44,130,55,130,55" href="/icao/kcbx" alt="Go to the Boise, ID Doppler radar">
<area shape="poly" coords="179,0,182,21,163,54,150,49,142,0,170,0,179,0,179,0" href="/icao/ktfx" alt="Go to the Great Falls, MT Doppler radar">
<area shape="poly" coords="76,75,66,89,47,96,16,65,63,62,74,73,76,75,76,75" href="/icao/kmax" alt="Go to the Medford, OR Doppler radar">
<area shape="poly" coords="67,89,68,109,43,111,48,96,67,89,67,89" href="/icao/kbbx" alt="Go to the Beal AFB, CA Doppler radar">
<area shape="poly" coords="74,119,69,125,37,118,42,112,68,108,74,119" href="/icao/kdax" alt="Go to the Sacramento, CA Doppler radar">
<area shape="poly" coords="69,126,64,141,22,168,10,138,37,119,69,126,69,126" href="/icao/kmux" alt="Go to the San Francisco Bay Area, CA Doppler radar">
<area shape="poly" coords="77,147,64,140,69,127,74,122,100,122,88,147,77,147,77,147" href="/icao/khnx" alt="Go to the San Joaquin Valley, CA Doppler radar">
<area shape="poly" coords="23,168,65,141,79,148,58,216,25,172,23,168,23,168" href="/icao/kvbx" alt="Go to the Vandenberg AFB, CA Doppler radar">
<area shape="poly" coords="87,148,92,156,80,184,57,214,79,148,87,148,87,148" href="/icao/kvtx" alt="Go to the Los Angeles, CA Doppler rada">
<area shape="poly" coords="108,125,114,156,93,156,88,148,101,123,108,125,108,125" href="/icao/keyx" alt="Go to the Edwards AFB, CA Doppler radar">
<area shape="poly" coords="114,157,83,179,93,156,114,157,114,157" href="/icao/ksox" alt="Go to the Santa Ana Mtns, CA Doppler radar">
<area shape="poly" coords="85,76,127,86,128,105,120,118,107,122,101,122,85,76,85,76" href="/icao/klrx" alt="Go to the Elko, NV Doppler radar">
<area shape="poly" coords="119,118,144,143,142,153,137,159,118,159,114,157,106,123,119,118,119,118" href="/icao/kesx" alt="Go to the Las Vegas, NV Doppler radar">
<area shape="poly" coords="104,232,57,217,83,178,114,158,117,162,106,224,104,232,104,232" href="/icao/knkx" alt="Go to the San Diego, CA Doppler radar">
<area shape="poly" coords="137,158,146,184,138,236,105,232,118,160,137,158,137,158" href="/icao/kyux" alt="Go to the Yuma, AZ Doppler radar">
<area shape="poly" coords="149,49,163,54,167,60,167,78,127,84,130,57,149,49,149,49" href="/icao/ksfx" alt="Go to the Pocatello/Idaho Falls, ID Doppler radar">
<area shape="poly" coords="166,77,173,93,165,110,129,105,127,85,166,77,166,77" href="/icao/kmtx" alt="Go to the Salt Lake City, UT Doppler radar">
<area shape="poly" coords="164,109,171,128,146,146,119,118,129,106,164,109,164,109" href="/icao/kicx" alt="Go to the Cedar City, UT Doppler radar">
<area shape="poly" coords="170,131,182,137,184,164,171,167,142,155,144,145,170,131,170,131" href="/icao/kfsx" alt="Go to the Flagstaff, AZ Doppler radar">
<area shape="poly" coords="146,186,139,159,142,155,171,166,146,186,146,186" href="/icao/kiwa" alt="Go to the Phoenix, AZ Doppler radar">
<area shape="poly" coords="184,163,186,166,186,237,139,236,145,186,172,168,184,163,184,163" href="/icao/kemx" alt="Go to the Tucson, AZ Doppler radar">
<area shape="poly" coords="234,0,234,33,217,50,183,22,179,0,234,0,234,0" href="/icao/kggw" alt="Go to the Glasgow, MT Doppler radar">
<area shape="poly" coords="215,57,167,59,164,53,182,21,218,51,215,57,215,57" href="/icao/kblx" alt="Go to the Billings, MT Doppler radar">
<area shape="poly" coords="211,57,214,63,200,90,172,93,166,78,167,58,211,57,211,57" href="/icao/kriw" alt="Go to the Riverton, WY Doppler radar">
<area shape="poly" coords="206,101,210,115,209,127,180,136,171,131,165,110,173,93,200,90,206,101,206,101" href="/icao/kgjx" alt="Go to the Grand Junction, CO Doppler radar">
<area shape="poly" coords="209,125,221,138,219,156,188,167,185,164,182,136,208,128,209,125,209,125" href="/icao/kabx" alt="Go to the Albuquerque, NM Doppler radar">
<area shape="poly" coords="219,156,230,174,228,185,191,167,219,156,219,156" href="/icao/khdx" alt="Go to the Holloman AFB, NM Doppler radar">
<area shape="poly" coords="226,184,227,215,219,239,186,237,186,166,226,184,226,184" href="/icao/kepz" alt="Go to the El Paso, TX Doppler radar">
<area shape="poly" coords="286,0,279,26,234,30,235,0,286,0,286,0" href="/icao/kmbx" alt="Go to the Minot AFB, ND Doppler radar">
<area shape="poly" coords="279,27,280,33,266,58,234,34,235,30,279,27,279,27" href="/icao/kbis" alt="Go to the Bismark, ND Doppler radar">
<area shape="poly" coords="243,83,216,64,215,57,217,50,234,33,266,59,243,83,243,83" href="/icao/kudx" alt="Go to the Rapid City, SD Doppler radar">
<area shape="poly" coords="243,88,239,95,206,101,200,89,215,63,242,82,243,88,243,88" href="/icao/kcys" alt="Go to the Cheyenne, WY Doppler radar">
<area shape="poly" coords="242,94,241,106,210,115,208,102,242,94,242,94" href="/icao/kftg" alt="Go to the Denver/Boulder, CO Doppler radar">
<area shape="poly" coords="240,107,248,127,241,134,221,137,210,126,210,116,240,107,240,107" href="/icao/kpux" alt="Go to the Pueblo, CO Doppler radar">
<area shape="poly" coords="240,133,246,156,240,170,230,173,219,157,222,136,240,133,240,133" href="/icao/kfdx" alt="Go to the Cannon AFB, NM Doppler radar">
<area shape="poly" coords="242,169,261,175,254,197,227,217,228,186,231,174,242,169,242,169" href="/icao/kmaf" alt="Go to the Midland/Odessa, TX Doppler radar">
<area shape="poly" coords="327,0,322,32,313,45,307,45,281,32,279,26,287,0,327,0,327,0" href="/icao/kmvx" alt="Go to the Fargo, ND Doppler radar">
<area shape="poly" coords="307,46,281,69,266,59,282,32,307,46,307,46" href="/icao/kabr" alt="Go to the Aberdeen, SD Doppler radar">
<area shape="poly" coords="282,70,285,78,269,100,244,89,244,83,266,59,282,70,282,70" href="/icao/klnx" alt="Go to the North Platte, NE Doppler radar">
<area shape="poly" coords="268,99,270,108,250,128,247,127,239,107,241,95,245,91,268,99,268,99" href="/icao/kgld" alt="Go to the Goodland, KS Doppler radar">
<area shape="poly" coords="284,115,284,122,270,142,250,128,273,107,284,115,284,115" href="/icao/kddc" alt="Go to the Dodge City, KS Doppler radar">
<area shape="poly" coords="270,141,265,156,246,156,241,134,248,127,250,128,270,141,270,141" href="/icao/kama" alt="Go to the Amarillo, TX Doppler radar">
<area shape="poly" coords="265,157,268,165,264,173,260,173,260,175,240,169,246,157,265,157,265,157" href="/icao/klbb" alt="Go to the Lubbock, TX Doppler radar">
<area shape="poly" coords="276,196,281,215,274,230,245,255,219,239,228,217,253,199,276,196,276,196" href="/icao/kdfx" alt="Go to the Laughlin AFB, TX Doppler radar">
<area shape="poly" coords="370,43,349,50,322,32,326,0,372,0,370,43,370,43" href="/icao/kdlh" alt="Go to the Duluth, MN Doppler radar">
<area shape="poly" coords="349,51,342,69,321,68,313,46,323,32,349,51,349,51" href="/icao/kmpx" alt="Go to the Minneapolis, MN Doppler radar">
<area shape="poly" coords="321,69,319,75,290,80,287,77,282,70,307,47,312,47,321,69,321,69" href="/icao/kfsd" alt="Go to the Sioux falls, SD Doppler radar">
<area shape="poly" coords="289,80,299,100,294,110,285,114,272,108,269,100,286,79,289,80,289,80" href="/icao/kuex" alt="Go to the Grand Island, NE Doppler radar">
<area shape="poly" coords="317,76,320,95,318,99,300,100,291,81,317,76,317,76" href="/icao/koax" alt="Go to the Omaha, NE Doppler radar">
<area shape="poly" coords="318,99,317,123,310,126,295,109,299,100,318,99,318,99" href="/icao/ktwx" alt="Go to the Topeka, KS Doppler radar">
<area shape="poly" coords="302,135,284,124,284,115,294,109,312,127,302,135,302,135" href="/icao/kict" alt="Go to the Wichita, KS Doppler radar">
<area shape="poly" coords="300,135,284,146,272,140,284,124,300,135,300,135" href="/icao/kvnx" alt="Go to the Vance AFB, OK Doppler radar">
<area shape="poly" coords="301,136,312,156,311,161,293,161,285,146,301,136,301,136" href="/icao/ktlx" alt="Go to the Oklahoma City, OK Doppler radar">
<area shape="poly" coords="292,160,289,166,268,164,265,155,271,141,284,145,292,160,292,160" href="/icao/kfdr" alt="Go to the Frederick, OK Doppler radar">
<area shape="poly" coords="288,167,289,182,280,190,265,173,267,165,288,167,288,167" href="/icao/kdyx" alt="Go to the Dyess AFB, TX Doppler radar">
<area shape="poly" coords="279,190,276,196,252,199,262,174,265,174,279,190,279,190" href="/icao/ksjt" alt="Go to the San Angelo, TX Doppler radar">
<area shape="poly" coords="281,189,305,205,304,208,281,214,277,195,281,189,281,189" href="/icao/kewx" alt="Go to the Austin/San Antonio, TX Doppler radar">
<area shape="poly" coords="305,209,318,230,273,232,284,215,305,209,305,209" href="/icao/kcrp" alt="Go to the Corpus Christi, TX Doppler radar">
<area shape="poly" coords="318,229,342,246,343,260,246,256,272,232,315,230,318,229,318,229" href="/icao/kbro" alt="Go to the Brownsville, TX Doppler radar">
<area shape="poly" coords="315,185,305,206,282,190,287,183,315,185,315,185" href="/icao/kgrk" alt="Go to the Central Texas Doppler radar">
<area shape="poly" coords="295,161,313,161,315,165,314,184,288,184,288,167,295,161,295,161" href="/icao/kfws" alt="Go to the Dallas/Fort Worth, TX Doppler radar">
<area shape="poly" coords="433,0,403,52,370,41,373,0,433,0,433,0" href="/icao/kmqt" alt="Go to the Marquette, MI Doppler radar">
<area shape="poly" coords="372,62,369,73,349,79,342,70,349,52,369,44,372,62,372,62" href="/icao/karx" alt="Go to the La Crosse, WI Doppler radar">
<area shape="poly" coords="348,78,348,99,346,101,321,98,318,76,322,69,343,70,348,78,348,78" href="/icao/kdmx" alt="Go to the Des Moines, IA Doppler radar">
<area shape="poly" coords="345,113,321,127,317,124,318,102,323,98,345,102,345,113,345,113" href="/icao/keax" alt="Go to the Kansas City/Pleasant Hill, MO Doppler radar">
<area shape="poly" coords="312,156,302,136,311,126,317,124,327,136,312,156" href="/icao/kinx" alt="Go to the Tulsa, OK Doppler radar">
<area shape="poly" coords="327,136,338,142,334,159,315,164,313,160,313,154,327,135,327,136,327,136" href="/icao/ksrx" alt="Go to the Western Arkansas/Ft. Smith, AR Doppler radar">
<area shape="poly" coords="334,160,351,171,326,188,314,186,315,165,334,160,334,160" href="/icao/kshv" alt="Go to the Shreveport, LA Doppler radar">
<area shape="poly" coords="314,186,325,188,343,245,319,230,305,207,314,186,314,186" href="/icao/khgx" alt="Go to the Houston/Galveston, TX Doppler radar">
<area shape="poly" coords="325,189,355,197,357,260,343,261,343,241,325,189,325,189" href="/icao/klch" alt="Go to the Lake Charles, LA Doppler radar">
<area shape="poly" coords="351,172,357,188,353,197,324,190,351,172,351,172" href="/icao/kpoe" alt="Go to the Fort Polk, LA Doppler radar">
<area shape="poly" coords="358,260,384,263,388,256,388,253,377,186,357,188,355,199,357,256,358,260,358,260" href="/icao/klix" alt="Go to the New Orleans/Baton Rouge, LA Doppler radar">
<area shape="poly" coords="361,163,372,164,385,178,375,187,357,188,351,172,361,163,361,163" href="/icao/kdgx" alt="Go to the Jackson/Brandon, MS Doppler radar">
<area shape="poly" coords="354,134,361,163,351,170,334,161,339,142,354,134,354,134" href="/icao/klzk" alt="Go to the Little Rock, AR Doppler radar">
<area shape="poly" coords="403,52,404,58,397,68,373,61,369,42,403,52,403,52" href="/icao/kgrb" alt="Go to the Green Bay, WI Doppler radar">
<area shape="poly" coords="397,68,397,75,375,83,369,73,373,61,397,68,397,68" href="/icao/kmkx" alt="Go to the Milwaukee, WI Doppler radar">
<area shape="poly" coords="375,83,375,88,362,99,348,100,349,79,368,74,375,83,375,83" href="/icao/kdvn" alt="Go to the Quad Cities, IA Doppler radar">
<area shape="poly" coords="361,100,377,116,363,133,355,133,344,112,345,102,347,100,361,100,361,100" href="/icao/klsx" alt="Go to the St. Louis, MO Doppler radar">
<area shape="poly" coords="363,132,386,145,371,162,361,163,356,135,363,132,363,132" href="/icao/knqa" alt="Go to the Memphis, TN Doppler radar">
<area shape="poly" coords="389,129,386,145,364,132,378,116,387,128,389,129" href="/icao/kpah" alt="Go to the Paducah, KY Doppler radar">
<area shape="poly" coords="390,102,378,115,361,100,376,88,390,102,390,102" href="/icao/kilx" alt="Go to the Central Illinois, IL Doppler radar">
<area shape="poly" coords="396,76,399,80,400,93,390,103,376,87,376,84,396,76,396,76" href="/icao/klot" alt="Go to the Chicago, IL Doppler radar">
<area shape="poly" coords="391,103,401,113,400,124,388,128,378,116,391,103,391,103" href="/icao/kvwx" alt="Go to the Evansville, IN Doppler radar">
<area shape="poly" coords="401,126,404,130,391,148,386,148,389,127,401,126,401,126" href="/icao/khpx" alt="Go to the Fort Campbell, KY Doppler radar">
<area shape="poly" coords="387,146,394,150,397,158,386,177,371,162,387,146,387,146" href="/icao/kgwx" alt="Go to the Columbus AFB, MS Doppler radar">
<area shape="poly" coords="400,184,395,246,389,254,377,186,387,176,400,184,400,184" href="/icao/kmob" alt="Go to the Mobile, AL Doppler radar">
<area shape="poly" coords="413,225,394,249,399,185,415,192,413,225,413,225" href="/icao/kevx" alt="Go to the Northwest Florida Doppler radar">
<area shape="poly" coords="412,163,398,157,387,178,398,183,412,163,412,163" href="/icao/kbmx" alt="Go to the Birmingham, AL Doppler radar">
<area shape="poly" coords="394,148,393,146,404,130,418,134,419,139,394,148,394,148" href="/icao/kohx" alt="Go to the Nashville, TN Doppler radar">
<area shape="poly" coords="417,111,422,117,421,131,419,133,403,130,400,124,402,115,417,111,417,111" href="/icao/klvx" alt="Go to the Louisville, KY Doppler radar">
<area shape="poly" coords="401,94,419,101,418,111,403,114,389,103,401,94,401,94" href="/icao/kind" alt="Go to the Indianapolis, IN Doppler radar">
<area shape="poly" coords="406,58,424,66,423,82,401,80,398,76,398,68,406,58,406,58" href="/icao/kgrr" alt="Go to the Grand Rapids/Muskegon, MI Doppler radar">
<area shape="poly" coords="423,82,427,90,418,101,399,93,400,80,423,82,423,82" href="/icao/kiwx" alt="Go to the Northern Indiana, IN Doppler radar">
<area shape="poly" coords="419,139,424,151,413,161,397,158,396,149,419,139,419,139" href="/icao/khtx" alt="Go to the Northern Alabama Doppler radar">
<area shape="poly" coords="471,0,456,48,423,66,405,59,404,52,434,0,471,0,471,0" href="/icao/kapx" alt="Go to the Gaylord, MI Doppler radar">
<area shape="poly" coords="455,49,456,64,436,90,427,90,424,81,424,66,455,49,455,49" href="/icao/kdtx" alt="Go to the Detroit, MI Doppler radar">
<area shape="poly" coords="446,104,438,116,424,118,417,111,418,101,428,92,436,93,446,104,446,104" href="/icao/kiln" alt="Go to the Wilmington, OH Doppler radar">
<area shape="poly" coords="438,116,448,133,421,132,423,119,438,116,438,116" href="/icao/kjkl" alt="Go to the Jackson, KY Doppler radar">
<area shape="poly" coords="449,134,450,137,432,154,426,152,420,142,420,136,423,133,449,134,449,134" href="/icao/kmrx" alt="Go to the Knoxville/Tri Cities, TN Doppler radar">
<area shape="poly" coords="431,154,434,160,425,177,422,176,411,162,424,152,431,154,431,154" href="/icao/kffc" alt="Go to the Atlanta, GA Doppler radar">
<area shape="poly" coords="420,176,399,184,412,164,420,176,420,176" href="/icao/kmxx" alt="Go to the East Alabama Doppler radar">
<area shape="poly" coords="421,177,424,179,428,183,418,192,398,184,421,177,421,177" href="/icao/keox" alt="Go to the Fort Rucker, AL Doppler radar">
<area shape="poly" coords="427,184,436,207,413,225,415,192,427,184,427,184" href="/icao/ktlh" alt="Go to the Tallahassee, FL Doppler radar">
<area shape="poly" coords="446,180,447,183,440,206,436,206,428,184,446,180,446,180" href="/icao/kvax" alt="Go to the Moody AFB, GA Doppler radar">
<area shape="poly" coords="435,161,445,165,446,168,445,181,428,183,423,178,435,161,435,161" href="/icao/kjgx" alt="Go to the Robins AFB, GA Doppler radar">
<area shape="poly" coords="433,154,450,138,460,146,444,165,436,160,433,154,433,154" href="/icao/kgsp" alt="Go to the Greer, SC Doppler radar">
<area shape="poly" coords="450,239,455,232,522,226,502,259,474,270,450,239,450,239" href="/icao/kamx" alt="Go to the Miami, FL Doppler radar">
<area shape="poly" coords="451,207,485,195,514,204,525,213,522,225,455,231,451,207,451,207" href="/icao/kmlb" alt="Go to the Melbourne, FL Doppler radar">
<area shape="poly" coords="484,194,450,208,440,206,448,183,484,194,484,194" href="/icao/kjax" alt="Go to the Jacksonville, FL Doppler radar">
<area shape="poly" coords="469,167,483,193,449,183,445,180,446,168,469,167,469,167" href="/icao/kclx" alt="Go to the Charleston, SC Doppler radar">
<area shape="poly" coords="446,163,460,147,466,149,469,154,469,167,448,168,446,163,446,163" href="/icao/kcae" alt="Go to the Columbia, SC Doppler radar">
<area shape="poly" coords="488,153,513,205,484,193,469,167,469,151,488,153,488,153" href="/icao/kltx" alt="Go to the Wilmington, NC Doppler radar">
<area shape="poly" coords="438,114,445,104,451,104,466,113,449,134,438,114,438,114" href="/icao/krlx" alt="Go to the Charleston, WV Doppler radar">
<area shape="poly" coords="466,113,473,112,481,123,481,127,465,148,459,145,449,135,466,113,466,113" href="/icao/kfcx" alt="Go to the Blacksburg, VA Doppler radar">
<area shape="poly" coords="481,128,493,143,488,153,471,152,466,148,481,128,481,128" href="/icao/krax" alt="Go to the Raleigh/Durham, NC Doppler radar">
<area shape="poly" coords="501,119,525,141,493,142,480,125,501,119,501,119" href="/icao/kakq" alt="Go to the Wakefield, VA Doppler radar">
<area shape="poly" coords="493,143,526,141,563,154,525,212,513,204,488,151,493,143,493,143" href="/icao/kmhx" alt="Go to the Morehead City, NC Doppler radar">
<area shape="poly" coords="473,113,480,106,504,99,502,119,481,126,473,113,473,113" href="/icao/klwx" alt="Go to the Sterling, VA Doppler radar">
<area shape="poly" coords="465,80,475,85,478,103,474,113,466,111,452,104,465,80,465,80" href="/icao/kpbz" alt="Go to the Pittsburgh, PA Doppler radar">
<area shape="poly" coords="456,64,464,79,452,104,444,103,435,92,456,64,456,64" href="/icao/kcle" alt="Go to the Cleveland, OH Doppler radar">
<area shape="poly" coords="492,80,504,98,478,106,474,87,492,80,492,80" href="/icao/kccx" alt="Go to the State Collegea, PA Doppler radar">
<area shape="poly" coords="505,95,564,153,525,140,502,119,505,95,505,95" href="/icao/kdox" alt="Go to the Dover AFB, DE Doppler radar">
<area shape="poly" coords="505,98,518,88,524,90,563,150,511,101,505,98,505,98" href="/icao/kdix" alt="Go to the Philadelphia, PA Doppler radar">
<area shape="poly" coords="525,87,540,79,577,139,564,153,525,87,525,87" href="/icao/kokx" alt="Go to the Upton, NY Doppler radar">
<area shape="poly" coords="493,79,495,71,515,75,516,83,518,88,505,99,493,79,493,79" href="/icao/kbgm" alt="Go to the Binghamton, NY Doppler radar">
<area shape="poly" coords="471,0,475,0,494,69,493,79,474,85,465,80,456,64,455,48,471,0,471,0" href="/icao/kbuf" alt="Go to the Buffalo, NY Doppler radar">
<area shape="poly" coords="522,64,516,74,495,70,475,0,503,0,522,64,522,64" href="/icao/ktyx" alt="Go to the Montague, NY Doppler radar">
<area shape="poly" coords="522,63,541,73,541,78,523,90,516,83,515,75,522,63,522,63" href="/icao/kenx" alt="Go to the Albany, NY Doppler radar">
<area shape="poly" coords="503,0,547,0,555,32,546,66,542,73,521,62,503,0,503,0" href="/icao/kcxx" alt="Go to the Burlington, VT Doppler radar">
<area shape="poly" coords="542,73,547,68,599,92,577,140,540,79,542,73,542,73" href="/icao/kbox" alt="Go to the Boston, MA Doppler radar">
<area shape="poly" coords="556,31,600,85,598,91,546,68,556,31,556,31" href="/icao/kgyx" alt="Go to the Portland, ME Doppler radar">
<area shape="poly" coords="599,0,599,81,555,31,547,0,599,0,599,0" href="/icao/kcbw" alt="Go to the Caribou, ME Doppler radar">
<area shape="poly" coords="355,133,340,141,327,137,322,128,344,114,355,133,355,133" href="/icao/ksgf" alt="Go to the Springfield, MO Doppler radar">
<area shape="poly" coords="394,249,415,223,435,208,440,207,450,209,454,231,447,238,394,249,394,249" href="/icao/ktbw" alt="Go to the Tampa Bay Area, FL Doppler radar">
<area shape="poly" coords="394,249,448,238,473,270,383,263,394,249,394,249" href="/icao/kbyx" alt="Go to the Key West, FL Doppler radar">
</map>
</body>
</html>
