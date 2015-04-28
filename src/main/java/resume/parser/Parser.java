package resume.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resume.application.model.Resume;
import resume.application.repository.ResumeRepository;
import resume.parser.exception.StringParseException;
import resume.parser.util.DocumentParser;
import resume.parser.util.ParserProperties;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Service
public class Parser {
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private DocumentParser documentParser;

    private Logger logger = LoggerFactory.getLogger(Parser.class);

    private boolean working = false;
    private int offset;

    /**
     * Метод возвращает статус парсера
     *
     * @return true - В работе. false - остановлен.
     */
    public boolean isWorking() {
        return working;
    }

    /**
     * Метод запускает парсер. Парсер сканирует до достижения одного из условий
     * 1. Достигнут лимит обозначенный в конфигарионном файле
     * 2. Просканированы все страницы сайта
     * 3. Просканированы все новые сообщения
     */
    public void startParser() {
        List<Resume> resumes = new ArrayList<Resume>();
        initParser();

        // Время обновления самого нового резюме в базе.
        Timestamp maxUpdateDate = getMaxUpdateDate();

        while (working) {
            try {
                Elements resumeElements = getResumeElementsFromUrl(nextPage());

                // Завершить работу если достигнута последняя страница,
                // или достигнут лимит на количество обрабатываемых страниц.
                if (resumeElements.size() == 0 || offset > ParserProperties.OFFSET_LIMIT) {
                    working = false;
                    break;
                }

                for (int resumeIndex = 0; resumeIndex < resumeElements.size(); resumeIndex++) {
                    try {
                        // Элемент резюме.
                        Element resumeElement = resumeElements.get(resumeIndex);

                        // Завершить работу если обработаны все новые резюме.
                        Timestamp resumeUpdateDate = documentParser.parseUpdateDate(resumeElement);
                        if (resumeUpdateDate.getTime() < maxUpdateDate.getTime() && !isAdvertising(resumeIndex)) {
                            working = false;
                            break;
                        }

                        // Добаление резюме в список
                        Resume resume = createResumeFromElement(resumeElement);
                        resumes.add(resume);
                    } catch (ParseException parseException) {
                        logger.warn("Парсер не смог обработать строку", parseException);
                    } catch (StringParseException stringParseException) {
                        logger.warn("Парсер не смог получить значение из строки", stringParseException);
                    }
                }

                // Добавление/обновление всех резюме из списка
                resumeRepository.insertOrUpdate(resumes);
            } catch (IOException ioException) {
                logger.warn("Невозможно загрузить страницу № {}, повторная попытка.", offset / ParserProperties.RESUMES_ON_PAGE);
                // Попробовать распарсить страницу снова
                offset -= ParserProperties.RESUMES_ON_PAGE;
            }
        }
    }

    /**
     * Получение url с увеличенным offset.
     *
     * @return Следующую страницу
     */
    private String nextPage() {
        String nextPage = ParserProperties.PARSE_URL + offset;
        offset += ParserProperties.RESUMES_ON_PAGE;
        return nextPage;
    }

    /**
     * Является ли резюме рекламным.
     *
     * @param resumeIndex Позиция элемента резюме
     * @return true - является, false - не является
     */
    private boolean isAdvertising(int resumeIndex) {
        return offset == 0 && resumeIndex <= ParserProperties.ADVERTISING_ON_FIRST_PAGE;
    }

    /**
     * Установка начальных данных.
     */
    private void initParser() {
        working = true;
        offset = 0;
    }

    /**
     * Возвращает время обновления того резюме, время обновление которого максимально.
     *
     * @return Время обновления последнего просканированного резюме
     */
    private Timestamp getMaxUpdateDate() {
        Timestamp timestamp = resumeRepository.findMaxTimestamp();
        return timestamp == null ? new Timestamp(0) : timestamp;
    }

    /**
     * Парсинг страницы. Полчение из страницы элемента, содержащего все резюме.
     *
     * @param url Адрес страницы
     * @return Корневой элемент со всеми резюме.
     */
    private Elements getResumeElementsFromUrl(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.select(documentParser.rootSelector);
    }

    /**
     * Создание объекта resume.application.model.Resume на основе html элемента резюме
     *
     * @param resumeElement Элемент, содержащий резюме
     * @return Объект resume.application.model.Resume
     * @throws ParseException       Ошибка парсинга
     * @throws StringParseException Если значение хотя бы одного из обязательных полей не найдено
     */
    private Resume createResumeFromElement(Element resumeElement) throws StringParseException, ParseException {
        return new Resume(documentParser.parseId(resumeElement),
                documentParser.parseTitle(resumeElement),
                documentParser.parseUpdateDate(resumeElement),
                documentParser.parseCity(resumeElement),
                documentParser.parseDescription(resumeElement),
                documentParser.parseSalary(resumeElement));
    }

}
