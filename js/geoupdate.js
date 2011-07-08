(function(){

if (window.addEventListener)
{
    addEventListener(
        'load',
        function ()
        {
            setTimeout(
                function ()
                {
                    scrollTo(0, 1);
                },
                1
            );
        },
        false
    );
}

if (navigator.geolocation)
{
    var start = new Date().getTime();

    setInterval(
        function ()
        {
            var now = new Date().getTime();
            if (now - start > 600000)
            {
                navigator.geolocation.getCurrentPosition(
                    function (pos)
                    {
                        document.cookie = 'lat=' + pos.coords.latitude;
                        document.cookie = 'lon=' + pos.coords.longitude;
                        document.cookie = 'utc=' + new Date().getTime();
                        location = location;
                    },
                    null,
                    {
                        enableHighAccuracy:false,
                        maximumAge:1800000,
                        timeout:6000
                    }
                );
            }
        },
        10000
    );
}

})();
