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
package com.mwthr.nws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mwthr.gae.URLDataHandler;

/**
 * This class fetches XML data from a URL and returns it as a
 * {@link java.util.Map java.util.Map}.
 */
public class Graph
    implements URLDataHandler
{
    /**
     * 90 minutes.
     */
    private static final long CACHE_LIFE = 5400000L;

    private final DateFormat formatCreation, formatForecast, formatHour, formatDay;

    private final List<Map<String, String>> stations;

    private final long cutoff;
    
    private TimeZone tz = null;

    /**
     * Constructor.
     * @param stations observation station properties maps
     */
    public Graph(List<Map<String, String>> stations)
    {
        this.stations = stations;

        // get max timestamp for a valid cache entry
        cutoff = System.currentTimeMillis() - CACHE_LIFE;

        // format for parsing observation timestamp
        formatCreation = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        formatCreation.setTimeZone(TimeZone.getTimeZone("GMT"));

        // format for parsing observation timestamp
        formatForecast = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        formatForecast.setTimeZone(TimeZone.getTimeZone("GMT"));

        // format for showing hour of day
        formatHour = new SimpleDateFormat("h aa", Locale.US);
        formatHour.setTimeZone(TimeZone.getTimeZone("GMT"));

        // format for showing day of week
        formatDay = new SimpleDateFormat("EEE", Locale.US);
        formatDay.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private String makeGraphURL(Map<String, String> props, long startTime, int hourCount)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("http://chart.apis.google.com/chart");
        buffer.append("?chs=298x250"); // Chart size
        buffer.append("&cht=lxy"); // Chart Type
        buffer.append("&chco=FF0000,0000CC"); // Series colors
        buffer.append("&chdl=Temperature|Probability+of+Precipitation"); // Chart legend text
        buffer.append("&chdlp=t"); // Chart legend position
        buffer.append("&chg=0,8,4,1,0,4"); // Grid
        buffer.append("&chxt=x,y,y,r,r"); // Visible Axes
        buffer.append("&chxs=1,FF0000|2,FF0000|3,0000CC|4,0000CC"); // Axis Label Style

        // Custom Axis Labels
        buffer.append("&chxl=0:");
        DateFormat format = hourCount > 24 ? formatDay : formatHour;
        Calendar cal = format.getCalendar();
        cal.setTimeInMillis(startTime);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        StringBuilder positions = new StringBuilder();
        for (int i = 1; i < hourCount; i++)
        {
            cal.add(Calendar.HOUR_OF_DAY, 1);
            if ((hourCount > 24) ? (cal.get(Calendar.HOUR_OF_DAY) == 12) : ((cal.get(Calendar.HOUR_OF_DAY) % 4) == 0))
            {
                long pos = (cal.getTimeInMillis() - startTime)/3600000L;
                positions.append(",");
                positions.append(pos);
                buffer.append("|");
                buffer.append(format.format(cal.getTime()));
            }
        }
        buffer.append("|2:|%C2%B0F|4:|%25");

        // Axis Label Positions
        buffer.append("&chxp=0");
        buffer.append(positions.toString());
        buffer.append("|1,0,20,40,60,80,100|2,50|3,0,20,40,60,80,100|4,50");

        // Axis Range
        buffer.append("&chxr=0,0,");
        buffer.append(hourCount);
        buffer.append("|1,-15,110|2,-15,110|3,-15,110|4,-15,110"); 

        // Data Scale
        buffer.append("&chds=0,");
        buffer.append(hourCount);
        buffer.append(",-15,110,0,");
        buffer.append(hourCount);
        buffer.append(",-15,110");

        // Data
        buffer.append("&chd=t");

        String[] data = new String[] { "temperature", "probability-of-precipitation" };

        for (int k = 0; k < data.length; k++)
        {
            String[] xValues = props.get(data[k] + "-x").split(",");
            int count = 0;
            for (int i = 0; i < xValues.length; i++)
            {
                if (Double.parseDouble(xValues[i]) > hourCount)
                {
                    break;
                }
                buffer.append(i > 0 ? "," : (k > 0 ? "|" : ":"));
                buffer.append(xValues[i]);
                count++;
            }
            String[] yValues = props.get(data[k] + "-y").split(",");
            for (int i = 0; i < count && i < yValues.length; i++)
            {
                buffer.append(i > 0 ? "," : "|");
                buffer.append(yValues[i]);
                count++;
            }
        }

        return buffer.toString();
    }

    @Override
    public List<URL> getURLs()
    {
        List<URL> result = new ArrayList<URL>();

        StringBuilder buffer = new StringBuilder();
        buffer.append("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php");
        buffer.append("?product=time-series&temp=temp&pop12=pop12");
        int length = buffer.length();

        for (Map<String, String> station : stations)
        {
            // build the NDFD URL
            buffer.setLength(length);
            buffer.append("&lat=");
            buffer.append(station.get("lat"));
            buffer.append("&lon=");
            buffer.append(station.get("lon"));
            String urlString = buffer.toString();
            try
            {
                result.add(new URL(urlString));
            }
            catch (Exception e)
            {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem parsing \"" + urlString + "\"", e);
            }
        }
        return result;
    }

    @Override
    public boolean isValid(Map<String, String> props)
    {
        String text = props.get("creation-date");
        long timestamp = WeatherDataHandler.parseDate(formatCreation, text);
        boolean result = (timestamp > cutoff);
        Calendar cal = formatCreation.getCalendar();
        cal.setTimeInMillis(timestamp);
        Logger.getLogger(getClass().getName()).log(result ? Level.FINE : Level.INFO, text + ", parsed " + formatCreation.format(cal.getTime()) + ", returning " + result);
        return result;
    }

    @Override
    public Map<String, String> parse(URL dataURL, byte [] data)
    {
        Map<String, String> result = new LinkedHashMap<String, String>();

        InputStream in = new ByteArrayInputStream(data);

        Document doc = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(in);
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem parsing XML", e);
        }

        StringBuilder buffer = new StringBuilder();
        try
        {
            XPathFactory factory = XPathFactory.newInstance();
            XPath path = factory.newXPath();

            // get creation date
            long min = System.currentTimeMillis();
            NodeList times = (NodeList) path.evaluate("//creation-date", doc, XPathConstants.NODESET);
            if (times.getLength() > 0)
            {
                min = WeatherDataHandler.parseDate(formatCreation, times.item(0).getTextContent());
                result.put("creation-date", times.item(0).getTextContent());
            }

            // get the parameter values
            NodeList parameters = (NodeList) path.evaluate("//parameters/*", doc, XPathConstants.NODESET);
            for (int i = 0; i < parameters.getLength(); i++)
            {
                // get the x-axis values name
                buffer.setLength(0);
                buffer.append(parameters.item(i).getNodeName());
                buffer.append("-x");
                String xKey = buffer.toString();

                // get the x-axis values - the hours after the creation-date
                buffer.setLength(0);
                String timeKey = (String) path.evaluate("@time-layout", parameters.item(i), XPathConstants.STRING);
                times = (NodeList) path.evaluate("//time-layout[layout-key=\"" + timeKey + "\"]/*", doc, XPathConstants.NODESET);
//Logger.getLogger(getClass().getName()).log(Level.INFO, "times: " + times.getLength());
                for (int j = 0; j < times.getLength(); j++)
                {
                    if ("start-valid-time".equals(times.item(j).getNodeName()))
                    {
                        long start = parseDate(times.item(j).getTextContent());
                        if (j + 1 < times.getLength())
                        {
                            long end = parseDate(times.item(j + 1).getTextContent());
                            start = (start + end)/2L;
                        }
                        if (buffer.length() > 0)
                        {
                            buffer.append(",");
                        }
                        buffer.append((start - min)/3600000.0);
                    }
                }
                result.put(xKey, buffer.toString());

                // get the y-axis values name
                buffer.setLength(0);
                buffer.append(parameters.item(i).getNodeName());
                buffer.append("-y");
                String yKey = buffer.toString();

                // get the y-axis values
                buffer.setLength(0);
                NodeList values = (NodeList) path.evaluate("value", parameters.item(i), XPathConstants.NODESET);
                for (int j = 0; j < values.getLength(); j++)
                {
                    if (buffer.length() > 0)
                    {
                        buffer.append(",");
                    }
                    buffer.append(values.item(j).getTextContent());
                }
                result.put(yKey, buffer.toString());
            }

            result.put("graph24", makeGraphURL(result, min, 24));
            result.put("graph120", makeGraphURL(result, min, 120));
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem interpreting XML", e);
        }

        return result;
    }

    long parseDate(String text)
    {
        long result = 0L;
        // 2011-06-11T20:00:00-04:00
        // 0000000000111111111122222
        // 0123456789012345678901234
        if (text != null && text.length() == 25)
        {
            // need to remove the colon to parse the timestamp
            String s = text.substring(0, 22).concat(text.substring(23));
            result = WeatherDataHandler.parseDate(formatForecast, s);
            // also note the first time zone we see
            if (tz == null)
            {
                tz = TimeZone.getTimeZone("GMT".concat(text.substring(19)));
                formatDay.setTimeZone(tz);
                formatHour.setTimeZone(tz);
            }
        }
        return result;
    }
}
