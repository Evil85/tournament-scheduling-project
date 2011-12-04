<?php
class Module_Add_Division implements Module_Interface{
	private $form;
	public function __construct($data = array()){
		if(!isset($data['tid'])){
			$this->form = false;
			return false;
		}
		$this->form = new Display_Form(array('method'=>'post'));
		$this->form->toggle(true);
		$this->form->addTitle('Add Division');
		$this->form->addInput(array(
			'label'   => 'Name',
			'require' => true,
			'name'    => 'name'
		));
		$this->form->addDropDown(array(
			'label'   => 'Game Type',
			'require' => true,
			'name'    => 'isDouble',
			'options' => array(
				'Singles' => '0',
				'Doubles' => '1'
			)
		));
		$this->form->addDropDown(array(
			'label'   => 'Gender Constraint',
			'require' => true,
			'name'    => 'genderConstraint',
			'options' => array(
				'Coed'   => 'a',
				'Male'   => 'm',
				'Female' => 'f'
			)
		));
		$this->form->addDropDown(array(
			'label'   => 'Tournament Style',
			'require' => true,
			'name'    => 'tournType',
			'options' => DB_Tournament::getTypes()
		));
		$this->form->addControl(array(
			'label' => 'Add Division',
			'name'  => 'add_division'
		));
		
		// form validation
		if(isset($_POST['add_division'])){
			$allow = array('name','isDouble','genderConstraint','tournType');
			$ddata = $this->form->getData($allow);
			$ddata['id_tournament'] = $data['tid'];
			$result = DB_Division::add($ddata);
			if($result === false){
				$this->form->setError('Error Adding your Division');
				$this->form->failed();
			} else {
				$this->form->reset();
			}
		}
	}
	public function permissions(){
		return array('admin');
	}
	public function generate(){
		if($this->form === false){
			?><h2>Error creating form</h2><?php
			return false;
		}
		$this->form->generate();
	}
}
?> 