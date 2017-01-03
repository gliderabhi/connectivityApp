<?php
    $con = mysqli_connect("mysql.hostinger.in", "u221808257_champ", "5019180XYZ", "u221808257_shop");
    
    $email = $_POST["mail"];
    $username= $_POST["username"];
    $contact = $_POST["contact"];
    $password= $_POST["password"];

  $statement = mysqli_prepare($con, "INSERT INTO customer (Email, Password, Contact, Username) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssss", $email, $contact,  $password,$username);
   if( mysqli_stmt_execute($statement)){
    
    $response = array();
    $response["success"] = true; 

	}
else {$response = array();
    $response["success"] = false; }
    
    echo json_encode($response);
?>
