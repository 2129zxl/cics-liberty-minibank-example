<%@ page import="com.ibm.cics.minibank.local.webapp.entity.User"
import="com.ibm.cics.minibank.local.webapp.entity.TransHistory"
import="com.ibm.cics.minibank.local.webapp.entity.Account"
import="java.util.*"
language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>

<html>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>IBM - MiniBank - UserQuery </title>

    <!-- Bootstrap Core CSS -->
    <link href="../bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="../bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">
    
     <!-- DataTables CSS -->
    <link href="../bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">

    <!-- DataTables Responsive CSS -->
    <link href="../bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="../dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="../bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<div class="container">
     <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            
            <%@ include file="../include/sidebar.jsp"%>    
      </nav>
      <%@ include file="../include/modal.jsp"%>    
      <div>
        <div class="row">
          <div class="col-md-10 col-md-offset-1">
            <div class="page-header">
              <h1 id="navbar"> Users Query </h1>
            </div>

            <div class="bs-component">
              <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4> Users - Query User </h4>
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-top: 40px">
                            		<div class="col-md-2"></div>
                            		<div class="col-md-8">
	                            		<form role="form" method="post" class="form-horizontal" action="doQueryUser">
	                                        <div class="form-group has-success ">
	                                            <label class="control-label col-md-4" for="inputSuccess"> Input With Customer ID </label>
	                                            <div class="col-md-6">
											      <input type="text" class="form-control" name="customerID"
											      id="customerID" placeholder=" Customer ID ">
											    </div>
	                                        </div>
	                                       
	                                        <button type="submit" class="btn btn-primary center-block" 
	                                        id="btn" style="margin-bottom:40px; margin-top: 30px" onclick="return onCheck()"> Query User </button>
	                                    </form>
                                    </div>
  								    <div class="col-md-2"></div>
                            </div>
                            <!-- /.row (nested) -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    
    <%@ include file="../include/footer.jsp"%>  
    <script language='javascript'>
    	
    	
	    function checkUserId(userId){
	 		var userIdLength=userId.length;
	 		if(userId==""){
				return "\"UserID\" is empty, ";
			}
				else{
					for(var i=0;i<userIdLength;i++){
						var ch=userId.charAt(i);
						if(ch>=0&&ch<=9){
						}else{
							return "\"UserID\" input illeagal, ";
						}
					}
					return "";
				}
		}
    	
    	
    	
		function onCheck(){
		var userID=document.getElementById("customerID").value;

 		var checkMessage=checkUserId(userID);
	 		if(checkMessage==""){
	 			return true;
	 		}
	 		else{
	 			var str="<br />"+checkMessage+" please check it again."+"<br /><br />";
				document.getElementById('message').innerHTML=str;
				document.getElementById('message').style.fontSize="20px"
				$("#modal").modal('show');
				return false;
	 		}
		}

	</script>

    <!-- jQuery -->
    <script src="../bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="../bower_components/metisMenu/dist/metisMenu.min.js"></script>
    
    <!-- DataTables JavaScript -->
    <script src="../bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
    <script src="../bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
    <script src="../bower_components/datatables-responsive/js/dataTables.responsive.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="../dist/js/sb-admin-2.js"></script>
    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
    $(document).ready(function() {
        $('#dataTables-example').DataTable({
                responsive: true
        });
    });
    </script>

</body>

</html>
