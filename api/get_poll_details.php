<?php


if (isset($_GET['pid'])) {

    $pid = $_GET["pid"];

    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {

        $sql = "SELECT * FROM poll_tb WHERE pid = '$pid'";

        $result = mysqli_query($conn, $sql);

        if (mysqli_num_rows($result) > 0) {

            $data = mysqli_fetch_assoc($result);

            $poll["pid"] = $data["pid"];
            $poll["created_by"] = $data["created_by"];
            $poll["title"] = $data["title"];
            $poll["description"] = $data["description"];
            $poll["creation_date"] = $data["creation_date"];
            $poll["end_time"] = $data["end_time"];
            $poll["photo_url"] = $data["photo_url"];
            $poll["number_candidates"] = $data["number_candidates"];
            $poll["is_location_specific"] = $data["is_location_specific"];
            $poll["location"] = $data["location"];

            $response["success"] = 1;
            $response["message"] = "Poll Details Successfully Found.";

            $response["poll"] = $poll;

            echo json_encode($response);
        } else {

            $response["success"] = 0;
            $response["message"] = "Poll Not Found";

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

