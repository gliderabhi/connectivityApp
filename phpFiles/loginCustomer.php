<?php
    $con = mysqli_connect("mysql.hostinger.in", "u221808257_champ", "5019180XYZ", "u221808257_shop");
    
    $email = $_POST["email"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM customer WHERE Email = ? AND Password = ?");
    mysqli_stmt_bind_param($statement, "ss", $email, $password);
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
