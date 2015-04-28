package resume.parser.util;

import org.springframework.stereotype.Component;
import resume.parser.enums.DaysEnum;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для получения значений из строк
 */
@Component
public class StringParser {

    // RegExp для выделения цифр
    private final String digitFormat = "(\\d+)";

    public StringParser() {
        simpleDateTimeFormat.setTimeZone(ParserProperties.TIME_ZONE);
    }

    //
    // ПАРСИНГ ID.
    //

    // RegExp для выделения подстроки "?id=000000"
    private final String idSubstringFormat = "\\?id=.*";

    /**
     * Получение Id пользователя из строки.
     *
     * @param containsId Строка вида "...?id=000000"
     * @return Id пользователя или null
     * @throws ParseException Ошибка парсинга
     */
    public Long parseId(String containsId) throws ParseException {
        Long id = null;

        Pattern pattern = Pattern.compile(idSubstringFormat);
        Matcher matcher = pattern.matcher(containsId);

        // Подстрока с id
        String idSubstring = "";
        if(matcher.find()) {
            idSubstring = matcher.group();
        }

        // Получение id
        pattern = Pattern.compile(digitFormat);
        matcher = pattern.matcher(idSubstring);
        if(matcher.find()) {
            id = Long.parseLong(matcher.group(), 10);
        }

        return id;
    }

    //
    // ПАРСИНГ ДАТЫ.
    //

    // Формат даты 00.00.00 для RegExp
    private final String dateFormat = "\\d\\d\\.\\d\\d\\.\\d\\d";
    // Формат времени 00:00 для RegExp
    private final String timeFormat = "\\d\\d\\:\\d\\d";

    private final DateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy ");
    private final DateFormat simpleDateTimeFormat = new SimpleDateFormat("dd.MM.yy hh:mm");

    /**
     * Получение даты из строки, приведение к Timestamp.
     *
     * @param date Строка в виде "00.00.00 "
     *  @param time Строка в виде "00:00"
     * @return Объект Timestamp или null
     * @throws ParseException Ошибка парсинга
     */
    public Timestamp parseDate(String date, String time) throws ParseException {
        if(date == null || time == null) {
            return null;
        }

        return new Timestamp(simpleDateTimeFormat.parse(date + time).getTime());
    }

    /**
     * Получение подстроки, содержащей дату.
     *
     * @param containsDate Строка содержащая дату
     * @return Дата в виде "00.00.00 " или null
     * @throws ParseException Ошибка парсинга
     */
    public String getDateSubstring(String containsDate) throws ParseException {
        // Получение даты. Возможные значения - "00.00.00", "Сегодня", "Вчера".
        String date = null;
        Pattern pattern = Pattern.compile(dateFormat);
        Matcher matcher = pattern.matcher(containsDate);

        // 00.00.00
        if(matcher.find()) {
            date = matcher.group() + " ";
        } else {
            Calendar calendar = Calendar.getInstance();
            // Вчера
            if(containsDate.contains(DaysEnum.YESTERDAY.getDay())) {
                calendar.add(Calendar.DATE, -1);
                date = simpleDateFormat.format(calendar.getTime());
            }
            // Сегодня
            if(containsDate.contains(DaysEnum.TODAY.getDay())) {
                date = simpleDateFormat.format(calendar.getTime());
            }
        }

        return date;
    }

    /**
     * Получение подстроки, содержащей время.
     *
     * @param containsTime Строка содержащая время
     * @return Время в виде "00:00" или null
     * @throws ParseException Ошибка парсинга
     */
    public String getTimeSubstring(String containsTime) throws ParseException {
        // Получение времени. Возможное значение - "00:00".
        String time = null;
        Pattern pattern = Pattern.compile(timeFormat);
        Matcher matcher = pattern.matcher(containsTime);

        if(matcher.find()) {
            time = matcher.group();
        }

        return time;
    }

    //
    // ОТДЕЛЬНЫЕ СЛУЧАИ ПАРСИНГА.
    //

    /**
     * Получение подстроки идущей после двоеточия.
     *
     * @param containsColon Строка вида "Заголовок: Значение"
     * @return Значение или null
     * @throws ParseException Ошибка парсинга
     */
    public String parseAfterColon(String containsColon) throws ParseException {
        int colonIndex = containsColon.indexOf(":") + 1;
        if(colonIndex == 0) {
            return null;
        }
        String afterColon = containsColon.substring(colonIndex, containsColon.length()).replaceAll("\\s+","");
        return afterColon.equals("") ? null : afterColon;
    }

    /**
     * Получение цифр из строки содержащей цифры.
     *
     * @param containsDigit Строка вида содержащая цифры
     * @return Цифры из строки или null
     * @throws ParseException Ошибка парсинга
     */
    public Integer parseDigit(String containsDigit) throws ParseException {
        Integer digit = null;
        Pattern pattern = Pattern.compile(digitFormat);
        Matcher matcher = pattern.matcher(containsDigit.replaceAll("\\s+",""));
        if(matcher.find()) {
            digit = Integer.parseInt(matcher.group(), 10);
        }
        return digit;
    }
}
