function addToCart(id) {
    $.ajax({
        method: 'POST',
        url: '/add-to-cart',
        data: {'id': id},
        success: function () {
            $("#cart-count").text(parseInt($("#cart-count").html()) + 1);
        }
    });
}

function removeFromCart(id) {
    $.ajax({
        method: 'POST',
        url: '/remove-from-cart',
        data: {'id': id},
        success: function () {
            $("#cart-count").text(parseInt($("#cart-count").html()) - 1);
        }
    });
}

function deleteFromCart(id) {
    $.ajax({
        method: 'POST',
        url: '/delete-from-cart',
        data: {'id': id}
    });
}

function plusAmount(id) {
    $("#amount-count").val(parseInt($("#amount-count").val()) + 1);
    addToCart(id);
    $("#total-price").load();
}

function minusAmount(id) {
    $("#amount-count").val(parseInt($("#amount-count").val()) - 1);
    if (parseInt($("#amount-count").val()) <= 0) {
        deleteItem(id);
    }
    removeFromCart(id);
    $("#total-price").load();
}

function deleteItem(id) {
    $("#shopping-cart-"+id.toString()).empty().remove();
    $("#shopping-cart-row-"+id.toString()).empty().remove();
    deleteFromCart(id);
    $("#total-price").load();
}

function showLoginModal() {
    $(".modal-body").empty();

    $(".modal-body").append(loginHmtl());

}

$(function() {
  $( "#login" ).submit(function( event ) {
  event.preventDefault();
  showLoginModal();
  return false;
});
});

function loginHmtl() {
    var html='\
    <form id="loginForm" method="post" action="/login"> \
        <input type="text" name="username" value="" required>\
        <input type="password" name="password" value="" required>\
        <input type="submit" value="LOGIN">\
    </form>';
    return html;

}
