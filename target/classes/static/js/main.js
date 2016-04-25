var show = false;

$('#suggestions').on('click', function() {
    show = ! show;
    if (! show){
        $("#header")[0].text = "Click for suggestions"
        $("#suggestions").attr("size", 0);
    }
    else {
        $("#header")[0].text = "Click to collapse"
        $("#header")[0].style.fontWeight = 900;
        $("#suggestions").attr('size', 6);
    }
})

$("#line").on('keyup', function() {
    var select = document.getElementById("suggestions");

    var postParameter = {
            	    line: this.value
            }

            	    console.log("-" + this.value + "-");
    $.post("/result", postParameter, function(responseJSON){
        responseObject = JSON.parse(responseJSON);
        answers = responseObject.answers;
        var i;
        for(i=select.options.length-1;i>0;i--)
        {
            select.remove(i);
        }
        for(var i = 0; i < answers.length; i++) {
            var answer = answers[i];
            var el = document.createElement("option");
            el.textContent = answer;
            el.value = answer;
            select.appendChild(el);
        }
    })
})