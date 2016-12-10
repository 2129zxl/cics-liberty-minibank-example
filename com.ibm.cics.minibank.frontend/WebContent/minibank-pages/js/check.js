function checkaccount(account) {
	var accountLength=account.length;
		if(account==""){
		return "\"Account\" is empty, ";
	}
		
		else if(accountLength!=10){
			return "\"Account\" input illegal, ";
	}
		else{
			for(var i=0;i<10;i++){
				var ch=account.charAt(i);
				if(ch>=0&&ch<=9){
				index=-1;
				}else{
					index=1;
					return "\"Account\" input illeagal, ";
				}
			}
			return "";
		}
}

function checkbalance(amount) {
	if(amount==""){
		return "\"Balance\" is empty, ";
	}
	
	if(!isNaN(amount)){    
		return "";    
		}    
	else{
		return "\"Balance\" input illegal,";
	}
}

function checkamount(amount) {
	if(amount==""){
		return "\"Money Amount\" is empty, ";
	}
	
	if(!isNaN(amount)){    
		return "";    
		}    
	else{
		return "\"Money Amount\" input illegal,";
	}
}

function checkuserid(userId){
	var userIdLength=userId.length;
		if(userId==""){
		return "\"UserID\" is empty, ";
	}
		else{
			for(var i=0;i<userIdLength;i++){
				var ch=userId.charAt(i);
				if(ch>=0&&ch<=9){
					index=-1;
				}else{
					return "\"UserID\" input illeagal, ";
				}
			}
			return "";
		}
}


function checkusername(userName){
	if(userName==""){
		return "\"UserName\" is empty, ";
	}
	else{
		return "";
		}
}

function checksourceaccount(account){
		var accountLength=account.length;
		if(account==""){
		return "\"SourceAccount\" is empty, ";
	}
		
		else if(accountLength!=10){
			return "\"SourceAccount\" input illegal, ";
	}
		else{
			for(var i=0;i<10;i++){
				var ch=account.charAt(i);
				if(ch>=0&&ch<=9){
				index=-1;
				}else{
					index=1;
					return "\"SourceAccount\" input illeagal, ";
				}
			}
			return "";
		}
}

function checktargetaccount(account){
		var accountLength=account.length;
		if(account==""){
		return "\"TargetAccount\" is empty, ";
	}
		
		else if(accountLength!=10){
			return "\"TargetAccount\" input illegal, ";
	}
		else{
			for(var i=0;i<10;i++){
				var ch=account.charAt(i);
				if(ch>=0&&ch<=9){
				index=-1;
				}else{
					index=1;
					return "\"TargetAccount\" input illeagal, ";
				}
			}
			return "";
		}
}

function checkage(age){
	var re = /^[0-9]*[1-9][0-9]*$/;  

	if(age==""){
		return "\"Age\" is empty, ";
	}
    if (re.test(age)){
    	return "";
    } 
	else{
		return "\"Age\" input illegal,";
	}
}

function checkaddress(address){
	if(address==""){
		return "\"Address\" is empty, ";
	}
	else{
		return "";
	}
}