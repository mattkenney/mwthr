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

/**
 * Types of locations with available
 * <a href="http://www.weather.gov/">US NWS</a> weather data.
 */
public enum Locator
{
    /**
     * Observation stations.
     */
    STATION(new Station()),

    /**
     * Radar.
     */
    NEXRAD(new Nexrad()),

    /**
     * County Warning Areas.
     */
    CWA(new CountyWarningArea()),
    ;

    /**
     * Reference to the package-private object that implements the enum entry.
     */
    private final LocatorImpl implementation;

    /**
     * Package-private contructor.
     * @param implementation the package-private object that implements the enum entry
     */
    Locator(LocatorImpl implementation)
    {
        this.implementation = implementation;
    }

    /**
     * Returns the property {@link java.util.Map} for the specified location.
     * @param code the code that identifies the location
     * @return the property {@link java.util.Map} for the specified location
     */
    public Map<String, String> get(String code)
    {
        return implementation.getMap().get(code);
    }

    /**
     * Returns the property {@link java.util.Map} for the location nearest the
     * specified latitude/longitude.
     * @param where [ latitude, longitude ]
     * @return the property {@link java.util.Map} for the nearest location
     */
    @SuppressWarnings("unchecked") // KDTree is not Java 5 generic
    public Map<String, String> nearest(double[] where)
    {
        // uses nearest-neighbor logic from net.sf.javaml.core.kdtree.KDTree
        return ((where == null) ? null : (Map<String, String>) implementation.getKDTree().nearest(where));
    }

    /**
     * Returns the property {@link java.util.Map} for the location nearest the
     * specified location. The props map must contain "lat" and "lon" properties
     * containing the latitude and longitude of that location. This method
     * will find the location of one type nearest a location of another type.
     * @param props the property {@link java.util.Map} of another location
     * @return the property {@link java.util.Map} for the nearest location
     */
    public Map<String, String> nearest(Map<String, String> props)
    {
        double[] where = (props == null) ? null : implementation.getCoordinates(props);
        return nearest(where);
    }
}
