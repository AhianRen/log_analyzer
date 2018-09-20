$(function(){
	
	
	
	obj={
			 
		search:function(){
			var data = getQueryData();
		
			$('#tb').datagrid('load',data);
		},
		download:function(type){
			var type = type;
			var myform = $("<form></form>");
	    	myform.attr('method','post')
	    	if(type == "excel"){
	    		myform.attr('action',"/download/excel");
	    	}
	    	if(type == "pdf"){
	    		myform.attr('action',"/download/pdf");
	    	}
	    	
	    	var timeStamp_from = $("<input type='hidden' name='timeStamp_from' />");
	    	timeStamp_from.attr('value',$('input[name="timeStamp_from"]').val());
	    	
			var timeStamp_to = $("<input type='hidden' name='timeStamp_to' />");
			timeStamp_to.attr('value',$('input[name="timeStamp_to"]').val());
			
	    	var threadName = $("<input type='hidden' name='threadName' />");
	    	threadName.attr('value',$.trim($('input[name="threadName"]').val()));
			
	    	var priority = $("<input type='hidden' name='priority' />");
	    	priority.attr('value',$.trim($('input[name="priority"]').val()));
			
	    	var className = $("<input type='hidden' name='className' />");
	    	className.attr('value',$.trim($('input[name="className"]').val()));
			
	    	var message = $("<input type='hidden' name='message' />");
	    	message.attr('value',$.trim($('input[name="message"]').val()));
	    	
	    	var fileName = $("<input type='hidden' name='fileName' />");
	    	fileName.attr('value',$.trim($('input[name="fileName"]').val()));
	    	
	    	var relatedType = $("<input type='hidden' name='relatedType' />");
	    	relatedType.attr('value',$('select[name="relatedType"] :selected').val());
			
	    	var queryType = $("<input type='hidden' name='queryType' />");
	    	queryType.attr('value',$('select[name="queryType"] :selected').val());
	    	
	    	myform.append(timeStamp_from);
	    	myform.append(timeStamp_to);
	    	myform.append(threadName);
	    	myform.append(priority);
	    	myform.append(className);
	    	myform.append(message);
	    	myform.append(fileName);
	    	myform.append(relatedType);
	    	myform.append(queryType);
	    	
			//var options = $('#tb').datagrid('getPager').data("pagination").options;
			//var page = options.pageNumber;//当前页数  
			//var total = options.total; //总记录数
			//var rows = options.pageSize;//每页的记录数（行数）
			var total;
			var data = getQueryData();
			$.post('/queryCount',data,function(result){
				total = result;
				if(total>5000){
					$.messager.prompt('数据量过大','查询结果有'+total+'条,大于50000条，请输入要导出的条数',function(count){
				    	var outCount = $("<input type='hidden' name='outCount' />");
				    	if(count!=null){
				    		outCount.attr('value',count);
					    	myform.append(outCount);
					    	myform.appendTo('body').submit();
				    	}
					});
				}else{
					if(total==null||total==0){
						$.messager.alert("提示","没有查询结果");
					}else{
						var outCount = $("<input type='hidden' name='outCount' />");
				    	outCount.attr('value',total);
						myform.append(outCount);
						myform.appendTo('body').submit();
					}
				}
				
			});
		},
	};
	
	
	$('#tb').datagrid({
		pagination:true,
		pageSize:10,
		pageList:[10,20],
		pageNumber:1,
		
		onLoadSuccess:function(result){
			/*alert("xx");*/
		},
		onDblClickCell:function(rowIndex, field, value){
			/*$.messager.alert(field,value);*/
			$.messager.alert({    
				  width:1000, 
				  title: field,  
				  msg:'<div style="height:500px">' + value+'</div>',
			});
		}
	});	
	
	$('#excel').click(function(){
		obj.download("excel");
	});
	$('#pdf').click(function(){
		obj.download("pdf");
	});
	
	$("#statistic").click(function(){
		//var value = $('select[name="queryType"] :selected').val();
		//$.get("/statistic");
		
		window.open("/statistic")
	});
	
	
});

function getQueryData(){
	var data = {
			timeStamp_from:$('input[name="timeStamp_from"]').val(),
			timeStamp_to:$('input[name="timeStamp_to"]').val(),
			threadName:$.trim($('input[name="threadName"]').val()),
			priority:$.trim($('input[name="priority"]').val()),
			className:$.trim($('input[name="className"]').val()),
			message:$.trim($('input[name="message"]').val()),
			fileName:$.trim($('input[name="fileName"]').val()),
			relatedType:$('select[name="relatedType"] :selected').val(),
			queryType:$('select[name="queryType"] :selected').val()
		};
	return data;
}

function dateFormat(value){
	
	
	var date = new Date(value);
     var y = date.getFullYear();
     var MM = date.getMonth() + 1;
     var d = date.getDate();
     var h = date.getHours();
     var m = date.getMinutes();
     var s = date.getSeconds()
     return y+'-'+MM+'-'+d+' '+h+':'+m+':'+s;
     /*return date.Format("yyyy-MM-dd HH:mm:ss");*/
}