/*
 * Copyright (C) 2014 Mukesh Y authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagdiv.android.gogleapplication;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Mukesh Y
 */
public class CalendarUtility {
	public static ArrayList<String> nameOfEvent = new ArrayList<String>();
	public static ArrayList<String> startDates = new ArrayList<String>();
	public static ArrayList<String> endDates = new ArrayList<String>();
	public static ArrayList<String> descriptions = new ArrayList<String>();

	/*public static ArrayList<String> readCalendarEvent(Context context) {
		Cursor cursor = context.getContentResolver()
				.query(Uri.parse("content://com.android.calendar/events"),
						new String[] { "calendar_id", "title", "description",
								"dtstart", "dtend", "eventLocation" }, null,
						null, null);
		cursor.moveToFirst();
		// fetching calendars name
		String CNames[] = new String[cursor.getCount()];

		// fetching calendars id
		nameOfEvent.clear();
		startDates.clear();
		endDates.clear();
		descriptions.clear();
		for (int i = 0; i < CNames.length; i++) {

			nameOfEvent.add(cursor.getString(1));
			startDates.add(getDate(Long.parseLong(cursor.getString(3))));
			endDates.add(getDate(Long.parseLong(cursor.getString(4))));
			descriptions.add(cursor.getString(2));
			CNames[i] = cursor.getString(1);
			cursor.moveToNext();

		}
		return nameOfEvent;
	}*/
	public static ArrayList<String> readCalendarEvent(List<Event> listevnts ) {
		/*Cursor cursor = context.getContentResolver()
				.query(Uri.parse("content://com.android.calendar/events"),
						new String[] { "calendar_id", "title", "description",
								"dtstart", "dtend", "eventLocation" }, null,
						null, null);
		cursor.moveToFirst();

	 // fetching calendars name
		String CNames[] = new String[cursor.getCount()];
*/
		// fetching calendars id
		nameOfEvent.clear();
	startDates.clear();
		endDates.clear();
		descriptions.clear();
		System.out.println("listevnts size********" + listevnts.size());

		for (int i = 0; i < listevnts.size(); i++) {
			Event event = listevnts.get(i);
			System.out.println("before summary");
			nameOfEvent.add(event.getSummary());
			System.out.println("after summ");
			DateTime start = event.getStart().getDateTime();
			System.out.println("value of datetime " + start.getValue());
			startDates.add(getDate(start.getValue()));
			endDates.add(getDate(event.getEnd().getDateTime().getValue()));
			descriptions.add(event.getDescription());
			//CNames[i] = cursor.getString(1);
			//cursor.moveToNext();

		}
		return nameOfEvent;
	}
	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		System.out.println("datetime "+formatter.format(calendar.getTime()));
		return formatter.format(calendar.getTime());
	}
}
