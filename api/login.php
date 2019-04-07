<?php


if (isset($_GET['email']) && isset($_GET['password'])) {

    $email = $_GET["email"];
    $password = $_GET["password"];

    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {

        $sql = "SELECT * FROM user_tb WHERE email = '$email'";

        $result = mysqli_query($conn, $sql);

        if (mysqli_num_rows($result) > 0) {

            $data = mysqli_fetch_assoc($result);

            if($data["pass"] == $password){

                $user["uid"] = $data["uid"];
                $user["name"] = $data["name"];
                $user["pass"] = $data["pass"];
                $user["uidno"] = $data["uidno"];
                $user["gender"] = $data["gender"];
                $user["occupation"] = $data["occupation"];
                $user["email"] = $data["email"];
                $user["phone"] = $data["phone"];
                $user["state"] = $data["state"];
                $user["city"] = $data["city"];
                $user["pincode"] = $data["pincode"];
                $user["photo_url"] = $data["photo_url"];
                $user["sec_question"] = $data["sec_question"];
                $user["sec_answer"] = $data["sec_answer"];
                $user["dob"] = $data["dob"];
                $user["latlong"] = $data["latlong"];
                $user["verified"] = $data["verified"];
                $user["organization"] = $data["organization"];
                $user["creation_datetime"] = $data["creation_datetime"];
    
                $response["success"] = 1;
                $response["message"] = "Login Successfull";
                
                $response["user"] = $user;
    
                echo json_encode($response);
            }else{
                $response["success"] = 0;
                $response["message"] = "Wrong Password.";
    
                echo json_encode($response);
            }
            
            
        } else {

            $response["success"] = 0;
            $response["message"] = "User Not Found.";

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