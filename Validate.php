<?php

$con = mysqli_connect("localhost", "root", "", "myapplication");

$userID = $_POST["memberID"];

$statement = mysqli_prepare($con, "SELECT memberID FROM clientdb WHERE memberID = ?");

mysqli_stmt_bind_param($statement, "s", $userID);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userId);

$response = array();
$response["success"] = true;

while(mysqli_stmt_fetch($statement)){
	$response["success"] = false;
	$response["memberID"] = $userId;
}

echo json_encode($response);

?>