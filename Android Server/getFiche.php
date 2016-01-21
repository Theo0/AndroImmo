<?php
require_once 'connectDB.php';
$req = $db->prepare("SELECT * FROM FICHES WHERE ID ='".$_GET['id']."'");
if($req->execute() !== TRUE){
  echo "Erreur récupération des fiches";
} else{
  $result = $req->fetchAll(PDO::FETCH_ASSOC);
	echo (json_encode($result));
}
?>
