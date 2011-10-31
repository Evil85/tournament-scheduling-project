<?php
class Display_Form{
	private $inputs;
	private $controls;
	private $table;
	private $form_data;
	private $reset;
	private $title;
	private $subtext;
	private $toggle;
	public $id;
	private static $static_id = 0;
	public function __construct($data = array('method' => 'post')){
		$this->form_data = $data;
		$this->table     = new Display_Table();
		$this->inputs    = array();
		$this->reset     = false;
		$this->controls  = array();
		$this->title     = false;
		$this->subtext   = false;
		$this->toggle    = false;
		$this->id        = 'form_'.self::$static_id;
		self::$static_id++;
		Script::get('display');
		Style::get('form');
		Debug::add('form',$_REQUEST);
	}
	public function toggle($val){
		$this->toggle = $val;
	}
	public function failed(){
		$this->toggle = false;
		$center_js = "window.location = '#".$this->id."';";
		Script::add($center_js);
	}
	public function getData($allow){
		$data = array();
		foreach($allow as $key){
			if(array_key_exists($key,$_POST)){
				$data[$key] = $_POST[$key];
			}
		};
		return $data;
	}
	public function addInput($data){
		if(!isset($data['render'])){
			$data['render'] = 'input';
		}
		$this->inputs[] = $this->normalize($data,'input');
	}
	private function input($data){
		?><input <?php echo $this->getParams($data);?>/><?php
	}
	public function div($data){
		?><div <?php echo $this->getParams($data);?>></div><?php
	}
	public function addDropDown($data){
		$data['render'] = 'select';
		$this->inputs[] = $this->normalize($data,'select');
	}
	public function addTitle($title){
		$this->title = $title;
	}
	public function addSubText($text){
		$this->subtext = $text;
	}
	private function select($data){
		?><select <?php echo $this->getParams($data); ?>><?php
		foreach($data['options'] as $label => $value){
			$selected = '';
			if($this->reset === false 
				&& isset($_POST[$data['name']])
				&& $value == $_POST[$data['name']]
			){
				$selected = 'selected';
			}
			?><option value="<?php echo $value; ?>" <?php echo $selected; ?>><?php
			echo $label; 
			?></option><?php
		}
		?></select><?php
	}
	public function addControl($data){
		$this->controls[] = $this->normalize($data,'control');
	}
	public function reset($value = true){
		$this->reset = $value;
	}
	public function generate(){
		?><div id="<?php echo $this->id; ?>"><?php
		$t = $this->table;
		$form_id    = $this->id.'_content';
		$form_style = '';
		if($this->title !== false){
			?><span class="form_title"><?php
			echo $this->title; 
			if($this->toggle){
				$js = "toggle('{$form_id}');"
				?><a href="#<?php echo $this->id; ?>" class="form_toggle" onclick="<?php echo $js; ?>" title="toggle form"></a><?php
				$form_style = 'display:none;';
			} 
			?></span><?php
		}
		?><div id="<?php echo $form_id;?>" style="<?php echo $form_style;?>"><?php
		if($this->subtext !== false){
			?><p class="form_title_subtext"><?php echo $this->subtext; ?></p><?php
		}
		?><div class="form_container"><?php
		?><form <?php echo $this->getParams($this->form_data); ?>><?php
		$t->newTable(array('width' => '100%'));
		foreach($this->inputs as $input){
			$t->newRow();
			$t->newCol();
			?><span class="form_label"><?php
			echo $input['label'];
			if($input['require']){
				?><span class="form_require">*</span><?php
			}
			?></span><?php
			if(isset($input['subtext'])){
				?><br/><span class="form_subtext"><?php
				echo $input['subtext'];
				?></span><?php
			}
			if(User::get_platform() == 'mobile'){
				$t->newRow();
			}
			$t->newCol(array('align' => 'right'));
			$this->$input['render']($input); 
		}
		foreach($this->controls as $control){
			$t->newRow();
			if(User::get_platform() == 'mobile') {
				$t->newCol(array('align'=>'right'));
			} else {
				$t->newCol(array('colspan' => 2, 'align' => 'right'));
			}
			?><button <?php echo $this->getParams($control);?>><?php
			echo $control['label']; 
			?></button><?php
		}
		$t->end();
		?></form></div><?php
		?></div></div><?php
	}
	private function normalize($data,$type){
		if($type == 'input'){
			if(!isset($data['type'])){
				$data['type'] = 'text';
			}
			if(!isset($data['label'])){
				$data['label'] = 'New Input';
			}
			if(!isset($data['require'])){
				$data['require'] = false;
			}
			if(!isset($data['name'])){
				$data['name'] = 'unknown[]';
			}
		} else if($type == 'control'){
			if(!isset($data['type'])){
				$data['type'] = 'submit';
			}
		} else if($type == 'select'){
			if(!isset($data['name'])){
				$data['name'] = 'unknown[]';
			}
			if(!isset($data['label'])){
				$data['label'] = 'New Input';
			}
			if(!isset($data['options'])){
				$data['options'] = array(' - ' => 0); 
			}
		}
		return $data;
	}
	private function strip($data){
		$strip = array('require','subtext','label','options');
		foreach($strip as $value){
			if(isset($data[$value])){
				unset($data[$value]);
			}
		}
		return $data;
	}
	private function getParams($data){
		$data = $this->strip($data);
		$params = array();
		if($this->reset === false && isset($data['name'])){
			if(isset($_POST[$data['name']])){
				$data['value'] = $_POST[$data['name']];
			}
		}
		foreach($data as $attribute => $value){
			$params[] = $attribute.'="'.$value.'"';
		}
		return implode(' ',$params);
	}
}
?>