<?php

$con = mysqli_connect("localhost", "root","","myapplication");
mysqli_query($con, 'SET NAMES utf8');

$userName = $_POST["memberName"];
$userID = $_POST["memberID"];
$userEmail = $_POST["email"];

$statement = mysqli_prepare($con, "SELECT email, password FROM clientdb WHERE memberName = ? AND memberID = ?");
mysqli_stmt_bind_param($statement, "ss", $userName, $userID);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userEmail, $userPw);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement)){
	$response["success"] = true;
	$response["email"] = $userEmail;
	$response["userPw"] = $userPw;
}

echo json_encode($response);

?>