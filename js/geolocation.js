(function(){

var makeTable = function ()
{
    var icao = document.getElementById('icao');
    var name = document.getElementById('name');
    var table = document.createElement('table');
    table.className = 'summary updating';
    name.parentNode.appendChild(table);
    var tbody = document.createElement('tbody');
    table.appendChild(tbody);
    var tr = document.createElement('tr');
    tbody.appendChild(tr);
    var td = document.createElement('td');
    td.className = 'label';
    td.rowSpan = 2;
    tr.appendChild(td);
    td.appendChild(document.createTextNode('Choose location:'));
    td = document.createElement('td');
    tr.appendChild(td);
    var a = document.createElement('a');
    a.href = icao.value;
    td.appendChild(a);
    a.appendChild(document.createTextNode(name.value));
    tr = document.createElement('tr');
    tbody.appendChild(tr);
    td = document.createElement('td');
    tr.appendChild(td);
    a = document.createElement('a');
    a.href = '/icao/';
    td.appendChild(a);
    a.appendChild(document.createTextNode('other...'));
    return tbody;
 };

if (navigator.geolocation)
{
    setTimeout(
        function ()
        {
            var tbody = makeTable();
            var tr = document.createElement(tr);
            tbody.insertBefore(tr, tbody.firstChild);
            var td = document.createElement('td');
            td.colSpan = 2;
            td.className = 'label';
            tr.appendChild(td);
            td.appendChild(document.createTextNode('Updating location ...'));
        },
        5000
    );
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
else
{
   makeTable();
}

})();
