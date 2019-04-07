<?php

$response = array();

if (isset($_GET['name']) && isset($_GET['pass'])) {


    $name = $_GET["name"];
    $pass = $_GET["pass"];
    $uidno = $_GET["uidno"];
    $gender = $_GET["gender"];
    $occupation = $_GET["occupation"];
    $email = $_GET["email"];
    $phone = $_GET["phone"];
    $state = $_GET["state"];
    $city = $_GET["city"];
    $pincode = $_GET["pincode"];
    $photo_url = $_GET["photo_url"];
    $sec_question = $_GET["sec_question"];
    $sec_answer = $_GET["sec_answer"];
    $dob = $_GET["dob"];
    $latlong = $_GET["latlong"];
    $verified = $_GET["verified"];
    $organization = $_GET["organization"];
    $creation_datetime = $_GET["creation_datetime"];

    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {
        $sql = "INSERT INTO user_tb(name,pass,uidno,gender,occupation,email,phone,state,city,pincode,photo_url,sec_question,sec_answer,dob,latlong,verified,organization,creation_datetime)
                    VALUES ('$name','$pass','$uidno','$gender','$occupation','$email','$phone','$state','$city','$pincode','$photo_url','$sec_question','$sec_answer','$dob','$latlong','$verified','$organization','$creation_datetime')";


        if (mysqli_query($conn, $sql)) {

            $response["success"] = 1;
            $response["message"] = "User Successfully Created.";

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
