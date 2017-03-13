var TT = TAOTAO = {
	checkLogin : function(){
		var _token = $.cookie("WEB_TOKEN");
		if(!_token){
			return ;
		}
		$.ajax({
			url : "http://ssoquery.mallmall.com/user/" + _token,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var html =_data.username+"，欢迎来到MallMall！<a href=\"http://www.mallmall.com/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});