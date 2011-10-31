<?php
class Display_Pager{
	private static $static_id = 1;
	private $key;
	private $page;
	private $data;
	private $limit;
	private $t;
	public function __construct(){
		$this->data = $_GET;
		$this->key = 'p'.self::$static_id;
		self::$static_id++;
		if(isset($_GET[$this->key]) && is_numeric($_GET[$this->key]) && $_GET[$this->key] > 0){
			$this->page = $_GET[$this->key];
			unset($this->data[$this->key]);
		} else {
			$this->page = 1;
		}
		$this->t = new Display_Table();
	}
	public function setLimit($limit){
		$this->limit = $limit;
	}
	public function getPage(){
		return $this->page;
	}
	private function generateLink($label,$page,$title){
		if($page == $this->page){
			echo $label;
			return false;
		}
		$url = '?'.$this->key.'='.$page;
		foreach($this->data as $key => $value){
			$url .= '&'.$key.'='.$value;
		}
		?><a href="<?php
		echo $url;
		?>" title="<?php
		echo $title;
		?>"><?php
		echo $label;
		?></a><?php
	
	}
	public function generate(){
		// calculating bounds
		$span = 5;
		$low = $this->page - $span;
		$high = $this->page + $span;
		if($low < 1){
			$low = 1;
		}
		if($high > $this->limit){
			$high = $this->limit;
		}
		$t = $this->t;
		$t->newTable(array('class' => '','width'=>'100%'));
		$t->newCol(array('align'=>'center','width'=>'50'));
		$this->generateLink('|&lt;',1,'First');
		$t->newCol(array('align'=>'center'));
		$t->newTable(array('class' => ''));
		for($i=$low; $i<=$high; $i++){
			$t->newCol();
			$this->generateLink($i,$i,'Page '.$i);
		}
		$t->end();
		$t->newCol(array('align'=>'center','width'=>'50'));
		$this->generateLink('&gt;|',$this->limit,'Last');
		$t->end();
	}
}
?>