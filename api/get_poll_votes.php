<?php

$response = array();

    if (isset($_GET['pid'])) {

        $pid = $_GET["pid"];
        
        require_once __DIR__ . '/db_config_votes.php';
        
        $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

        if ($conn) {

            $sql = "SELECT * FROM opinion.candidate_tb WHERE poll_id = '$pid' ORDER BY poll_position ASC";

            $sql2 = "SELECT candidate_id,count(candidate_id) FROM poll" . $pid . "_votes_tb GROUP BY candidate_id";
            
            $result = mysqli_query($conn, $sql);

            $result2 = mysqli_query($conn,$sql2);

            if (mysqli_num_rows($result) > 0) {
                
            $response["candidates"] = array();
            
            $rows = array();
            while($data2 = mysqli_fetch_assoc($result2)){                       
                array_push($rows,$data2);      
            }
            
            while($data = mysqli_fetch_assoc($result)){

                $candidates= array();
                $candidates["cid"] = $data["cid"];
                $candidates["poll_id"] = $data["poll_id"];
                $candidates["name"] = $data["name"];
                $candidates["party_name"] = $data["party_name"];
                $candidates["poll_position"] = $data["poll_position"];
                $candidates["symbol_photo_url"] = $data["symbol_photo_url"];
                $candidates["gender"] = $data["gender"];
                $candidates["occupation"] = $data["occupation"];
                $candidates["dob"] = $data["dob"];
                $candidates["location"] = $data["location"];
                
                $found = 0;
                for($i = 0; $i < mysqli_num_rows($result2) ; $i++){
                    if($data["cid"] == $rows[$i]["candidate_id"]){
                        $candidates["votes"] = $rows[$i]["count(candidate_id)"];
                        $found = 1;
                    }
                }
                if($found == 0)
                $candidates["votes"] = "0";
                array_push($response["candidates"], $candidates);
            }
                $response["success"] = 1;
                $response["message"] = "Votes Successfully Fetched.";
            
                echo json_encode($response);
            
            } else {
                
                $response["success"] = 0;
                $response["message"] = "No Candidates Found.";

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