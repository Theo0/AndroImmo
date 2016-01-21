
<?php
require_once 'connectDB.php';

$req = $db->prepare("SELECT * FROM FICHES");
if($req->execute() == TRUE){
  $result = $req->fetchAll(PDO::FETCH_ASSOC);
  var_dump($result);

} else{

  echo "Erreur";
}

?>
