<?php

$response = array();

if (isset($_GET['pid'])) {

    $pid = $_GET["pid"];

    require_once __DIR__ . '/db_config_votes.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {
        $sql = "CREATE TABLE poll" . $pid . "_votes_tb ( vid INT NOT NULL AUTO_INCREMENT , user_id INT NOT NULL ,
                candidate_id INT NOT NULL , gender VARCHAR(15) NOT NULL ,occupation VARCHAR(20) NOT NULL , 
                state VARCHAR(25) NOT NULL , city VARCHAR(25) NOT NULL , pincode VARCHAR(10) NOT NULL , 
                age VARCHAR(5) NOT NULL , latlong VARCHAR(50) NOT NULL , PRIMARY KEY (vid)) ENGINE = InnoDB";

        if (mysqli_query($conn, $sql)) {

            $response["success"] = 1;
            $response["message"] = "Votes Table Successfully Created.";

            echo json_encode($response);
        } else {

            $response["success"] = 0;
            $response["message"] = "Creation Fail query." . mysqli_error($conn);

            echo json_encode($response);

            //echo "Error: " . $sql . "" . mysqli_error($conn);
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "Connection Failed db";

        echo json_encode($response);

        //die("Connection failed: " . mysqli_connect_error());
    }
} else {

    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";


    echo json_encode($response);
}
