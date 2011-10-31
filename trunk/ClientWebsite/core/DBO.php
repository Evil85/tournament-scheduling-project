<?php
class DBO {
	private $con;
	public $error;
	public $sql_error;
	public $debug;
	
	public function __construct(){
	}
	
	public function connect($server,$user,$pass){
		$this->con = mysql_connect($server,$user,$pass);
		$this->debug = $server.' '.$user.' '.$pass;
		if($this->con === false){
			Error::add('DB','ERROR: Could not connect to database.');
			return false;
		}
		return true;
	}
	
	public function close(){
		mysql_close($this->con);
	}
	
	public function select_db($db_name){
		mysql_select_db($db_name,$this->con);
		return true;
	}
	public function get_table_cols($table){
		$sql = "
			SHOW COLUMNS FROM $table";
		Debug::add('SQL',$sql);
		$resource = mysql_query($sql);
		if($resource === false){
			Error::add('DB',"ERROR: Could not Show Columns.");
			Debug::add('SQLE',"SQL-ERROR: ".mysql_error($this->con));
			return false;
		}
		$cols = array();
		if(mysql_num_rows($resource) > 0){
			while($row = mysql_fetch_assoc($resource)){
				$cols[] = $row['Field'];
			}
		}
		return $cols;
	}
	public function fetch_all($sql){
		Debug::add('SQL',$sql);
		$resource = mysql_query($sql,$this->con);
		if($resource === false){
			Error::add('DB',"ERROR: Could not Fetch Rows.");
			Debug::add('SQLE',"SQL-ERROR: ".mysql_error($this->con));
			return false;
		}
		$rows = array();
		while($row = mysql_fetch_assoc($resource)){
			$rows[] = $row;
		}
		return $rows;
	}
	
	public function fetch_row($sql){
		Debug::add('SQL',$sql);
		$resource = mysql_query($sql,$this->con);
		if($resource === false){
			Error::add('DB',"ERROR: Could not Fetch Row.");
			Debug::add('SQLE',"SQL-ERROR: ".mysql_error($this->con));
			return false;
		}
		$row = mysql_fetch_assoc($resource);
		if($row === false){
			Error::add('DB',"ERROR: No row exists.");
			$this->sql_error = '';
			return false;
		}
		return $row;
	}
	
	public function insert($table,$data){
		$sql = "
			INSERT INTO $table (%s)
			VALUES (%s)";
		$col = '';
		$val = '';
		foreach($data as $key => $value){
			if($value != NULL){
				$col .= " ,$key";
				$val .= " ,'$value'";
			}
		}
		$col = substr($col,2);
		$val = substr($val,2);
		$sql = sprintf($sql,$col,$val);
		$this->debug = $sql;
		$resource = mysql_query($sql,$this->con);
		if($resource === false){
			$this->error = 'ERROR: Could not insert into $table.';
			$this->sql_error = "SQL-ERROR: ".mysql_error($this->con);
			return false;
		}
		return mysql_insert_id($this->con);
	}
	public function update_row($table,$data,$id){
		$sql = "
			UPDATE $table
			SET %s
			WHERE id=$id";
		$actions = '';
		foreach($data as $key => $value){
			if($value != NULL){
				$actions .= " ,$key='$value'";
			}
		}
		$actions = substr($actions,2);
		$sql = sprintf($sql,$actions);
		$this->debug = $sql;
		$resource = mysql_query($sql,$this->con);
		if($resource === false){
			$this->error = "ERROR: Could not update row id:$id of $table.";
			$this->sql_error = "SQL-ERROR: ".mysql_error($this->con);
			return false;
		}
		return true;
	}
	public function delete_row($table, $id){
		$sql ="
			DELETE FROM $table
			WHERE id = $id";
		$this->debug = $sql;
		$resource = mysql_query($sql,$this->con);
		if($resource === false){
			$this->error = "ERROR: Could not delete row id:$ of $table.";
			$this->sql_error = "SQL-ERROR: ".mysql_error($this->con);
			return false;
		}
		return true;
	}
	public function create_table($name,$data){
		$sql = "
			CREATE TABLE $name
			(
				id int NOT NULL AUTO_INCREMENT,
				%s
				PRIMARY KEY(id)
			)
			";
		$cols = '';
		foreach($data as $key => $value){
			$cols .= "$key  $value,";
		}
		$sql = sprintf($sql,$cols);
		$this->debug = $sql;
		$resource = mysql_query($sql,$con);
		if($resource === false){
			$this->error = "ERROR: Could not Create table:$table.";
			$this->sql_error = "SQL-ERROR: ".mysql_error($this->con);
			return false;
		}
		return true;
	}
}
?>