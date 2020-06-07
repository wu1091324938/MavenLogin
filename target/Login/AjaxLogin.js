/**
 * 登录界面使用异步登录的ajax脚本
 */
function login(){
	//使用Ajax向服务器发起请求
	//$在此处表示引用jquery的方法
	$.ajax(
			{
			//接口四大要素：url,http请求方法，请求体，请求头
			//url为ajax发送的请求地址
			url:"./Login",
			//http请求方法
			type:"post",
			//请求头：预期服务器返回数据的类型
			//dataType可使用json格式
			//dataType:"text",
			dataType:"json",
			//请求体：将form表单中的参数信息序列化成ajax可以提交的数据
			//通过CSS的ID定位form元素
			data:$("#formData").serialize(),
			
			//function无方法名，result为jquery为本来就有的参数，此处是拿result进行处理
			//接口请求成功直接调用success,否则调用error(自动判断)
			success:function(result){
				//alert("接口调用成功"+"；"+result);
				$("#msg")[0].innerText="接口返回的信息是："+result["msg"];
			},
			error:function(result){
				alert("接口调用失败，请检查ajax的服务器代码"+"；"+result);
			}
			
			}
		)
}