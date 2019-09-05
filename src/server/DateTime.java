/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */

package server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * This class is for creating formatted local date time.
 */
public interface DateTime {
	
	default String getDateTime() {
		var dt = LocalDateTime.now();
		return dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
	}
}
