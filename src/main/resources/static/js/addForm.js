var fieldsChoice = $("#fieldsChoice");
var allChoices = $("#allChoices");
var currentId = 1;
$("#addFormSelection").click(function () {
    var newDiv = fieldsChoice.clone();
    var select = newDiv.find("select");
    var labels = newDiv.find("label");
    var input = newDiv.find("input");
    labels.each(function(){
       $(this).attr("for", $(this).attr("for").split("-")[0] + "-" + currentId);
    });
    input.attr("id", "inputName-" + 0);
    input.attr("name", "inputName-" + 0);
    select.attr("id", "selectName-" + currentId);
    select.attr("name", "selectName-" + currentId);
    currentId += 1;
    newDiv.appendTo(allChoices);
});