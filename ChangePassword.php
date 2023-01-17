<?php

$con = mysqli_connect("localhost", "root","","myapplication");

$userID = $_POST["memberID"];
$userPw = $_POST["password"];

$statement = mysqli_prepare($con, "UPDATE clientdb SET password = ? WHERE memberID = ? ");
mysqli_stmt_bind_param($statement, "ss", $userPw, $userID);
mysqli_stmt_execute($statement);

$response = array();
$response["success"] = true;

echo json_encode($response);

?>