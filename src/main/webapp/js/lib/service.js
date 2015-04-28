/**
 * Полезные методы объявлены глобальными.
 */
(function() {
    this.defer = function defer() {
        var result = {};
        result.promise = new Promise(function(resolve, reject) {
            result.resolve = resolve;
            result.reject = reject;
        });
        return result;
    }

    this.load = function(path) {
        var deferred = defer();
        var request = new XMLHttpRequest();
        request.open("GET", path);
        request.onreadystatechange = function() {
            if (request.readyState == 4 && request.status == 200) {
                deferred.resolve(request.responseText);
            }
        };
        request.send();
        return deferred.promise;
    }
})();
