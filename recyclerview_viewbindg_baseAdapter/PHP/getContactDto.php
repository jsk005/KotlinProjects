<?php
if(!isset($_SESSION)) {
	session_start();
}

// 파일을 직접 실행하면 동작되지 않도록 하기 위해서
if(isset($_POST) && $_SERVER['REQUEST_METHOD'] == "POST"){
	@extract($_POST); // POST 전송으로 전달받은 값 처리
	if(!(isset($idx) && !empty($idx))) {
		echo 0;
		exit;
	}

	require_once 'phpclass/dbconnect.php';
	require_once 'phpclass/loginClass.php';
	$c = new LoginClass();
	$column ="idx,userNM,mobileNO,telNO,photo";
	$sql = "select idx,userNM,mobileNO,telNO,photo from Person";
	if(!empty($search)) {
		$where = "userNM LIKE '%".$search."%' or mobileNO LIKE '%".$search."%'";
	} else {
		$where = "";
	}

	if(strlen($where) > 0){
		$sql .= " where ".$where;
	}

	$R = array(); // 결과 담을 변수 생성
	$result = $c->putDbArray($sql);
	while($row = $result->fetch_assoc()) {
		if($row['photo'] == NULL) {
			$row['photo'] = "";
		} else {
			$path = "./photos/".$row['photo'];
			if(!file_exists($path)) {
				$row['photo'] = "";
			}
		}
		array_push($R, $row);
	}
	header("Cache-Control: no-cache, must-revalidate");
	header("Content-type: application/json; charset=UTF-8");

	$status = "success";
	$result = array(
		'status' => $status,
		'message' => $R
	);
	echo json_encode($result);

}
?>
