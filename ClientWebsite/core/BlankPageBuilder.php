<?php
class BlankPageBuilder extends PageBuilder{
	public function generate(){
		
		?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><?
		?><html><?php
		?><head><?php
		?><title><?php echo $this->title; ?></title><?php
		Style::generate(); 
		?></head><?php
		?><body><?php
		?><div class="main_container"><?php
		$this->header->generate();
		$this->page->generate();
		?></div><?php
		Debug::generate();
		Script::generate();
		?></body><?php
		?></html><?php
	}
}
?>