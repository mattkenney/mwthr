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

import java.util.Map;

import net.sf.javaml.core.kdtree.KDTree;

public final class CountyWarningArea extends Location
{
    private final String state;
    private final String zone;
    private final String cwa;
    private final String name;
    private final String state_zone;
    private final String countyname;
    private final String fips;
    private final String time_zone;
    private final String fe_area;
    private final String lat;
    private final String lon;

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
    
    public String getState()
    {
        return state;
    }

    public String getZone()
    {
        return zone;
    }

    public String getCwa()
    {
        return cwa;
    }

    public String getName()
    {
        return name;
    }

    public String getState_zone()
    {
        return state_zone;
    }

    public String getCountyname()
    {
        return countyname;
    }

    public String getFips()
    {
        return fips;
    }

    public String getTime_zone()
    {
        return time_zone;
    }

    public String getFe_area()
    {
        return fe_area;
    }

    public String getLat()
    {
        return lat;
    }

    public String getLon()
    {
        return lon;
    }

    public double[] getCoordinates()
    {
        return Location.getCoordinates(lat, lon);
    }

    public static KDTree getKDTree()
    {
        return Location.getKDTree(CountyWarningArea.class);
    }

    public String getKey()
    {
        return zone;
    }

    public static Map getMap()
    {
        return Location.getMap(Nexrad.class);
    }

    public boolean isValid()
    {
        return
            zone != null && zone.length() > 0 &&
            name != null && name.length() > 0 &&
            state != null && state.length() > 0 &&
            fips != null && fips.length() > 0;
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
