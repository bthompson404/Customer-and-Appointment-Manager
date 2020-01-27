
package utilities;

import implement.DatabaseReadWrite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Blake Thompson
 */
public class DateTimeLocale {
    // global locale variable
    public static Locale myLocale = Locale.getDefault();
    
    // determine if an appointment falls within business hours, 8-5 M-F
    public static boolean checkBusinessHours(String apptStart, String apptEnd) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime openTime = LocalTime.NOON.minusHours(4);
        LocalTime closeTime = LocalTime.NOON.plusHours(5);

        if (apptStart.length() < 8)
            apptStart = "0" + apptStart;
        
        LocalTime checkStart = LocalTime.parse(dtf.parse(apptStart).toString().substring(19));
        LocalTime checkEnd = LocalTime.parse(dtf.parse(apptEnd.substring(13)).toString().substring(19));
        return (checkStart.isAfter(openTime) && checkEnd.isBefore(closeTime));
    }
    
    public static String getCurrentUTC() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }
    
    public static String utcToLocal(String utcTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Instant instant = sdf.parse(utcTime).toInstant();
        TimeZone myTimeZone = TimeZone.getDefault();
        ZonedDateTime localTime = instant.atZone(ZoneId.of(myTimeZone.getID()));
        return DateTimeFormatter.ofPattern("MM/dd/yyyy - hh:mm a").format(localTime);
    }
    
    public static String localToUtc(String localTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        sdf.setTimeZone(TimeZone.getDefault());
        Instant instant = sdf.parse(localTime).toInstant();
        TimeZone utcTimeZone = TimeZone.getTimeZone("GMT");
        ZonedDateTime utcTime = instant.atZone(ZoneId.of(utcTimeZone.getID()));
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(utcTime);
    }
    
    // determines the minute difference between two times
    public static String appointmentLength(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Instant instStart = sdf.parse(start).toInstant();
        Instant instEnd = sdf.parse(end).toInstant();
        Duration apptLength = Duration.between(instStart, instEnd);
        return apptLength.toMinutes() + " minutes";
    }
    
    // takes a start time and minute length and gives an end time
    public static String calculateAppointmentEnd(String start, int length) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        LocalDateTime ldt = LocalDateTime.parse(start, dtf);
        ldt = ldt.plusMinutes(length);
        
        return ldt.toString();
    }
    
    //checks how many minutes between current time and another time
    public static long compareTime(String start) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentUTC = sdf.parse(getCurrentUTC());
        Date apptStart = sdf.parse(start);
        
        long timeDifference = TimeUnit.MILLISECONDS.toMinutes(apptStart.getTime() - currentUTC.getTime());

        return timeDifference;
    }
    
    // returns the date of the first day of the week based on current locale
    public static Date getWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTime();
    }
    
    // same for last date
    public static Date getWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTime();
    }
    
    // converts a date and time string to a local date
    public static LocalDate getDateFromDateTime(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse(dateTime);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    // checks for appointment overlap
    public static boolean isOverlapping(String appointmentId, String userId, String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // get assigned user schedule
            ResultSet schedule = DatabaseReadWrite.getUserSchedule(userId);

            // compare selected time to schedule
            schedule.beforeFirst();
            while (schedule.next()) {
                Date apptStart = sdf.parse(start);
                Date apptEnd = sdf.parse(end.replace("T", " ") + ":00");
                Date scheduleStart = sdf.parse(schedule.getString("start"));
                Date scheduleEnd = sdf.parse(schedule.getString("end"));
                
                // appointment can't overlap itself, return false
                if (appointmentId.equals(schedule.getString("appointmentId"))) {
                    return false;
                }
                else if (apptStart.before(scheduleEnd) && scheduleStart.before(apptEnd)) {
                    return true;
                }
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}