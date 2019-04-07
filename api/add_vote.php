<?php

$response = array();

if (isset($_GET['uid']) && isset($_GET['cid']) && isset($_GET['pid'])) {


    $pid = $_GET["pid"];
    $uid = $_GET["uid"];
    $cid = $_GET["cid"];
    $gender = $_GET["gender"];
    $occupation = $_GET["occupation"];
    $state = $_GET["state"];
    $city = $_GET["city"];
    $pincode = $_GET["pincode"];
    $age = $_GET["age"];
    $latlong = $_GET["latlong"];

    require_once __DIR__ . '/db_config_votes.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {

        $sql = "SELECT * from poll" . $pid . "_votes_tb WHERE user_id = '$uid'";

        $result = mysqli_query($conn, $sql);

        if (!mysqli_num_rows($result) > 0) {
            
            $sql2 = "INSERT INTO poll" . $pid . "_votes_tb(user_id,candidate_id,gender,occupation,state,city,pincode,age,latlong)
            VALUES ('$uid','$cid','$gender','$occupation','$state','$city','$pincode','$age','$latlong')";


            if (mysqli_query($conn, $sql2)) {

                $response["success"] = 1;
                $response["message"] = "Vote Successfully Registered.";

                echo json_encode($response);
            } else {

                $response["success"] = 0;
                $response["message"] = "Insertion Fail query" . mysqli_error($conn);

                echo json_encode($response);

                //echo "Error: " . $sql . "" . mysqli_error($conn);
            }
        } else {
            $response["success"] = 0;
            $response["message"] = "You have already voted in this poll.";

            echo json_encode($response);
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "Connection Failed db";


        echo json_encode($response);

        die("Connection failed: " . mysqli_connect_error());
    }
} else {

    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";


    echo json_encode($response);
}
