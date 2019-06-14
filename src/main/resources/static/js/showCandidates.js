$(".ajax-btn").click(function (e) {
    e.preventDefault();
    var value = $(this).attr("id");
    var parentDiv = $(this).parents(".bigCard");
    var candidateId = parentDiv.attr('id');
    var postUrl;
    if(value !== undefined){
        postUrl = "candidate/" + candidateId + "/" + value;
    }
    else {
        postUrl = $(this).attr("href");
    }
    console.log(postUrl);
    $.ajax({
        url : postUrl,
        method: "POST",
        success:function (data) {
            console.log("succes");
            console.log(data);
            if(data === 1){
                parentDiv.remove();
            }
        }
        });
});