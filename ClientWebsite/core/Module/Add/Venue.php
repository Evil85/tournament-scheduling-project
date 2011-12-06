<?php
class Module_Add_Venue implements Module_Interface{
	private $form;
	private $id = false;
	public function __construct($id){
		$this->id = $id;
		$this->form = new Display_Form(array('method'=>'post'));
		$this->validate();
		$venues = DB_Location::getAllLocations($id);
		$temp = DB_Venue::getVenueList($id);
		$exists = array();
		foreach($temp as $linked){
			$exists[] = $linked['id_location'];
		}
		$form_data = array();
		foreach($venues as $venue){
			if(!in_array($venue['id'],$exists)){
				$form_data[$venue['name']] = $venue['id'];
			}
		}
		$this->count = count($form_data);
		$this->form->toggle(true);
		$this->form->addTitle('Add Venue');
		$this->form->addDropDown(array(
			'label'   => 'Location',
			'require' => true,
			'name'    => 'id_location',
			'options' => $form_data
		));
		$this->form->addControl(array(
			'label' => 'Add Venue',
			'name'  => 'add_venue'
		));
	}
	private function validate(){
		// form validation
		if(isset($_POST['add_venue'])){
			$allow = array('id_location');
			$data = $this->form->getData($allow);
			$data['id_tournament'] = $this->id;
			$result = DB_Venue::add($data);
			if($result === false){
				$this->form->setError('Error Adding your Venue');
				$this->form->failed();
			} else {
				$this->form->reset();
			}
		}
	}
	public function generate(){
		if($this->count > 0){
			$this->form->generate();
		}
	}
}
?> 