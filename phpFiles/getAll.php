<?php
    $con = mysqli_connect("mysql.hostinger.in", "u221808257_champ", "5019180XYZ", "u221808257_shop");
    
    
    $statement = mysqli_prepare($con, "SELECT * FROM customer");
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $email,$password, $contact ,$username);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["email"] = $email;
        $response["username"] = $username;
        $response["contact"] = $contact;
        $response["password"] = $password;
    }
    
    echo json_encode($response);
?>
