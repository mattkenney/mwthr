(function(){

var overall = document.getElementById('overall');
document.cookie = 'lrg=' + ((overall && overall.offsetWidth > 300) ? '1' : '0');

if (navigator.geolocation)
{
    navigator.geolocation.getCurrentPosition(
        function (pos)
        {
            document.cookie = 'lat=' + pos.coords.latitude;
            document.cookie = 'lon=' + pos.coords.longitude;
            document.cookie = 'utc=' + new Date().getTime();
            location = 'now';
        },
        null,
        {
            enableHighAccuracy:false,
            maximumAge:1800000,
            timeout:6000
        }
    );
}

})();
