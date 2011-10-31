<?php
interface Page_Interface{
	public function __construct();
	public function permissions();
	public function generate();
}
?>