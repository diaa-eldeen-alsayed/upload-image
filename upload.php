<?php
$user_name="root";
$user_pass="";
$host_name="localhost";
$db_name="dbupload";
$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);
if($con){
	$image=$_POST["image"];
	$name=$_POST["name"];
	$sql="insert into imageinfo (name) values ('$name')";
	$upload_path="uploads/$name.jpg";
	if(mysqli_query($con,$sql)){
		file_put_contents($upload_path,base64_decode($image));
		echo json_encode(array('respone '=>' upload successfuly'));
	}
	else{
		echo json_encode(array('respone '=>' upload failed'));
	}
	
}
else{
	echo json_encode(array('respone '=>' not connection '));
	
}
mysqli_close($con);
?>
