<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require $_SERVER['DOCUMENT_ROOT'] . '/opinion/api/mail/Exception.php';
require $_SERVER['DOCUMENT_ROOT'] . '/opinion/api/mail/PHPMailer.php';
require $_SERVER['DOCUMENT_ROOT'] . '/opinion/api/mail/SMTP.php';

if (isset($_GET['name']) && isset($_GET['email']) && isset($_GET['otp'])) {

    $emailId = $_GET['email'];
    $emailName = $_GET['name'];
    $otp = $_GET['otp'];

    require_once __DIR__ . '/db_config.php';
    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if ($conn) {

        $sql = "SELECT * FROM user_tb WHERE email = '$emailId'";

        $result = mysqli_query($conn, $sql);

        if (mysqli_num_rows($result) > 0) {

            $response["success"] = 0;
            $response["message"] = "User with same email already present, Login insted.";

            echo json_encode($response);
        } else {

            $mail = new PHPMailer;
            $mail->isSMTP();
            $mail->SMTPDebug = 0; // 0 = off (for production use) - 1 = client messages - 2 = client and server messages
            $mail->Host = "smtp.gmail.com"; // use $mail->Host = gethostbyname('smtp.gmail.com'); // if your network does not support SMTP over IPv6
            $mail->Port = 587; // TLS only
            $mail->SMTPSecure = 'tls'; // ssl is deprecated
            $mail->SMTPAuth = true;
            $mail->Username = 'nileshinde7@gmail.com'; // email
            $mail->Password = 'nilesh7812'; // password
            $mail->setFrom('opinion@youropinion.com', 'Opinion OTP'); // From email and name
            $mail->addAddress($emailId, $emailName); // to email and name
            $mail->Subject = 'Verification OTP for Opinion';
            $mail->msgHTML("Hello " . $emailName . ".Your OTP is " . $otp . "."); //$mail->msgHTML(file_get_contents('contents.html'), __DIR__); //Read an HTML message body from an external file, convert referenced images to embedded,
            $mail->AltBody = 'HTML messaging not supported'; // If html emails is not supported by the receiver, show this body
            // $mail->addAttachment('images/phpmailer_mini.png'); //Attach an image file

            if (!$mail->send()) {
                $response["success"] = 0;
                $response["message"] = "Failed to Send OTP.Try Again.";
                echo json_encode($response);
                //echo "Mailer Error: " . $mail->ErrorInfo;
            } else {
                $response["success"] = 1;
                $response["message"] = "OTP Sent Successfully.";

                echo json_encode($response);
            }
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
