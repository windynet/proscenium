/**
 * Created by ASUS on 2017/12/7.
 */
var host = 'http://192.168.137.108:8085/';
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}

function showwarnBox(msg){
    $('#wB_msg').text(msg);
    $('.warnBox').show();
    setTimeout(function () {
       $('.warnBox').css('opacity',1)
        $('.wB_content').css('marginTop','10%')
    },1)
}

function closewarnBox() {
    $('.warnBox').css('opacity',0)
    $('.wB_content').css('marginTop','5%')

    setTimeout(function () {
        $('.warnBox').hide();
    },500)
}

