// Элемент отображает статус парсера
var parserWork = document.querySelector("#parserWork");
// Поисковый Input
var searchField = document.querySelector("#searchField");
// Элемент для отображения результатов поиска
var searchResultDiv = document.querySelector("#searchResults");

// Команда запуска парсера
var startParser = function() {
    load("/startParser");
    showParserWork(true);
    setTimeout(pollingParser, 1000);
}

// Опрос парсера, получение статуса
var pollingParser = function() {
    load("/isWorking").then(function(isWorking) {
        if(JSON.parse(isWorking)) {
            // Повторный опрос через секунду
            setTimeout(pollingParser, 1000);
        } else {
            showParserWork(false);
        }
    });
}

// Показать или спрятать элемент отображающий статус парсера
var showParserWork = function(isWorking) {
    parserWork.style.visibility = isWorking ? "visible" : "hidden";
}

// Установка пустых полей объекта резюме
var setResumeNullFields = function(resumesJson) {
    resumesJson.salary = resumesJson.salary == null ? "Договорная" : resumesJson.salary;
    resumesJson.description = resumesJson.description == null ? "" : resumesJson.description;
}

// Запрос на поиск
var search = function() {
    var keywords = searchField.value;
    var searchUrl = "/search?keywords=" + keywords;
    if(keywords != "") {
        load(searchUrl).then(function(resumes) {
            var resumesJson = JSON.parse(resumes);

            // Очистить результаты поиска
            searchResultDiv.innerHTML = '';

            // Заполнить searchResultDiv результатами поиска
            for(var i in resumesJson) {
                setResumeNullFields(resumesJson[i]);
                searchResultDiv.appendChild(Template.render("resume", resumesJson[i]));
            }
        });
    } else {
        // Очистить результаты поиска
        searchResultDiv.innerHTML = '';
    }
}

// Загрузка шаблонов
Template.compile("template/template.html");

// При запуске приложения проверяется статус парсера
showParserWork(true);
pollingParser();





