(function(){
    var src = document.getElementById('jsloop');
    var images = [];

    img.alt = document.title;
    img.onload = function ()
    {
        setTimeout(
            function ()
            {
                if ((/^(.*)_((([0-7])\.png)|(loop\.gif))$/).test(img.src))
                {
                    img.src = RegExp.$1 + '_' + (((0|RegExp.$4)+1)%8) + '.png';
                }
            },
            500
        );
    };
    img.src = src.value;
    src.parentNode.insertBefore(img, src);
})();
