/**
 *
 *  @author Kowalski Artur S28186
 *
 */

package zad1;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {
    public static String passed(String from, String to){

        try {
            if (from.contains("T")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM u (EEEE) 'godz. 'H':'mm", new Locale("pl","PL"));
                LocalDateTime start = LocalDateTime.parse(from);
                LocalDateTime end = LocalDateTime.parse(to);
                ZonedDateTime zdt = start.atZone(ZoneId.of("Europe/Warsaw"));
                ZonedDateTime zdt2 = end.atZone(ZoneId.of("Europe/Warsaw"));
                StringBuilder ans = new StringBuilder("Od "+zdt.format(formatter)+" do "+zdt2.format(formatter));

                Duration duration = Duration.between(zdt, zdt2);
                boolean check = calculateDaysAndWeeks(zdt.toLocalDate(),zdt2.toLocalDate(),ans);

                long durationHours = duration.getSeconds()/3600;
                long durationMinutes = duration.getSeconds()/60;
                ans.append("\n - godzin: ").append(durationHours).append(", minut: ").append(durationMinutes).toString();
                if(check)
                    calendarFormat(zdt.toLocalDate(),zdt2.toLocalDate(),ans);

                return ans.toString();
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM u (EEEE)", new Locale("pl","PL"));
                LocalDate start = LocalDate.parse(from);
                LocalDate end = LocalDate.parse(to);
                StringBuilder ans = new StringBuilder("Od "+start.format(formatter)+" do "+end.format(formatter));
                if(calculateDaysAndWeeks(start,end,ans))
                    calendarFormat(start,end,ans);
                return ans.toString();
            }
        }catch (Exception e){
            return "*** "+e;
        }
    }
    private static void calendarFormat(LocalDate start, LocalDate end, StringBuilder ans){
        Period period = Period.between(start,end);
        ans.append("\n - kalendarzowo: ");
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();
        if(years > 0) ans.append(years).append(yearsFormat(years));
        if(years > 0 && months > 0) ans.append(", ");
        if(months > 0) ans.append(months).append(monthsFormat(months));
        if(months > 0 && days > 0) ans.append(", ");
        if(days > 0) ans.append(days).append(daysFormat(days));
    }
    private static boolean calculateDaysAndWeeks(LocalDate start, LocalDate end,StringBuilder sb){
        long periodDays = ChronoUnit.DAYS.between(start, end);
        String periodWeek;
        if(periodDays / 7.0 == Math.floor(periodDays / 7.0)) {
            periodWeek = String.format(Locale.US,"%d", periodDays / 7);
        }else{
            periodWeek = String.format(Locale.US,"%.2f", periodDays / 7.0);
        }
        sb.append("\n - mija: ").append(periodDays).append(daysFormat((int)periodDays)+", tygodni ").append(periodWeek);

        return periodDays > 0?true:false;
    }
    private static String daysFormat(int days){
        return days==1?" dzień":" dni";
    }
    private static String monthsFormat(int months){
        if(months==1)return " miesiąc";
        if((months % 10 >= 2 && months % 10 <= 4) && !(months % 100 >= 12 && months % 100 <= 14)) return " miesiące";
        return " miesięcy";
    }
    private static String yearsFormat(int years){
        if(years==1)return " rok";
        if((years % 10 >= 2 && years % 10 <= 4) && !(years % 100 >= 12 && years % 100 <= 14)) return " lata";
        return " lat";
    }

}
