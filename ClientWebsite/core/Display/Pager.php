<?php
class Display_Pager{
	private static $static_id = 1;
	private $key;
	private $page;
	private $data;
	private $limit;
	private $count;
	private $perpage;
	private $t;
	
	// public vars
	public $skip;
	public $pages;
	public function __construct($perpage,$count){
		// setting up unique id for this pager
		$this->key = 'p'.self::$static_id;
		self::$static_id++;
		
		// getting values from get
		$this->data = $_GET;
		
		// getting the current page
		if(isset($_GET[$this->key]) && is_numeric($_GET[$this->key]) && $_GET[$this->key] > 0){
			$this->page = $_GET[$this->key];
			unset($this->data[$this->key]);
		} else {
			$this->page = 1;
		}
		
		// setting variables
		$this->perpage = $perpage;
		$this->count   = $count;
		$this->skip    = ($this->page-1)*$perpage;
		$this->pages   = ceil($this->count/$perpage);
		
		// setting up display table
		$this->t = new Display_Table();
		
		// including style
		Style::get('pager');
	}
	private function generateLink($label,$page,$title){
		if($page == $this->page){
			?><span class="pager_current"><?php
			echo $label;
			?></span><?php
			return false;
		}
		$url = '?'.$this->key.'='.$page;
		foreach($this->data as $key => $value){
			$url .= '&'.$key.'='.$value;
		}
		?><a class="pager_link" href="<?php
		echo $url;
		?>" title="<?php
		echo $title;
		?>"><?php
		echo $label;
		?></a><?php
	
	}
	public function generate(){
		// calculating bounds
		if($this->pages <= 1){
			return false;
		}
		$span = 5;
		$low = $this->page - $span;
		$high = $this->page + $span;
		if($low < 1){
			$low = 1;
		}
		if($high > $this->pages){
			$high = $this->pages;
		}
		$t = $this->t;
		$t->newTable(array('class' => 'pager','width'=>'100%'));
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
		$this->generateLink('&gt;|',$this->pages,'Last');
		$t->end();
	}
}
?>