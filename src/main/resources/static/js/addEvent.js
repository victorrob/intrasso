$("#hasForm").change(function () {
    $(".formVisible").toggleClass("hidden", !$(this).is(":checked"));
});