<?php
class DBController {
    protected $db; // 변수를 선언한 클래스와 상속받은 클래스에서 참조할 수 있다.

    // 생성자
    function __construct() {
        $this->db = $this->connectDB();
		// construct 메소드는 객체가 생성(인스턴스화)될 때 자동으로 실행되는 특수한 메소드다.
    }

    // 소멸자(destructor)
    function __destruct() {
		mysqli_close($this->connectDB());
    }

    private function connectDB() {
        require_once 'dbinfo.php';
        // MySQLi 객체지향 DB 연결
        $conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
		$conn->set_charset("utf8");
        return $conn; // return database handler
    }

	public function putDbArray($sql) {
		$stmt = $this->db->prepare($sql);
		$stmt->execute();
		$result = $stmt->get_result();
		return $result;
	}

	public function getDbArray($sql) {
		$stmt = $this->db->prepare($sql);
		$stmt->execute();
		$result = $stmt->get_result();
		return $result;
	}

	public function SearchFiltering($str){
		// 해킹 공격을 대비하기 위한 코드
		$str = preg_replace("/[\s\t\'\;\"\=\-]+/","", $str); // 공백이나 탭 제거, 특수문자 제거
		return $str;
	}

}
?>
