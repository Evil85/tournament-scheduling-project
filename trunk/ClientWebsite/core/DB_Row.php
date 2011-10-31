<?php
class DB_Row{
	private $sql_error;
	private $error;
	private $debug;
	private $data;
	private $db;
	private $table;
	public function __construct($db,$table){
		$this->table = $table;
		$cols = $db->get_table_cols($table);
		$this->debug = $db->debug;
		if($cols === false){
			$this->set_errors();
			return false;
		}
		foreach($cols as $value){
			$this->data[$value] = NULL;
		}
		$this->db = $db;
	}
	public function fetch($id){
		$table = $this->table;
		$sql = "
			SELECT *
			FROM $table
			WHERE id = $id
			LIMIT 1
		";
		$this->debug = $sql;
		$result = $this->db->fetch_row($sql);
		if($result === false){
			$this->set_errors();
			return false;
		}
		$this->data = $result;
		return true;
	}
	public function fetch_from_key($key){
		$table = $this->table;
		$main_key = $table.'_key';
		$sql = "
			SELECT *
			FROM $table
			WHERE $main_key = '$key'
			LIMIT 1
		";
		$this->debug = $sql;
		$result = $this->db->fetch_row($sql);
		if($result === false){
			$this->set_errors();
			return false;
		}
		$this->data = $result;
		return true;
	}
	public function update(){
		$result = $this->db->update_row($this->table,$this->data,$this->data[$this->table.'_id']);
		$this->debug = $this->db->debug;
		if($result === false){
			$this->set_errors();
			return false;
		}
		echo $this->debug;
		return true;
	}
	public function add(){
		$result = $this->db->insert($this->table,$this->data);
		$this->debug = $this->db->debug;
		if($result === false){
			$this->set_errors();
			return false;
		}
		$this->data['id'] = $result;
		return $result;
	}
	public function delete(){
		$result = $this->db->delete($this->table,$this->data['id']);
		$this->debug = $this->db->debug;
		if($result === false){
			$this->set_errors();
			return false;
		}
		return true;
	}
	public function __set($name,$value){
		if(array_key_exists($name,$this->data)){
			$this->data[$name] = $value;
		}
	}
	public function __get($name){
		if($name == 'data'){
			return $this->data;
		}
		return $this->data[$name];
	}
	private function set_errors(){
		$this->error = $this->db->error;
		$this->sql_error = $this->db->sql_error;
		echo $this->error;
		echo $this->sql_error;
	}
	public function print_object($toString = false){
		$str = '<pre>'.print_r($this->data,true).'</pre>';
		if($toString){
			return $str;
		}
		echo $str;
	}
}
?>