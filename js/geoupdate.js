(function(){

setTimeout(
    function ()
    {
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
                function (pos)
                {
                    location = location;
                },
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
    600000
);

})();
