<?php
    $con = mysqli_connect("mysql.hostinger.in", "u221808257_champ", "5019180XYZ", "u221808257_shop");
    
    $email = $_POST["email"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM shopkeeper WHERE Email = ? AND Password = ?");
    mysqli_stmt_bind_param($statement, "ss", $email, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $email, $shopname, $contact, $password);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["email"] = $email;
        $response["shopname"] = $shopname;
        $response["contact"] = $contact;
        $response["password"] = $password;
    }
    
    echo json_encode($response);
?>
