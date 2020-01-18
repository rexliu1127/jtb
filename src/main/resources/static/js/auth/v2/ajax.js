
function GET(url,params,callback){
	
	if(params!=undefined && params!=""){
		url+="?"+params
	}
	
	$.get(url,function(result){
    	callback(result);
  	});
}

function POST(url,params,callback){
  	$.ajax({
            type: 'post',
            url:url,
            contentType:'application/json;charset=UTF-8',
            data:params,
            dataType:'json',
            success:function (result) {
				callback(result);
            }
        })    

}



/*
var XR = false;
try{
    XR = new XMLHttpRequest();
}catch(tryminrosoft){
    try{
        XR = new ActiveXObject("Msxml2.XMLHTTP");
    }catch(othermicrosoft){
        try{
            XR = new ActiveXObject("Microsoft.XMLHTTP");
        }catch(failed){
            XR = false;
        }
    }
}

if(!XR)
{
    alert("initializing ajax error");
}

function GET(url,params,callback){

    XR.open("GET",url,true);
    XR.setRequestHeader('Content-Type','application/json');
    XR.onreadystatechange = function() {
        if (XR.readyState == 4 && (XR.status == 200 || XR.status == 304)) {
            callback(JSON.parse(XR.responseText));
        }
    };
    XR.send(null);
}

function POST(url,params,callback){
    alert(XR);
    XR.open("POST",url,true);
    XR.setRequestHeader('Content-Type','application/json');
    XR.onreadystatechange = function() {
        if (XR.readyState == 4 && (XR.status == 200 || XR.status == 304)) {
            callback(JSON.parse(XR.responseText));
        }
    };
    if(params!=null && params!=""){
        XR.send(params);
    }else{
        XR.send(null);
    }
}*/