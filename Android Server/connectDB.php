<?php try{
  $db = new PDO('mysql:host=sql-1;dbname=theochamb1_immo','theochamb1','Ds6os1p4m7xZ');
}catch(Exception $e)
{
	die('Erreur : '.$e->getMessage());
}
?>
