/*
 * Copyright (C) 2018 Andreas Redmer <ar-gdroid@abga.be>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.gdroid.gdroid.tasks;

import android.util.Pair;

import org.gdroid.gdroid.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AbstractJsonParser {


    /**
     *
     * @param app
     * @param name
     * @return A pair of (ItemArray,Locale)
     * @throws JSONException
     */
    protected Pair<JSONArray,String> getLocalizedArrayItemAndLocale(JSONObject app, String name) throws JSONException {
        final String repoLocale="en";

        // Map locale -> itemcontent
        Map<String, JSONArray> availalableLocales = new HashMap<>();

        //gather available Locales for this item
        final JSONArray unLocalisedItem = app.optJSONArray(name);
        if (unLocalisedItem != null)
        {
            availalableLocales.put(repoLocale,unLocalisedItem);
        }

        final JSONObject localized = app.optJSONObject("localized");
        if (localized != null)
        {
            Iterator<String> keys = localized.keys();

            while(keys.hasNext()) {
                String resLocale = keys.next();
                final JSONObject localizedJSONObject = localized.getJSONObject(resLocale);
                final JSONArray localisedArrayItem = localizedJSONObject.optJSONArray(name);
                if (localisedArrayItem != null)
                {
                    availalableLocales.put(resLocale,localisedArrayItem);
                }
            }
        }

        // for some items (like 'name') having none is bad, others (like 'whatsNew') are optional
        if (availalableLocales.isEmpty())
            return null;

        final Set<String> keySet = availalableLocales.keySet();
        String[] resLocales = keySet.toArray(new String[keySet.size()]);
        final String usableLocale = Util.getUsableLocale(resLocales);

        // this can't be null anymore, otherwise we wouldn't have arrived here
        return new Pair<JSONArray,String>(availalableLocales.get(usableLocale), usableLocale);
    }
}
