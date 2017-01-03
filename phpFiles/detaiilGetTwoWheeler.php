<?php
    $con = mysqli_connect("mysql.hostinger.in", "u221808257_champ", "5019180XYZ", "u221808257_shop");
    
    $email = $_POST["email"];
   
    
    $statement = mysqli_prepare($con, "SELECT * FROM twoWheeler WHERE Email = ? ");
    mysqli_stmt_bind_param($statement, "s", $email, );
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $email,$m0,$m1,$m2,$m3,$m4,$m5,$m6,$m7,$m8,$m9);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["email"] = $email;
		$response["m0"]=$m0;
        $response["m1"]=$m1;
        $response["m2"]=$m2;
        $response["m3"]=$m3;
        $response["m4"]=$m4;  		
	    $response["m5"]=$m5;
		$response["m6"]=$m6;
		$response["m7"]=$m7;
		$response["m8"]=$m8;
	}
    
    echo json_encode($response);
?>
