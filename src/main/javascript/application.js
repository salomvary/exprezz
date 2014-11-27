var Client = JavaImporter(com.exprezz).Client
var Server = JavaImporter(com.exprezz).Server
var Promise = ES6Promise.Promise

var router = {
    handlers: {},
    handle: function(req, res) {
        print(req.method + ' ' + req.url)
        var handler = this.handlers[req.method + req.url]
        if (handler) handler(req, res)
        else this.default(req, res)
    },
    default: function(req, res) {
        res.statusCode = 404
        res.write('Not Found')
        res.end()
    },
    add: function(method, url, handler) {
        this.handlers[method + url] = handler;
    },
    get: function(url, handler) {
        this.add('GET', url, handler)
    }
}

router.get('/berlin', function(req, res) {
    Promise.all([getWeather(), getSunset()])
        .then(function(results) {
            var weather = results[0];
            var sunset = results[1];
            var response = {
                weather: weather,
                sunset: sunset
            }
            res.write(JSON.stringify(response, null, ' '))
            res.end()
        })
        .catch(function(reason) {
            res.write('Failed to fetch: ' + reason)
            res.end()
        })
})

var port = 9101
Server.createServer(router.handle.bind(router)).listen(port)
print('Server running at http://localhost:' + port)

function yql(query){
    return 'http://query.yahooapis.com/v1/public/yql?format=json&q=' + encodeURIComponent(query)
}

function weatherUrl(place) {
    var query = 'select * from weather.forecast where woeid in (select woeid from geo.places(1) where text="' + place + '")'
    return yql(query)
}

function sunsetUrl(place) {
    var query = 'select astronomy.sunset from weather.forecast where woeid in (select woeid from geo.places(1) where text="' + place + '")'
    return yql(query)
}

function get(url) {
    return new Promise(function(resolve, reject) {
        Client.get(url, function(res) {
             print('SUCCESS ' + url)
             try {
                resolve(JSON.parse(res))
             } catch(e) {
                print('Error parsing response ' + e)
             }
        })
    })
}

function getWeather() {
    return get(weatherUrl('Berlin')).then(function(response) {
        return response.query.results.channel.item.condition.text
    })
}

function getSunset() {
    return get(sunsetUrl('Berlin')).then(function(response) {
        return response.query.results.channel.astronomy.sunset
    })
}