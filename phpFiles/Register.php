<?php
    $con = mysqli_connect("mysql.hostinger.in", "u221808257_champ", "5019180XYZ", "u221808257_shop");
    
    $email = $_POST["mail"];
    $Shopname= $_POST["Shopname"];
    $contact = $_POST["contact"];
    $password= $_POST["password"];
    $state = $_POST["state"];
    $city= $_POST["city"];
    $locality=$_POST["locality"];
    $market=$_POST["marketAdd"];

  $statement = mysqli_prepare($con, "INSERT INTO shopkeeper (Email, ShopName, Contact, Password, State, City, Locality, Market_address) VALUES (?, ?, ?, ?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssssssss", $email,$Shopname, $contact,  $password, $state, $city, $locality , $market);
   if( mysqli_stmt_execute($statement)){
    
    $response = array();
    $response["success"] = true; 
	
	$statement = mysqli_prepare($con, "INSERT INTO twoWheeler (Email) VALUES (?)");
    mysqli_stmt_bind_param($statement, "ssssssss", $email);
    
	}
else {$response = array();
    $response["success"] = false; }
    
    echo json_encode($response);
?>
