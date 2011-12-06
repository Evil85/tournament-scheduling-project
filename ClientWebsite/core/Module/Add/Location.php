<?php
class Module_Add_Location implements Module_Interface{
	private $form;
	public function __construct($data = array()){
		$time_options = array(' - ' => 'none');
		for($i = 1; $i<=24; $i++){
			$label = ($i > 12) ? ($i-12).' pm' : $i.' am';
			$time_options[$label] = $i;
		}
		
		$this->form = new Display_Form(array('method'=>'post'));
		$this->form->toggle(true);
		$this->form->addTitle('Add Location');
		$this->form->addInput(array(
			'label'   => 'Name',
			'require' => true,
			'name'    => 'name'
		));
		$this->form->addInput(array(
			'label'   => 'Address',
			'require' => true,
			'name'    => 'address'
		));
		$this->form->addInput(array(
			'label'   => 'City',
			'require' => true,
			'name'    => 'city'
		));
		$this->form->addInput(array(
			'label'   => 'State',
			'require' => true,
			'name'    => 'state'
		));
		$this->form->addInput(array(
			'label'   => 'Zip',
			'require' => true,
			'name'    => 'zip'
		));
		$this->form->addInput(array(
			'label'   => 'Phone',
			'require' => true,
			'name'    => 'phone'
		));
		$this->form->addDropDown(array(
			'label'   => 'Weekday Open Time',
			'name'    => 'weekdayOpenTime',
			'require' => true,
			'options' => $time_options
		));
		$this->form->addDropDown(array(
			'label'   => 'Weekday Close Time',
			'name'    => 'weekdayCloseTime',
			'require' => true,
			'options' => $time_options
		));
		$this->form->addDropDown(array(
			'label'   => 'Weekend Open Time',
			'name'    => 'weekendOpenTime',
			'require' => true,
			'options' => $time_options
		));
		$this->form->addDropDown(array(
			'label'   => 'Weekend Close Time',
			'name'    => 'weekendCloseTime',
			'require' => true,
			'options' => $time_options
		));
		
		$this->form->addControl(array(
			'label' => 'Add Location',
			'name'  => 'add_location'
		));
		
		// form validation
		if(isset($_POST['add_location'])){
			$allow = array('name','address','city','state','zip','phone',
				'weekdayOpenTime','weekdayCloseTime','weekendOpenTime','weekendCloseTime');
			$data = $this->form->getData($allow);
			$data['weekdayOpenTime']  .= ':00:00';
			$data['weekdayCloseTime'] .= ':00:00';
			$data['weekendOpenTime']  .= ':00:00';
			$data['weekendCloseTime'] .= ':00:00';
			
			foreach($allow as $key){
				if($data[$key] == ''){
					$this->form->setError('Error Adding your Division');
					$this->form->failed();
					return false;
				}
			}
			
			$result = DB_Location::add($data);
			if($result === false){
				$this->form->setError('Error Adding your Division');
				$this->form->failed();
			} else {
				$this->form->reset();
				Script::add("window.location = 'location.php?lid={$result}';");
			}
		}
	}
	public function permissions(){
		return array('admin');
	}
	public function generate(){
		$this->form->generate();
	}
}
?> 