<?php

$response = array();

    if (isset($_GET['uid'])) {

        $uid = $_GET["uid"];
        
        require_once __DIR__ . '/db_config.php';
        
        $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

        if ($conn) {

            $sql = "SELECT * FROM poll_tb WHERE created_by = '$uid' ORDER BY creation_date DESC";
            
        $result = mysqli_query($conn, $sql);

            if (mysqli_num_rows($result) > 0) {
                
                $response["polls"] = array();
                
            while($data = mysqli_fetch_assoc($result)){
                $polls= array();
                $polls["pid"] = $data["pid"];
                $polls["created_by"] = $data["created_by"];
                $polls["title"] = $data["title"];
                $polls["description"] = $data["description"];
                $polls["creation_date"] = $data["creation_date"];
                $polls["end_time"] = $data["end_time"];
                $polls["photo_url"] = $data["photo_url"];
                $polls["number_candidates"] = $data["number_candidates"];
                $polls["is_location_specific"] = $data["is_location_specific"];
                $polls["location"] = $data["location"];

                array_push($response["polls"], $polls);
            }
                $response["success"] = 1;
                $response["message"] = "Polls Successfully Found.";
            
                echo json_encode($response);
            
            } else {
                
                $response["success"] = 0;
                $response["message"] = "No Polls Found.";

                echo json_encode($response); 
            
                //echo "Error: " . $sql . "" . mysqli_error($conn);
            
            }
        }else{
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
    
?>