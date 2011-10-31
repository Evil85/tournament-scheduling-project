<?php
class Display_Table{
	private $state;
	private $flip;
	public function __construct(){
		$this->state = array(
			'table' => 0,
			'tr'    => 0,
			'td'    => 0
		);
		$this->flip = true;;
		Style::get('table');
	}
	private function getParams($data){
		$params = array();
		foreach($data as $attribute => $value){
			$params[] = $attribute.'="'.$value.'"';
		}
		return implode(' ',$params);
	}
	public function newTable($data = array()){
		if(!isset($data['class'])){
			$data['class'] = 'display_table';
		}
		$this->state['table']++;
		?><table <?php echo $this->getParams($data);?>><?php
	}
	public function newTitle($data = array()){
		$data['class'] = 'title';
		$this->newRow($data);
	}
	public function newRow($data = array()){
		if($this->state['table'] == 0){
			$this->newTable();
		}
		if($this->state['tr'] < $this->state['table']){
			$this->state['tr']++;
		} else {
			echo '</tr>';
		}
		$this->flip = !$this->flip;
		if(!isset($data['class'])){
			$data['class'] = 'even';
			if($this->flip){
				$data['class'] = 'odd';
			}
		}
		?><tr <?php echo $this->getParams($data);?>><?php
	}
	public function newCol($data = array()){
		if($this->state['tr'] < $this->state['table'] || $this->state['tr'] == 0){
			$this->newRow();
		}
		if($this->state['td'] < $this->state['tr']){
			$this->state['td']++;
		} else {
			echo '</td>';
		}
		?><td <?php echo $this->getParams($data);?>><?php
	}
	public function end(){
		$this->state['td']--;
		echo '</td>';
		$this->state['tr']--;
		echo '</tr>';
		$this->state['table']--;
		echo '</table>';
	}
}
?>