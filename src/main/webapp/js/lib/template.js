/**
 * Создание повторно используемых шаблонов, взятых из единого файла.
 */
var Template = (function() {
    var templates = {};

    var parse = function(htmlString) {
        var parser = new DOMParser();
        return parser.parseFromString(htmlString, "text/html").body;
    }

    return {
        compile : function(sourceFile) {
            var deferred = defer();
            load(sourceFile).then(function(sourceHtml) {
                var getTemplate = function(html) {
                    return new Function("model", "with(model){var html='" + html + "';}return html;");
                };
                var sourceTemplates = parse(sourceHtml.replace(/[\r\t\n]/g, "")).childNodes;
                for(var i = 0; i < sourceTemplates.length; i++) {
                    var name = sourceTemplates[i].id;
                    templates[name] = getTemplate(sourceTemplates[i].outerHTML.replace(/ +/g, " ").replace(/{{/g, "'+").replace(/}}/g, "+'"));
                }
                deferred.resolve();
            });
            return deferred.promise;
        },
        render : function(name, model) {
            return parse(templates[name](model || {})).firstChild;
        }
    };
})();
