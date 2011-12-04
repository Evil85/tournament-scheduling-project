<?php
class Module_Add_Tournament implements Module_Interface{
	private $form;
	public function __construct($data = ''){
		$time_options = array(' - ' => 'none');
		for($i = 1; $i<=24; $i++){
			$label = ($i > 12) ? ($i-12).' pm' : $i.' am';
			$time_options[$label] = $i;
		}
	
		$form_data = array('method' => 'post');
		$this->form = new Display_Form($form_data);
		$this->form->toggle(true);
		$this->form->addTitle('Add Tournament');
		$this->form->addInput(array(
			'label'   => 'Name',
			'require' => true,
			'name'    => 'name'
		));
		$this->form->addInput(array(
			'label'   => 'Start Date',
			'require' => true,
			'name'    => 'start_date',
			'id'      => 'start_date_picker'
		));
		$this->form->addInput(array(
			'label'   => 'End Date',
			'require' => true,
			'name'    => 'end_date',
			'id'      => 'end_date_picker'
		));
		$this->form->addDropDown(array(
			'label'   => 'Visibility',
			'name'    => 'isGuestViewable',
			'require' => true,
			'options' => array(
				'Public'  => '1',
				'Private' => '0'
			)
		));
		$this->form->addDropDown(array(
			'label'   => 'Start Time on Weekdays',
			'name'    => 'start_time_weekdays',
			'require' => true,
			'options' => $time_options
		));
		$this->form->addDropDown(array(
			'label'   => 'End Time on Weekdays',
			'name'    => 'end_time_weekdays',
			'require' => true,
			'options' => $time_options
		));
		$this->form->addDropDown(array(
			'label'   => 'Start Time on Weekends',
			'name'    => 'start_time_weekends',
			'require' => true,
			'options' => $time_options
		));
		$this->form->addDropDown(array(
			'label'   => 'End Time on Weekends',
			'name'    => 'end_time_weekends',
			'require' => true,
			'options' => $time_options
		));
		$this->form->addDropDown(array(
			'label'   => 'Max Divisions per player',
			'name'    => 'maxDivPerPlayer',
			'require' => true,
			'options' => array(
				'1' => '1',
				'2' => '2',
				'3' => '3',
				'4' => '4',
				'5' => '5'
			)
		));
		$this->form->addControl(array(
			'label' => 'Add Tournament',
			'name'  => 'add_tournament'
		));
		
		// validating form
		if(isset($_POST['add_tournament'])){
			$allow = array(
				'name','start_date','end_date','start_time_weekdays',
				'end_time_weekdays','start_time_weekends','end_time_weekends',
				'maxDivPerPlayer','isGuestViewable'
			);
			$data = $this->form->getData($allow);
			$user_info = DB_User::getUserData();
			$data['id_owner'] = $user_info['id'];
			$data['start_time_weekdays'] .= ':00:00';
			$data['end_time_weekdays']   .= ':00:00';
			$data['start_time_weekends'] .= ':00:00';
			$data['end_time_weekends']   .= ':00:00';
			
			$result = DB_Tournament::add($data);
			if($result === false){
				// if form was not filled out correctly
				$this->form->failed();
			} else {
				$this->form->reset(true);
				Script::add("window.location = 'tournament.php?id={$result}';");
			}
		}
		
		// adding style and script for page
		Style::get('jquery_datepicker');
		Script::get('jquery_datepicker');
		$custom_script = '
			$(function() {
				var dates = $( "#start_date_picker, #end_date_picker" ).datepicker({
					defaultDate: "+1w",
					changeMonth: true,
					numberOfMonths: 1,
					dateFormat: "yy-mm-dd",
					onSelect: function( selectedDate ) {
						var option = this.id == "start_date_picker" ? "minDate" : "maxDate",
							instance = $( this ).data( "datepicker" ),
							date = $.datepicker.parseDate(
								instance.settings.dateFormat ||
								$.datepicker._defaults.dateFormat,
								selectedDate, instance.settings );
						dates.not( this ).datepicker( "option", option, date );
					}
				});
			});
		';
		Script::add($custom_script);
		$custom_style = "
			<style>
			#ui-datepicker-div{
				font-size:16px;
			}
			</style>
		";
		Style::add($custom_style);
	}
	public function generate(){
		$this->form->generate();
	}
}
?>