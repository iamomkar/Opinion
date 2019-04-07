<?php

$response = array();

if (isset($_GET['poll_id']) && isset($_GET['name'])) {


    $poll_id = $_GET["poll_id"];
    $name = $_GET["name"];
    $party_name = $_GET["party_name"];
    $poll_position = $_GET["poll_position"];
    $symbol_photo_url = $_GET["symbol_photo_url"];
    $gender = $_GET["gender"];
    $occupation = $_GET["occupation"];
    $dob = $_GET["dob"];
    $location = $_GET["location"];

    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {
        $sql = "INSERT INTO candidate_tb(poll_id,name,party_name,poll_position,symbol_photo_url,gender,occupation,dob,location)
                    VALUES ('$poll_id','$name','$party_name','$poll_position','$symbol_photo_url','$gender','$occupation','$dob','$location')";

        $sql2 = "UPDATE `poll_tb` SET `number_candidates`= `number_candidates` + 1 WHERE `pid` = '".intval($poll_id)."'";

        if (mysqli_query($conn, $sql)  && mysqli_query($conn,$sql2)) {

            $response["success"] = 1;
            $response["message"] = "Candidate successfully added.";

            echo json_encode($response);
        } else {

            $response["success"] = 0;
            $response["message"] = "Insertion Fail query" . mysqli_error($conn);

            echo json_encode($response);

            //echo "Error: " . $sql . "" . mysqli_error($conn);
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
