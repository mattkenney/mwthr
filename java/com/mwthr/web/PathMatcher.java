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

/**
 * A utility class for matching request paths using regular expressions.
 */
public class PathMatcher
{
    /**
     * Cache of compiled regular expressions.
     */
    private static final ConcurrentMap<String, Pattern> cache = new ConcurrentHashMap<String, Pattern>();

    /**
     * The request paths.
     */
    private final String path;

    /**
     * The most recent {@link java.util.regex.Matcher}.
     */
    private Matcher last = null;

    /**
     * Constructor.
     * @param path the request path
     */
    public PathMatcher(String path)
    {
        this.path = path;
    }

    /**
     * Returns the substring captured by the specified group in the most recent
     * match attempt, or <code>null</code> if the group did not match.
     * @param n the request path
     * @return the substring captured by the specified group
     * @see java.util.regex.Matcher#group(int)
     */
    public String group(int n)
    {
        return last.group(n);
    }

    /**
     * Returns the {@link java.util.regex.Matcher} from the most recent match
     * attempt, or <code>null</code> if there has not been one.
     * @return the {@link java.util.regex.Matcher} from the most recent match attempt
     */
    public Matcher matcher()
    {
        return last;
    }

    /**
     * Tests the request path for this <code>PathMatcher</code> againts the
     * specified regular expression pattern.
     * @param patternString the regular expression pattern
     * @return true if the request path matches the specified pattern
     */
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
