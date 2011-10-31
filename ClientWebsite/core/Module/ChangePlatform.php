<?php
class Module_ChangePlatform implements Module_Interface {
	private $extra;
	public function __construct($data = ''){
		Style::get('page');
		Style::get('ui');
		$this->extra = '';
		unset($_GET['p']);
		foreach($_GET as $key => $value){
			$this->extra .= '&'.urlencode($key).'='.urlencode($value);
		}
	}
	public function generate(){
		?><div class="main_content"><?php
		$t = new Display_Table();
		$t->newTable(array('class' => '', 'width' => '100%'));
		$t->newCol(array('align' => 'right'));
		echo 'Mobile Version';
		$t->newCol(array('width' => 90));
		if(User::get_platform() == 'computer'){
			?><a href="?p=mobile<?php echo $this->extra; ?>" title="view mobile site" class="switch_off"></a><?php
		} else {
			?><a href="?p=computer<?php echo $this->extra; ?>" title="view desktop site" class="switch_on"></a><?php
		}
		$t->end();
		?></div><?php
	}
}
?>