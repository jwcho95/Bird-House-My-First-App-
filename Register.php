<?php

$con = mysqli_connect("localhost", "root", "", "myapplication");
mysqli_query($con, 'SET NAMES utf8');

$userName = $_POST["memberName"];
$userID = $_POST["memberID"];
$userPw = $_POST["password"];
$userEmail = $_POST["email"];

$statement_email = mysqli_prepare($con, "SELECT email FROM clientdb WHERE email = ?");
mysqli_stmt_bind_param($statement_email, "s", $userEmail);
mysqli_stmt_execute($statement_email);
mysqli_stmt_store_result($statement_email);
mysqli_stmt_bind_result($statement_email, $email);

$response = array();
$response["success_email"] = true;
$response["success"] = true;

if(mysqli_stmt_fetch($statement_email)){
	$response["success_email"] = false;
}else{
	$statement = mysqli_prepare($con, "INSERT INTO clientdb VALUES(?,?,?,?)");
	mysqli_stmt_bind_param($statement, "ssss", $userName, $userID, $userPw, $userEmail);
	mysqli_stmt_execute($statement);
}

echo json_encode($response);

?>