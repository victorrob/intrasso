var fieldsChoice = $("#fieldsChoice-0");
var clonedDiv = fieldsChoice.clone();
var allChoices = $("#allChoices");
var currentId = 1;
$(document).ready(function () {
    formMap.replace(/&quot/, '"');
    formMap = JSON.parse(formMap);
    for(var key in formMap){
        if(formMap.hasOwnProperty(key)){
            var currentId = key.split("-")[1];
            var currentDiv;
            if(currentId === "0"){
                currentDiv = fieldsChoice;
            }
            else {
                currentDiv = clonedDiv.clone();
            }
            currentDiv.attr("id", "fieldsChoice-" + currentId);
            currentDiv.addClass("order-" + currentId);
            var select = currentDiv.find("select");
            var labels = currentDiv.find("label");
            var input = currentDiv.find("input");
            labels.each(function(){
                $(this).attr("for", $(this).attr("for").split("-")[0] + "-" + currentId);
            });
            input.attr("id", "inputName-" + currentId);
            input.attr("name", "inputName-" + currentId);
            input.val(formMap[key]["inputValue"]);
            select.attr("id", "selectName-" + currentId);
            select.attr("name", "selectName-" + currentId);
            select.find("option").each(function () {
                var option = $(this);
                if(option.val() === formMap[key]["optionValue"]){
                    option.attr("selected", "selected");
                }
            });
            if(currentId !== 0){
                currentDiv.appendTo(allChoices);
            }
        }
    }
});
$("#addFormSelection").click(function () {
    var newDiv = fieldsChoice.clone();
    newDiv.attr("id", "fieldsChoice-" + currentId);
    var select = newDiv.find("select");
    var labels = newDiv.find("label");
    var input = newDiv.find("input");
    labels.each(function(){
       $(this).attr("for", $(this).attr("for").split("-")[0] + "-" + currentId);
    });
    input.attr("id", "inputName-" + currentId);
    input.attr("name", "inputName-" + currentId);
    select.attr("id", "selectName-" + currentId);
    select.attr("name", "selectName-" + currentId);
    currentId += 1;
    newDiv.appendTo(allChoices);
});