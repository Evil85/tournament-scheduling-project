<?php
class Module_Add_Court implements Module_Interface{
	private $form;
	private $id = false;
	public function __construct($id){
		$this->id = $id;
		$this->form = new Display_Form(array('method'=>'post'));
		$this->form->toggle(true);
		$this->form->addTitle('Add Court');
		$this->form->addInput(array(
			'label'   => 'Court Name',
			'require' => true,
			'name'    => 'courtName'
		));
		$this->form->addControl(array(
			'label' => 'Add Court',
			'name'  => 'add_court'
		));
		
		// form validation
		if(isset($_POST['add_court'])){
			$allow = array('courtName');
			$data = $this->form->getData($allow);
			$data['id_location'] = $this->id;
			$result = DB_Court::add($data);
			if($result === false){
				$this->form->setError('Error Adding your Court');
				$this->form->failed();
			} else {
				$this->form->reset();
			}
		}
	}
	public function generate(){
		$this->form->generate();
	}
}
?> 