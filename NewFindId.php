<?php

$con = mysqli_connect("localhost", "root","","myapplication");
mysqli_query($con, 'SET NAMES utf8');

$userName = $_POST["memberName"];
$userEmail = $_POST["email"];

$statement = mysqli_prepare($con, "SELECT memberID FROM clientdb WHERE memberName = ? AND email = ?");
mysqli_stmt_bind_param($statement, "ss", $userName, $userEmail);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userId);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement)){
	$response["success"] = true;
	$response["memberID"] = $userId;
}

echo json_encode($response);

?>