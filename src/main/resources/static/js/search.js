$(function(){
	
	obj={
		search:function(){
			$('#tb').datagrid('load',{
				timeStamp_from:$('input[name="timeStamp_from"]').val(),
				timeStamp_to:$('input[name="timeStamp_to"]').val(),
				threadName:$.trim($('input[name="threadName"]').val()),
				priority:$.trim($('input[name="priority"]').val()),
				className:$.trim($('input[name="className"]').val()),
				message:$.trim($('input[name="message"]').val()),
				fileName:$.trim($('input[name="fileName"]').val()),
				relatedType:$('select[name="relatedType"] :selected').val(),
				queryType:$('select[name="queryType"] :selected').val()
			});
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
//	    	myform.append(relatedType);
	//    	myform.append(queryType);
	    	
	    	myform.appendTo('body').submit();
			
		},
	};
	
	$('#tb').datagrid({
		
		pagination:true,
		pageSize:10,
		pageList:[10,20],
		pageNumber:1,
		
		onLoadSuccess:function(data){
			/*alert("xx");*/
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