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
package com.mwthr.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PathMatcher
{
    private static final ConcurrentMap<String, Pattern> cache = new ConcurrentHashMap<String, Pattern>();
    private final String path;
    private Matcher last = null;

    public PathMatcher(String path)
    {
        this.path = path;
    }

    public String group(int n)
    {
        return last.group(n);
    }

    public Matcher matcher()
    {
        return last;
    }

    public boolean matches(String patternString)
    {
        Pattern regex = cache.get(patternString);
        if (regex == null)
        {
            regex = Pattern.compile(patternString);
            cache.put(patternString, regex);
        }
        last = regex.matcher(path);
        return last.matches();
    }
}
