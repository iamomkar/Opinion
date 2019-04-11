<?php
if (isset($_GET['sid']) && isset($_GET['vote'])) {


    $sid = $_GET["sid"];
    $vote = $_GET["vote"];

    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {

        if($vote == 1){
            $sql = "UPDATE `survey_tb` SET `option_one_votes`= `option_one_votes` + 1 WHERE `sid` = '".intval($sid)."'";
        }else if($vote == 2){
            $sql = "UPDATE `survey_tb` SET `option_two_votes`= `option_two_votes` + 1 WHERE `sid` = '".intval($sid)."'";
        }else if($vote == 3){
            $sql = "UPDATE `survey_tb` SET `option_three_votes`= `option_three_votes` + 1 WHERE `sid` = '".intval($sid)."'";
        }else if($vote == 4){
            $sql = "UPDATE `survey_tb` SET `option_four_votes`= `option_four_votes` + 1 WHERE `sid` = '".intval($sid)."'";
        }
        if (mysqli_query($conn, $sql)) {

            $response["success"] = 1;
            $response["message"] = "Vote Successfully Recorded.";

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
