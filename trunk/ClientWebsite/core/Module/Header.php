<?php
class Module_Header implements Module_Interface {
	private $user_info;
	private $menu;
	public function __construct($data = '') {
		$this->user_info = DB_User::getUserData();
		$this->menu = new Module_Menu(Permissions::getMenu());
		Style::get('header');
	}
	public function generate() {
		?><div class="main_header">
		<span class="title">
		Hello <?php echo $this->user_info['username'];
		?></span><?php
		$this->menu->generate();
		?>
		</div><?php
	}
}
?>