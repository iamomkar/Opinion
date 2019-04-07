<?php

$response = array();

if (isset($_GET['created_by']) && isset($_GET['title'])) {


    $created_by = $_GET["created_by"];
    $title = $_GET["title"];
    $description = $_GET["description"];
    $creation_date = $_GET["creation_date"];
    $end_time = $_GET["end_time"];
    $photo_url = $_GET["photo_url"];
    $number_candidates = $_GET["number_candidates"];
    $is_location_specific = $_GET["is_location_specific"];
    $location = $_GET["location"];

    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {
        $sql = "INSERT INTO poll_tb(created_by,title,description,creation_date,end_time,photo_url,number_candidates,is_location_specific,location)
                    VALUES ('$created_by','$title','$description','$creation_date','$end_time','$photo_url','$number_candidates','$is_location_specific','$location')";

        
        if (mysqli_query($conn, $sql)) {

            $sql2 = "SELECT * FROM poll_tb WHERE creation_date = '$creation_date'";

            $result = mysqli_query($conn, $sql2);

            if (mysqli_num_rows($result) > 0) {

                $data = mysqli_fetch_assoc($result);
    
                    $user["pid"] = $data["pid"];
                    $user["title"] = $data["title"];
                    $user["description"] = $data["description"];
                    $user["creation_date"] = $data["creation_date"];
                    $user["end_time"] = $data["end_time"];
                    $user["photo_url"] = $data["photo_url"];
                    $user["number_candidates"] = $data["number_candidates"];
                    $user["is_location_specific"] = $data["is_location_specific"];
                    $user["location"] = $data["location"];
        
                    $response["success"] = 1;
                    $response["message"] = "Poll Successfully Created.Add Candidates Now.";
                    $response["poll"] = $user;
        
                    echo json_encode($response);                
                
            }else{
                $response["success"] = 0;
                $response["message"] = "Poll Created. Error Getting Poll Data";
    
                echo json_encode($response);
            }            
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
