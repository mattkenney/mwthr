(function(){

if ((/utc=([0-9]+)/).test(document.cookie) && 0|RegExp.$1 > (new Date().getTime() - 300000))
{
    var delay = 60000;
}
else
{
    delay = 1;
}

setTimeout(
    function ()
    {
        var overall = document.getElementById('overall');
        document.cookie = 'lrg=' + ((overall && overall.offsetWidth > 300) ? '1' : '0');
        document.cookie = 'utc=' + new Date().getTime();

        if (navigator.geolocation)
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
        else
        {
            location = location;
        }
    },
    delay
);

if (window.addEventListener && (/\biPhone\b/i).test(navigator.userAgent))
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

})();
