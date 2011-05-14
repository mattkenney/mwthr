package com.mwthr.nws;

import java.util.Map;

import net.sf.javaml.core.kdtree.KDTree;

public final class CountyWarningArea extends Location
{
    public final String state;
    public final String zone;
    public final String cwa;
    public final String name;
    public final String state_zone;
    public final String countyname;
    public final String fips;
    public final String time_zone;
    public final String fe_area;
    public final String lat;
    public final String lon;

    public CountyWarningArea(String line)
    {
        // get the data into the named final fields
        String state = null;
        String zone = null;
        String cwa = null;
        String name = null;
        String state_zone = null;
        String countyname = null;
        String fips = null;
        String time_zone = null;
        String fe_area = null;
        String lat = null;
        String lon = null;
        if (line != null)
        {
            final int length = line.length();
            for (int i = 0, start = 0; i < 11 && start < length; i++)
            {
                int end = line.indexOf('|', start);
                if (end < 0)
                {
                    end = length;
                }
                String field = line.substring(start, end);
                switch (i)
                {
                case 0:
                    state = field;
                    break;
                case 1:
                    zone = field;
                    break;
                case 2:
                    cwa = field;
                    break;
                case 3:
                    name = field;
                    break;
                case 4:
                    state_zone = field;
                    break;
                case 5:
                    countyname = field;
                    break;
                case 6:
                    fips = field;
                    break;
                case 7:
                    time_zone = field;
                    break;
                case 8:
                    fe_area = field;
                    break;
                case 9:
                    lat = field;
                    break;
                case 10:
                    lon = field;
                    break;
                }
                start = end + 1;
            }
        }
        this.state =        state;
        this.zone =         zone;
        this.cwa =          cwa;
        this.name =         name;
        this.state_zone =   state_zone;
        this.countyname =   countyname;
        this.fips =         fips;
        this.time_zone =    time_zone;
        this.fe_area =      fe_area;
        this.lat =          lat;
        this.lon =          lon;
    }

    public double[] getCoordinates()
    {
        return Location.getCoordinates(lat, lon);
    }

    public static KDTree getKDTree()
    {
        return Location.getKDTree(CountyWarningArea.class);
    }

    public static Map getMap()
    {
        return Location.getMap(Nexrad.class);
    }

    public String getName()
    {
        return zone;
    }

    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(state);
        buffer.append('|');
        buffer.append(zone);
        buffer.append('|');
        buffer.append(cwa);
        buffer.append('|');
        buffer.append(name);
        buffer.append('|');
        buffer.append(state_zone);
        buffer.append('|');
        buffer.append(countyname);
        buffer.append('|');
        buffer.append(fips);
        buffer.append('|');
        buffer.append(time_zone);
        buffer.append('|');
        buffer.append(fe_area);
        buffer.append('|');
        buffer.append(lat);
        buffer.append('|');
        buffer.append(lon);
        return buffer.toString();
    }
}
