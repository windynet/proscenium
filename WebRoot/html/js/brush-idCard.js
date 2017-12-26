/**
 * Created by ASUS on 2017/12/23.
 */
'use strict';
$('.itcc_close').click(function () {
    $('.idCard-tc').hide();
});


//调用摄像头
var video = document.getElementById('video');
var canvas = document.getElementById('canvas');
var ctx = canvas.getContext('2d');
var width = $('.new_id').width();
var height = $('.new_id').height();
canvas.width = width;
canvas.height = height;

function liveVideo() {
    var URL = window.URL || window.webkitURL; // 获取到window.URL对象
    navigator.getUserMedia({
        video: true,
        /*audio: true*/
    }, function(stream) {
        video.src = URL.createObjectURL(stream); // 将获取到的视频流对象转换为地址
        video.play(); // 播放
        
    }, function(error) {
        console.log(error.name || error);
    });
}

function draw() {
    ctx.drawImage(video, 0, 0, width, height);
    var url = canvas.toDataURL('image/png');
    console.log('开始传输数据')
    $.ajax({
	  type:'post',
	  aysnc: true,
	  url: 'http://localhost:8080/proscenium/proscenium/faceScan',
	  data:{
		  base64Pictrue:url
	  },
	  success: function (res) {
	      console.log(res)
	  },
	  error: function (err) {
	
	  }
	})
    $('#video').hide();

}

var gtype = GetQueryString('gtype');

if(gtype == 'yuding'){
	//预定单号：QT20171225092309I8NE
  $.ajax({
	  type:'get',
	  aysnc: true,
	  url: 'http://localhost:8080/proscenium/proscenium/readIDCard?timeOut=3',
	  data:{},
	  success: function (res) {
	      console.log(res)
	      if(res.name !== undefined){
	    	  $('.idCard-tc').show();
	    	    $('#video').show();
	        liveVideo();
	        setTimeout('draw()', 5000);
	        $("#idcard").attr('src','http://localhost:8080/proscenium/resources/idcard.bmp')
	      }
	  },
	  error: function (err) {
	
	  }
	})
}

