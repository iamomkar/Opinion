<?php

$response = array();


require_once __DIR__ . '/db_config.php';

$conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

if ($conn) {

    $sql = "SELECT * FROM survey_tb ORDER BY start_time DESC";
    
$result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0) {
        
        $response["survey"] = array();
        
    while($data = mysqli_fetch_assoc($result)){
        $survey= array();
        $survey["sid"] = $data["sid"];
        $survey["created_by"] = $data["created_by"];
        $survey["number_of_options"] = $data["number_of_options"];
        $survey["question"] = $data["question"];
        $survey["option_one"] = $data["option_one"];
        $survey["option_two"] = $data["option_two"];
        $survey["option_three"] = $data["option_three"];
        $survey["option_four"] = $data["option_four"];
        $survey["option_one_votes"] = $data["option_one_votes"];
        $survey["option_two_votes"] = $data["option_two_votes"];
        $survey["option_three_votes"] = $data["option_three_votes"];
        $survey["option_four_votes"] = $data["option_four_votes"];
        $survey["start_time"] = $data["start_time"];
        $survey["end_time"] = $data["end_time"];

        array_push($response["survey"], $survey);
    }
        $response["success"] = 1;
        $response["message"] = "Survey's Successfully Found.";
    
        echo json_encode($response);
    
    } else {
        
        $response["success"] = 0;
        $response["message"] = "No Survey Found.";

        echo json_encode($response); 
    
        //echo "Error: " . $sql . "" . mysqli_error($conn);
    
    }
}else{
    $response["success"] = 0;
    $response["message"] = "Connection Failed db";


    echo json_encode($response);     
    
    //die("Connection failed: " . mysqli_connect_error());
}        
    
?>