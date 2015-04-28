package resume.parser.util;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import resume.parser.annotation.ValidateNotNull;
import resume.parser.exception.StringParseException;

import java.sql.Timestamp;
import java.text.ParseException;

/**
 * Получение значений для полей модельного класса из html документа.
 */
@Component
public class DocumentParser {
    @Autowired
    private StringParser stringParser;

    // Css селектор корневого элемента, который содержит все резюме
    public final String rootSelector = "ul.ra-elements-list-hidden li";

    //
    // ID
    //

    private final String idSelector = "a";

    /**
     * Получение Id пользователя из html элемента резюме.
     *
     * @param resumeElement Элемент содержащий резюме
     * @return Id
     * @throws StringParseException В случае если значение null
     * @throws ParseException Ошибка парсинга
     */
    @ValidateNotNull(validateFieldName ="id пользователя")
    public Long parseId(Element resumeElement) throws StringParseException, ParseException {
        Long id = stringParser.parseId(
                parseHref(resumeElement, idSelector));
        return id;
    }

    //
    // TITLE
    //

    private final String titleSelector = "h3";

    /**
     * Получение заголовка из html элемента резюме.
     *
     * @param resumeElement Элемент содержащий резюме
     * @return Заголовок
     * @throws StringParseException В случае если значение null
     * @throws ParseException Ошибка парсинга
     */
    @ValidateNotNull(validateFieldName ="заголовок")
    public String parseTitle(Element resumeElement) throws StringParseException, ParseException {
        String title = parseText(resumeElement, titleSelector);
        return title;
    }

    //
    // UPDATE DATE
    //

    private final String changedSelector = "p:eq(1)";

    /**
     * Получение даты обновления резюме из html элемента резюме
     *
     * @param resumeElement Элемент содержащий резюме
     * @return Дата обновления резюме
     * @throws StringParseException В случае если значение null
     * @throws ParseException Ошибка парсинга
     */
    @ValidateNotNull(validateFieldName ="дату обновления резюме")
    public Timestamp parseUpdateDate(Element resumeElement) throws StringParseException, ParseException {
        String containsDateTime = parseText(resumeElement, changedSelector);
        String date = stringParser.getDateSubstring(containsDateTime);
        String time = stringParser.getTimeSubstring(containsDateTime);

        return stringParser.parseDate(date, time);
    }

    //
    // CITY
    //

    private final String citySelector = "p:eq(2)";

    /**
     * Получение города из html элемента резюме.
     *
     * @param resumeElement Элемент содержащий резюме
     * @return Город
     * @throws StringParseException В случае если значение null
     * @throws ParseException Ошибка парсинга
     */
    @ValidateNotNull(validateFieldName ="город")
    public String parseCity(Element resumeElement) throws StringParseException, ParseException {
        String city = stringParser.parseAfterColon(
                parseText(resumeElement, citySelector));
        return city;
    }

    //
    // DESCRIPTION
    //

    private final String descriptionSelector = "p:gt(2):not(p:last-child)";

    /**
     * Получение описания из html элемента резюме.
     *
     * @param resumeElement Элемент содержащий резюме
     * @return Описание или null
     * @throws ParseException Ошибка парсинга
     */
    public String parseDescription(Element resumeElement) throws ParseException {
        String description = parseText(resumeElement, descriptionSelector);
        return description.equals("") ? null : description;
    }

    //
    // SALARY
    //

    private final String salarySelector = "p:last-child";

    /**
     * Получение зарплаты из html элемента резюме.
     *
     * @param resumeElement Элемент содержащий резюме
     * @return Зарплата или null
     * @throws ParseException Ошибка парсинга
     */
    public Integer parseSalary(Element resumeElement) throws ParseException {
        return stringParser.parseDigit(
                parseText(resumeElement, salarySelector));
    }

    //
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ.
    //

    /**
     * Поиск текста тэга из Element по селектроу.
     *
     * @param resumeItem Element содержащий тэг
     * @param cssSelector Селектор для поиска тэга
     * @return Текста найденного тэга
     */
    public String parseText(Element resumeItem, String cssSelector) {
        return resumeItem.select(cssSelector).text();
    }

    /**
     * Поиск аттрибута href тэга из Element по селектроу.
     *
     * @param resumeItem Element содержащий тэг
     * @param cssSelector Селектор для поиска тэга
     * @return Аттрибут href найденного тэга
     */
    public String parseHref(Element resumeItem, String cssSelector) {
        return resumeItem.select(cssSelector).attr("href");
    }
}
