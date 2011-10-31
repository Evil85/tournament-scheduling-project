<?php
class Page_Users implements Page_Interface{
	private $table;
	private $users;
	private $form;
	public function __construct(){
		$this->table = new Display_Table();
		$this->form  = new Module_Add_User();
		$this->users = User::get_user_list();
	}
	public function permissions(){
		return array('admin');
	}
	public function generate(){
		?><h2>User List</h2><?php
		$data = array('align' => 'center');
		$t = $this->table;
		?><div style="margin:10px;"><?php
		$t->newTable(array('width' => '100%'));
		$t->newTitle();
		$t->newCol($data);
		echo 'UID';
		$t->newCol($data);
		echo 'Name';
		$t->newCol($data);
		echo 'Username';
		if(User::get_platform() == 'computer'){
			$t->newCol($data);
			echo 'Email';
			$t->newCol($data);
			echo 'Phone';
			$t->newCol($data);
			echo 'Permissions';
		}
		
		foreach($this->users as $user){
			$t->newRow();
			$t->newCol($data);
			?><a href="profile.php?uid=<?php echo $user['uid'];?>" title="View Profile"><?
			echo $user['uid'];
			?></a><?php
			$t->newCol();
			echo $user['name'];
			$t->newCol();
			echo $user['username'];
			if(User::get_platform() == 'computer'){
				$t->newCol();
				echo $user['email'];
				$t->newCol();
				echo $user['phone'];
				$t->newCol($data);
				echo $user['permissions'];
			}
		}
		$t->end();
		?></div><div style="margin:10px;"><?php
		$this->form->generate();
		?></div><?php
	}
}
?>