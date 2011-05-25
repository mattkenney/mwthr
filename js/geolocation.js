(function(){

if (navigator.geolocation)
{
    navigator.geolocation.getCurrentPosition(
        function (pos)
        {
            document.forms.location.elements.lat.value = pos.coords.latitude;
            document.forms.location.elements.lon.value = pos.coords.longitude;
            document.forms.location.submit();
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
