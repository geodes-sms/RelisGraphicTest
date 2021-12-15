<?php //test
function get_classification_test(){
$reference_tables=array();//from nowit will worklike this
$config=array();
$result['class_action']='no_override';
$result['screen_action']='override';
$result['qa_action']='override';
$result['project_title']='Project 2';
$result['project_short_name']='test';
$config['classification']['table_name']='classification';
$config['classification']['config_id']='classification';
$config['classification']['table_id']='class_id';
$config['classification']['table_active_field']='class_active';
$config['classification']['main_field']='class_paper_id';

$config['classification']['entity_label']='Classification';
$config['classification']['entity_label_plural']='Classifications';


$config['classification']['reference_title']='Classifications';
$config['classification']['reference_title_min']='Classification';

$config['classification']['order_by']='class_id ASC ';


$config['classification']['links'][ 'edit']=array(
	   			'label'=>'Edit',
	   			'title'=>'Edit classification',
	   			'on_list'=>False,
	   			'on_view'=>True
	   	);

$config['classification']['links'][ 'view']=array(
	   			'label'=>'View',
	   			'title'=>'View',
	   			'on_list'=>True,
	   			'on_view'=>True
	   	);

$config['classification']['fields'][ 'class_id']=array(
	   			'field_title'=>'#',
	   			'field_type'=>'number',
	   			'field_value'=>'auto_increment',
	   			'default_value'=>'auto_increment',
	   			
	   			'on_add'=>'hidden',
	   			'on_edit'=>'hidden',
	   			'on_list'=>'show',
	   			'on_view'=>'hidden',
	   	);

$config['classification']['fields'][ 'class_paper_id']=array(
	   			'field_title'=>'Paper',
	   			'field_type'=>'int',
	   			'field_size'=>11,
	   			//'field_value'=>'normal',
				'input_type'=>'select',
				'input_select_source'=>'table',
				'input_select_values'=>'papers;CONCAT_WS(" - ",bibtexKey,title)',				
				'mandatory'=>' mandatory ',
				
	   			'on_add'=>'enabled',
	   			'on_edit'=>'enabled',
	   			'on_list'=>'show'
	   	);
//project specific area


$config['classification']['fields'][ 'string_100']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'String100',
 	'input_type'=>'text',
 	'field_size'=>100,
 	'field_type'=>'text',
 	//'number_of_values'=>'',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	
 	'mandatory'=>' mandatory ',

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	

$config['classification']['fields'][ 'int_33']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'Int4',
 	'input_type'=>'text',
 	'field_size'=>4,
 	'field_type'=>'int',
 	//'number_of_values'=>'',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	
 	'mandatory'=>' mandatory ',

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	

$config['classification']['fields'][ 'bool_1']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'Boolean',
 	'input_type'=>'text',
 	'field_size'=>20,
 	'field_type'=>'bool',
 	'field_value'=>'0_1',
 	'field_size'=>1,
 	'field_type'=>'int',
 	'input_type'=>'select',
 	'input_select_source'=>'yes_no',
 	'input_select_values'=>'1',
 	//'number_of_values'=>'',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	
 	'mandatory'=>' mandatory ',

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	

$config['classification']['fields'][ 'real_1']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'Real',
 	'input_type'=>'text',
 	'field_size'=>20,
 	'field_type'=>'real',
 	//'number_of_values'=>'',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	
 	'mandatory'=>' mandatory ',

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	

$config['classification']['fields'][ 'add_int']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'Add Int',
 	'input_type'=>'text',
 	'field_size'=>4,
 	'field_type'=>'int',
 	//'number_of_values'=>'',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	
 	'mandatory'=>' mandatory ',

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	

$config['classification']['fields'][ 'pattern']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'pattern',
 	'input_type'=>'text',
 	'field_size'=>50,
 	'field_type'=>'text',
 	//'number_of_values'=>'',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	

 	'pattern'=>'[A-Z]+[0-9]*',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	
$config['classification']['fields'][ 'dynListOne']=array( 		
	'category_type'=>'IndependantDynamicCategory',		
 	'field_title'=>'DynamicListOne',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'1',//a verifier
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'listOneDynamic',
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 				);
 	$reference_tables['listOneDynamic']['ref_name'] ='listOneDynamic';   
 	$initial_values=array();
 	array_push($initial_values, "DynamicListData1");
 	array_push($initial_values, "DynamicListData2");
 	array_push($initial_values, "DynamicListData3");
 	array_push($initial_values, "DynamicListData4");
 	array_push($initial_values, "DynamicListData5");
 	if(empty($reference_tables['listOneDynamic']['values'])){
 		$reference_tables['listOneDynamic']['values'] =	$initial_values;
 	}else{
 		foreach ($initial_values as $key => $value) {
 			if (!in_array($value, $reference_tables['listOneDynamic']['values'] )){
 				array_push($reference_tables['listOneDynamic']['values'],$value);
 			}
 		}		
 	}
 		
 		
$config['dynList3']['table_name']='dynList3';
$config['dynList3']['table_id']='dynList3_id';
$config['dynList3']['table_active_field']='dynList3_active';
$config['dynList3']['main_field']='dynList3';
$config['dynList3']['order_by']='dynList3_id ASC ';


$config['dynList3']['reference_title']='DynamicListThreeValues';
$config['dynList3']['reference_title_min']='DynamicListThreeValues';
		
$config['dynList3']['entity_label_plural']='DynamicListThreeValues';
$config['dynList3']['entity_label']='DynamicListThreeValues';


$config['dynList3']['links'][ 'edit']=array(
	   			'label'=>'Edit',
	   			'title'=>'Edit ',
	   			'on_list'=>False,
	   			'on_view'=>True
	   	);

$config['dynList3']['links'][ 'view']=array(
	   			'label'=>'View',
	   			'title'=>'View',
	   			'on_list'=>True,
	   			'on_view'=>True
	   	);
	   	
$config['dynList3']['fields']['dynList3_id']=array(
			   	'field_title'=>'#',
			   	'field_type'=>'int',
			   	'field_size'=>11,
			   	'field_value'=>'auto_increment',					   	
			   	'default_value'=>'auto_increment',
			   	//to clean
			   	'on_add'=>'hidden',
			   	'on_edit'=>'hidden',
			   	'on_list'=>'show',
			   	'on_view'=>'hidden'
			   	);
$config['dynList3']['fields']['parent_field_id']=array(
					   	'category_type'=>'ParentExternalKey',
					   	'field_title'=>'Parent',
					   	'field_type'=>'int',
					   	//'field_value'=>'normal',
					   	'field_size'=>11,
					   	'mandatory'=>' mandatory ',
					   	'input_type'=>'select',
					   	'input_select_source'=>'table',
					   	'input_select_values'=>'classification',
					   	//to clean
					   	'compute_result'=>'no',							   
					   	'on_add'=>'hidden',
					   	'on_edit'=>'hidden',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
				);
				
$config['dynList3']['fields'][ 'dynList3']=array( 		
	'category_type'=>'IndependantDynamicCategory',		
 	'field_title'=>'DynamicListThreeValues',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'1',//a verifier
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'DynamicListThreeValues',
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 				);
 	$reference_tables['DynamicListThreeValues']['ref_name'] ='DynamicListThreeValues';   
 	$initial_values=array();
 	if(empty($reference_tables['DynamicListThreeValues']['values'])){
 		$reference_tables['DynamicListThreeValues']['values'] =	$initial_values;
 	}else{
 		foreach ($initial_values as $key => $value) {
 			if (!in_array($value, $reference_tables['DynamicListThreeValues']['values'] )){
 				array_push($reference_tables['DynamicListThreeValues']['values'],$value);
 			}
 		}		
 	}
 		
 		
$config['dynList3']['fields']['dynList3_active']=array(
					   	'field_title'=>'Active',
					   	'field_type'=>'int',
					   	'field_size'=>'1',
					   	'field_value'=>'1',
					   	//to clean				
					   	'on_add'=>'not_set',
					   	'on_edit'=>'not_set',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
			);
$config['dynList3']['operations']=array();
			
$config['classification']['fields'][ 'dynList3']=array( 		
	'category_type'=>'WithMultiValues',		
 	'field_title'=>'DynamicListThreeValues',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'2',//a verifier
 	
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'dynList3',
 	'input_select_key_field'=>'parent_field_id',
 	'input_select_source_type'=>'normal',
 	'multi-select' => 'Yes',
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'compute_result'=>'no',
 	'on_list'=>'show'				
 				);			

$config['classification']['fields'][ 'dynListMulti']=array( 		
	'category_type'=>'IndependantDynamicCategory',		
 	'field_title'=>'DynamicListMulti',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'0',//a verifier
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'DynamicListThreeValues',
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 				);
 	$reference_tables['DynamicListThreeValues']['ref_name'] ='DynamicListThreeValues';   
 	$initial_values=array();
 	array_push($initial_values, "MultiVal1");
 	array_push($initial_values, "MultiVal2");
 	array_push($initial_values, "MultiVal3");
 	array_push($initial_values, "MultiVal4");
 	if(empty($reference_tables['DynamicListThreeValues']['values'])){
 		$reference_tables['DynamicListThreeValues']['values'] =	$initial_values;
 	}else{
 		foreach ($initial_values as $key => $value) {
 			if (!in_array($value, $reference_tables['DynamicListThreeValues']['values'] )){
 				array_push($reference_tables['DynamicListThreeValues']['values'],$value);
 			}
 		}		
 	}
 		
 		
$config['classification']['fields'][ 'listOne']=array( 		
	'category_type'=>'StaticCategory',		
 	'field_title'=>'ListOne',	
 	'field_type'=>'text',
 	//'field_value'=>'normal',
 	'number_of_values'=>'1',// a  verifier
 	'mandatory'=>' mandatory ',
 	'field_size'=>20,
 	'input_type'=>'select',
 	'input_select_source'=>'array',
 	'input_select_values'=>array(
 	'value 1'=>"value 1",
 	'value 2'=>"value 2",
 	),
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   
 		
 		
$config['listThree']['table_name']='listThree';
$config['listThree']['table_id']='listThree_id';
$config['listThree']['table_active_field']='listThree_active';
$config['listThree']['main_field']='listThree';
$config['listThree']['order_by']='listThree_id ASC ';


$config['listThree']['reference_title']='List Three Values';
$config['listThree']['reference_title_min']='List Three Values';
		
$config['listThree']['entity_label_plural']='List Three Values';
$config['listThree']['entity_label']='List Three Values';


$config['listThree']['links'][ 'edit']=array(
	   			'label'=>'Edit',
	   			'title'=>'Edit ',
	   			'on_list'=>False,
	   			'on_view'=>True
	   	);

$config['listThree']['links'][ 'view']=array(
	   			'label'=>'View',
	   			'title'=>'View',
	   			'on_list'=>True,
	   			'on_view'=>True
	   	);
	   	
$config['listThree']['fields']['listThree_id']=array(
			   	'field_title'=>'#',
			   	'field_type'=>'int',
			   	'field_size'=>11,
			   	'field_value'=>'auto_increment',					   	
			   	'default_value'=>'auto_increment',
			   	//to clean
			   	'on_add'=>'hidden',
			   	'on_edit'=>'hidden',
			   	'on_list'=>'show',
			   	'on_view'=>'hidden'
			   	);
$config['listThree']['fields']['parent_field_id']=array(
					   	'category_type'=>'ParentExternalKey',
					   	'field_title'=>'Parent',
					   	'field_type'=>'int',
					   	//'field_value'=>'normal',
					   	'field_size'=>11,
					   	'mandatory'=>' mandatory ',
					   	'input_type'=>'select',
					   	'input_select_source'=>'table',
					   	'input_select_values'=>'classification',
					   	//to clean
					   	'compute_result'=>'no',							   
					   	'on_add'=>'hidden',
					   	'on_edit'=>'hidden',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
				);
				
$config['listThree']['fields'][ 'listThree']=array( 		
	'category_type'=>'StaticCategory',		
 	'field_title'=>'List Three Values',	
 	'field_type'=>'text',
 	//'field_value'=>'normal',
 	'number_of_values'=>'1',// a  verifier
 	'field_size'=>20,
 	'input_type'=>'select',
 	'input_select_source'=>'array',
 	'input_select_values'=>array(
 	'ListThreeVal1'=>"ListThreeVal1",
 	'ListThreeVal2'=>"ListThreeVal2",
 	'ListThreeVal3'=>"ListThreeVal3",
 	'ListThreeVal4'=>"ListThreeVal4",
 	'ListThreeVal5'=>"ListThreeVal5",
 	),
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   
 		
 		
$config['listThree']['fields']['listThree_active']=array(
					   	'field_title'=>'Active',
					   	'field_type'=>'int',
					   	'field_size'=>'1',
					   	'field_value'=>'1',
					   	//to clean				
					   	'on_add'=>'not_set',
					   	'on_edit'=>'not_set',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
			);
$config['listThree']['operations']=array();
			
$config['classification']['fields'][ 'listThree']=array( 		
	'category_type'=>'WithMultiValues',		
 	'field_title'=>'List Three Values',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'3',//a verifier
 	
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'listThree',
 	'input_select_key_field'=>'parent_field_id',
 	'input_select_source_type'=>'normal',
 	'multi-select' => 'Yes',
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'compute_result'=>'no',
 	'on_list'=>'show'				
 				);			

$config['classification']['fields'][ 'listMulti']=array( 		
	'category_type'=>'StaticCategory',		
 	'field_title'=>'List Multi Values',	
 	'field_type'=>'text',
 	//'field_value'=>'normal',
 	'number_of_values'=>'0',// a  verifier
 	'field_size'=>20,
 	'input_type'=>'select',
 	'input_select_source'=>'array',
 	'input_select_values'=>array(
 	'ListThreeVal1'=>"ListThreeVal1",
 	'ListThreeVal2'=>"ListThreeVal2",
 	'ListThreeVal3'=>"ListThreeVal3",
 	'ListThreeVal4'=>"ListThreeVal4",
 	'ListThreeVal5'=>"ListThreeVal5",
 	),
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   
 		
 		
$config['superCategory']['table_name']='superCategory';
$config['superCategory']['table_id']='superCategory_id';
$config['superCategory']['table_active_field']='superCategory_active';
$config['superCategory']['main_field']='superCategory';
$config['superCategory']['order_by']='superCategory_id ASC ';


$config['superCategory']['reference_title']='SuperCategory';
$config['superCategory']['reference_title_min']='SuperCategory';
		
$config['superCategory']['entity_label_plural']='SuperCategory';
$config['superCategory']['entity_label']='SuperCategory';


$config['superCategory']['links'][ 'edit']=array(
	   			'label'=>'Edit',
	   			'title'=>'Edit ',
	   			'on_list'=>False,
	   			'on_view'=>True
	   	);

$config['superCategory']['links'][ 'view']=array(
	   			'label'=>'View',
	   			'title'=>'View',
	   			'on_list'=>True,
	   			'on_view'=>True
	   	);
	   	
$config['superCategory']['fields']['superCategory_id']=array(
			   	'field_title'=>'#',
			   	'field_type'=>'int',
			   	'field_size'=>11,
			   	'field_value'=>'auto_increment',					   	
			   	'default_value'=>'auto_increment',
			   	//to clean
			   	'on_add'=>'hidden',
			   	'on_edit'=>'hidden',
			   	'on_list'=>'show',
			   	'on_view'=>'hidden'
			   	);
$config['superCategory']['fields']['parent_field_id']=array(
					   	'category_type'=>'ParentExternalKey',
					   	'field_title'=>'Parent',
					   	'field_type'=>'int',
					   	//'field_value'=>'normal',
					   	'field_size'=>11,
					   	'mandatory'=>' mandatory ',
					   	'input_type'=>'select',
					   	'input_select_source'=>'table',
					   	'input_select_values'=>'classification',
					   	//to clean
					   	'compute_result'=>'no',							   
					   	'on_add'=>'hidden',
					   	'on_edit'=>'hidden',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
				);
				
$config['superCategory']['fields'][ 'superCategory']=array( 		
	'category_type'=>'IndependantDynamicCategory',		
 	'field_title'=>'SuperCategory',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'1',//a verifier
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'SuperCategory',
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 				);
 	$reference_tables['SuperCategory']['ref_name'] ='SuperCategory';   
 	$initial_values=array();
 	array_push($initial_values, "Super1");
 	array_push($initial_values, "Super2");
 	array_push($initial_values, "Super3");
 	array_push($initial_values, "Super4");
 	if(empty($reference_tables['SuperCategory']['values'])){
 		$reference_tables['SuperCategory']['values'] =	$initial_values;
 	}else{
 		foreach ($initial_values as $key => $value) {
 			if (!in_array($value, $reference_tables['SuperCategory']['values'] )){
 				array_push($reference_tables['SuperCategory']['values'],$value);
 			}
 		}		
 	}
 		
 		

$config['superCategory']['fields'][ 'subSimple']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'subCategoryOne',
 	'input_type'=>'text',
 	'field_size'=>100,
 	'field_type'=>'text',
 	//'number_of_values'=>'',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	
 	'mandatory'=>' mandatory ',

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	

$config['superCategory']['fields'][ 'subInt']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'subCategoryInt',
 	'input_type'=>'text',
 	'field_size'=>20,
 	'field_type'=>'int',
 	//'number_of_values'=>'1',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	
$config['superCategory']['fields']['superCategory_active']=array(
					   	'field_title'=>'Active',
					   	'field_type'=>'int',
					   	'field_size'=>'1',
					   	'field_value'=>'1',
					   	//to clean				
					   	'on_add'=>'not_set',
					   	'on_edit'=>'not_set',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
			);
$config['superCategory']['operations']=array();
			
$config['classification']['fields'][ 'superCategory']=array( 		
	'category_type'=>'WithMultiValues',		
 	'field_title'=>'SuperCategory',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'3',//a verifier
 	
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'superCategory',
 	'input_select_key_field'=>'parent_field_id',
 	'input_select_source_type'=>'drill_down',
 	//'number_of_values'=>'*',//a verifier
 	'on_add'=>'drill_down',
 	'on_edit'=>'drill_down',
 	'compute_result'=>'no',
 	'on_list'=>'show'				
 				);			

$config['dependentList']['table_name']='dependentList';
$config['dependentList']['table_id']='dependentList_id';
$config['dependentList']['table_active_field']='dependentList_active';
$config['dependentList']['main_field']='dependentList';
$config['dependentList']['order_by']='dependentList_id ASC ';


$config['dependentList']['reference_title']='dependentList';
$config['dependentList']['reference_title_min']='dependentList';
		
$config['dependentList']['entity_label_plural']='dependentList';
$config['dependentList']['entity_label']='dependentList';


$config['dependentList']['links'][ 'edit']=array(
	   			'label'=>'Edit',
	   			'title'=>'Edit ',
	   			'on_list'=>False,
	   			'on_view'=>True
	   	);

$config['dependentList']['links'][ 'view']=array(
	   			'label'=>'View',
	   			'title'=>'View',
	   			'on_list'=>True,
	   			'on_view'=>True
	   	);
	   	
$config['dependentList']['fields']['dependentList_id']=array(
			   	'field_title'=>'#',
			   	'field_type'=>'int',
			   	'field_size'=>11,
			   	'field_value'=>'auto_increment',					   	
			   	'default_value'=>'auto_increment',
			   	//to clean
			   	'on_add'=>'hidden',
			   	'on_edit'=>'hidden',
			   	'on_list'=>'show',
			   	'on_view'=>'hidden'
			   	);
$config['dependentList']['fields']['parent_field_id']=array(
					   	'category_type'=>'ParentExternalKey',
					   	'field_title'=>'Parent',
					   	'field_type'=>'int',
					   	//'field_value'=>'normal',
					   	'field_size'=>11,
					   	'mandatory'=>' mandatory ',
					   	'input_type'=>'select',
					   	'input_select_source'=>'table',
					   	'input_select_values'=>'classification',
					   	//to clean
					   	'compute_result'=>'no',							   
					   	'on_add'=>'hidden',
					   	'on_edit'=>'hidden',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
				);
				
$config['dependentList']['fields'][ 'dependentList']=array( 		
	'category_type'=>'DependentDynamicCategory',		
 	'field_title'=>'dependentList',	
 	'field_type'=>'int',
 	'field_size'=>11,
 //	'field_value'=>'normal',
 	'number_of_values'=>'1',
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'superCategory',//? corriger seul les category sur le root sont support?s pour le moment
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'compute_result'=>'no',
 	'on_list'=>'show'				
 				);
 	
 		
 		
$config['dependentList']['fields']['dependentList_active']=array(
					   	'field_title'=>'Active',
					   	'field_type'=>'int',
					   	'field_size'=>'1',
					   	'field_value'=>'1',
					   	//to clean				
					   	'on_add'=>'not_set',
					   	'on_edit'=>'not_set',
					   	'on_list'=>'hidden',
					   	'on_view'=>'hidden'
			);
$config['dependentList']['operations']=array();
			
$config['classification']['fields'][ 'dependentList']=array( 		
	'category_type'=>'WithMultiValues',		
 	'field_title'=>'dependentList',	
 	'field_type'=>'int',
 	'field_size'=>11,
 	//'field_value'=>'normal',
 	'number_of_values'=>'2',//a verifier
 	
 	
 	'input_type'=>'select',
 	'input_select_source'=>'table',
 	'input_select_values'=>'dependentList',
 	'input_select_key_field'=>'parent_field_id',
 	'input_select_source_type'=>'normal',
 	'multi-select' => 'Yes',
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'compute_result'=>'no',
 	'on_list'=>'show'				
 				);			


$config['classification']['fields'][ 'note']=array( 		
	'category_type'=>'FreeCategory',		
 	'field_title'=>'Note',
 	'input_type'=>'text',
 	'field_size'=>500,
 	'field_type'=>'text',
 	'input_type'=>'textarea',
 	//'number_of_values'=>'1',//a verifier
 	'number_of_values'=>'1',//tous les Freecategory ont une seule valeur
 	//'field_value'=>'normal',
 	

 	'pattern'=>'',
 	
 	'initial_value'=>'',
 	'field_value'=>'',
 	
 	'on_add'=>'enabled',
 	'on_edit'=>'enabled',
 	'on_list'=>'show'				
 	);   	

// end project specific area
$config['classification']['fields']['user_id']=array(
		'field_title'=>'Classified by',
		'field_type'=>'int',
		'field_size'=>11,
		'field_value'=>active_user_id(),
		'input_type'=>'select',
		'input_select_source'=>'table',
		'input_select_values'=>'users;user_name',
		'mandatory'=>' mandatory ',
		
		'on_add'=>'hidden',		
		'on_edit'=>'hidden',		
		'on_list'=>'hidden',		
		'on_view'=>'hidden'
);

$config['classification']['fields']['classification_time']=array(
		'field_title'=>'Classification time',
		'field_type'=>'time',
		'default_value'=>'CURRENT_TIMESTAMP',
		'field_value'=>bm_current_time('Y-m-d H:i:s'),		 
		'field_size'=>20,
		'mandatory'=>' mandatory ',
		
		'on_add'=>'not_set',
		'on_edit'=>'not_set',
		'on_list'=>'hidden',
		'on_view'=>'hidden'
);

$config['classification']['fields']['class_active']=array(
	   			'field_title'=>'Active',
	   			'field_type'=>'int',
	   			'field_size'=>'1',
	   			
	   			//'field_value'=>'normal',
	   			'on_add'=>'not_set',
	   			'on_edit'=>'not_set',
	   			'on_list'=>'hidden',
				'on_view'=>'hidden'
	   	);
$config['classification']['operations']=array();
$result[ 'config' ] =$config;

$result[ 'reference_tables' ] =$reference_tables;

//QA area

 		
$qa=array();
$qa['cutt_off_score']='3';
$qa['questions']=array();
  array_push($qa['questions'], array(
  										'title' =>"Question 1",
  										)
  );
  array_push($qa['questions'], array(
  										'title' =>"Question 2",
  										)
  );
  array_push($qa['questions'], array(
  										'title' =>"Question 3",
  										)
  );
  array_push($qa['questions'], array(
  										'title' =>"Question 4",
  										)
  );
$qa['responses']=array();
   array_push($qa['responses'], array(
   										'title' =>"Answer1",
   										'score' =>"3",
   										)
   );
   array_push($qa['responses'], array(
   										'title' =>"Answer2",
   										'score' =>"1.5",
   										)
   );
   array_push($qa['responses'], array(
   										'title' =>"Answer3",
   										'score' =>"0",
   										)
   );
$result[ 'qa' ]=$qa; 		

//QA area


//SCREENING area

 		
$screening=array();
$screening['review_per_paper']='2';
$screening['conflict_type']='ExclusionCriteria';
$screening['conflict_resolution']='Unanimity';
$screening['validation_assigment_mode']='Normal';
$screening['validation_percentage']='30';
$screening['exclusion_criteria']=array();
array_push($screening['exclusion_criteria'], "Criteria 1");
array_push($screening['exclusion_criteria'], "Criteria 2");
$screening['source_papers']=array();
array_push($screening['source_papers'], "Source 1");
array_push($screening['source_papers'], "Source 2");
$screening['source_papers']=array();
array_push($screening['source_papers'], "Source 1");
array_push($screening['source_papers'], "Source 2");
$screening['phases']=array();
 array_push($screening['phases'], array(
 										'title' =>"Phase 1",
 										'description' =>"Screen per title",
 										'fields'=>'Title|',
 										)
 );
 array_push($screening['phases'], array(
 										'title' =>"Phase 2",
 										'description' =>"Screen per title and abstract 2",
 										'fields'=>'Title|Abstract|Link|',
 										)
 );

$result[ 'screening' ]=$screening; 		

//SCREENING area

//REPORTING
$report=array();
$report['listOne']['type']='simple';
$report['listOne']['title']='Domain'; 		
$report['listOne']['id']='listOne';
$report['listOne']['link']='false'; 
$report['listOne']['values']['field']='listOne';
$report['listOne']['values']['style']='select';
$report['listOne']['values']['title']='ListOne';  
$charts=array();
 	array_push($charts, "pie");
 	array_push($charts, "bar");
$report['listOne']['chart']=$charts;
$report['list_dyn']['type']='compare';
$report['list_dyn']['title']='DynamicListOne per ListOne'; 		
$report['list_dyn']['id']='list_dyn';
$report['list_dyn']['link']='false'; 
$report['list_dyn']['values']['field']='dynListOne';
$report['list_dyn']['values']['style']='select';
$report['list_dyn']['values']['title']='DynamicListOne';  
$report['list_dyn']['reference']['field']='listOne';
 $report['list_dyn']['reference']['style']='select';
 $report['list_dyn']['reference']['title']='ListOne';  
  $charts=array();
   	array_push($charts, "line");
   	array_push($charts, "bar");
  $report['list_dyn']['chart']=$charts;
$result[ 'report' ]=$report; 		
//REPORTING

return $result;
}
