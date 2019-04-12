<?php
if (isset($_GET['created_by']) && isset($_GET['number_of_options'])) {


    $created_by = $_GET["created_by"];
    $number_of_options = $_GET["number_of_options"];
    $question = $_GET["question"];
    $option_one = $_GET["option_one"];
    $option_two = $_GET["option_two"];
    $option_three = $_GET["option_three"];
    $option_four = $_GET["option_four"];
    $start_time = $_GET["start_time"];
    $end_time = $_GET["end_time"];

    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {
        $sql = "INSERT INTO survey_tb(created_by,number_of_options,question,option_one,option_two,option_three,option_four,start_time,end_time)
                    VALUES ('$created_by','$number_of_options','$question','$option_one','$option_two','$option_three','$option_four','$start_time','$end_time')";

        if (mysqli_query($conn, $sql)) {

            $response["success"] = 1;
            $response["message"] = "Survey Successfully Created.";

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
