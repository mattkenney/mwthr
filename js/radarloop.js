(function(){

var hidden = document.getElementById('jsloop'),
    images = [],
    loaded = [],
    step = 0,
    current = null;

var changeImage = function ()
{
    for (var i = 0, n = step++; i < 8; i++)
    {
        if (loaded[n])
        {
            if (current)
            {
                current.parentNode.removeChild(current);
            }
            current = images[n];
            hidden.parentNode.insertBefore(current, hidden);
            break;
        }
        n = (n + 1) % 8;
    }
};

var makeImage = function (n)
{
    var img = new Image();
    img.onerror = function ()
    {
        if (n < 7)
        {
            makeImage(n + 1);
        }
    };
    img.onload = function ()
    {
        loaded[n] = true;
        if (!current)
        {
            changeImage();
            setInterval(changeImage, 500);
        }
        img.onerror();
    };
    img.src = hidden.value + (7 - n) + '.png';
    img.height = 275;
    img.width = 300;
    img.style.marginLeft = '-1px';
    img.style.marginTop = '-1px';
    images[n] = img;
};

if (hidden)
{
    makeImage(0);
}

})();
