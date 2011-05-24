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

public enum Locator
{
//    STATION(new Station()),
    NEXRAD(new Nexrad()),
    CWA(new CountyWarningArea()),
    ;

    private final LocatorImpl implementation;
    
    Locator(LocatorImpl implementation)
    {
        this.implementation = implementation;
    }

    public Map get(String code)
    {
        return (Map) implementation.getMap().get(code);
    }

    public Map nearest(double[] where)
    {
        return ((where == null) ? null : (Map) implementation.getKDTree().nearest(where));
    }

    public Map nearest(Map props)
    {
        double[] where = (props == null) ? null : implementation.getCoordinates(props);
        return nearest(where);
    }
}
