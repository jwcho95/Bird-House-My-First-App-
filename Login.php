<?php

$con = mysqli_connect("localhost", "root","","myapplication");

$userId = $_POST["memberID"];
$userPassword = $_POST["password"];

$statement = mysqli_prepare($con, "SELECT memberName,memberID FROM clientdb WHERE memberID = ? AND password = ?");
mysqli_stmt_bind_param($statement, "ss", $userId, $userPassword);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userName ,$userID);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement)){
	$response["success"] = true;
	$response["memberName"] = $userName;
	$response["memberID"] = $userId;
}

echo json_encode($response);

?>	

