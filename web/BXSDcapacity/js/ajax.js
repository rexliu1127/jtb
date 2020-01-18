function Get(url,callback)
{
	$.get(url,function(data){
    	callback(data);
  	});
}

function Post(url,params,callback)
{
	
}
