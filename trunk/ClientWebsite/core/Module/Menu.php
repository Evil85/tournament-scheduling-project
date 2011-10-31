<?php
class Module_Menu implements Module_interface {
	private $menu_items;
	public function __construct($data) {
		Debug::add('menu',$data);
		$this->menu_items = $data;
		Style::get('menu');
		Style::get('ui');
		if(User::get_platform() == 'mobile'){
			Script::get('display');
		}
	}
	public function generate(){
		?><div class="menu_container"><?php
			if(User::get_platform() == 'mobile'){
				?><a class="down_arrow" href="#" onclick="toggle('main_menu_items');"><span class="menu_icon"></span></a><?php
				?><div class="menu_item_container" id="main_menu_items" style="display:none"><?php
			} else {
				?><div class="menu_item_container"><?php
			}
			foreach($this->menu_items as $link){
				?><a class="menu_item" href="<?php echo $link['path'];?>"><?php 
				if(isset($link['icon'])){
					?><span class="<?php echo $link['icon']; ?>"> </span><?php
				}
				?><span class="menu_label"><?php
				echo $link['label']; 
				?></span><?php
				?></a><?php
			}
			?></div></div><?php
	}
}