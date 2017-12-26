/**
 * Created by ASUS on 2017/12/6.
 */
//鼠标移动切换背景颜色
$('.h_text').mouseover(function () {
    let a = $(this).index();
    $('.hu_active:eq('+ a +')').css('opacity', '1')
});
$('.hu_active').mouseover(function () {
    $(this).css('opacity', '1')
});
$('.hu_active').mouseout(function () {
    let a = $(this).index();
    $(this).css('opacity', '0')
});
//鼠标点击添加相应事件
//团体入住
$('.r_gorup').click(function () {
   console.log('团体入住');
    window.location.href="idCard.html";
});
//续住
$('.s_over').click(function () {
    console.log('续住')
    window.location.href='xufei-chaka.html?gtype=xuzhu';
});
//退房
$('.c_out').click(function () {
    console.log('退房')
    window.location.href='xufei-chaka.html?gtype=tuifang';
});
//预定
$('.reserve').click(function () {
    console.log('预定')
    window.location.href='import-yuding.html';
});
//散客入住
$('.r_sanke').click(function () {
    console.log('散客入住')
    window.location.href='select-fangtai.html?gtype=sanke';
});