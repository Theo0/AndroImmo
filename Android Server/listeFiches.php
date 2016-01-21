<?php
require_once 'connectDB.php';

if($_REQUEST['ville'] == "0") {
  $req = $db->prepare('SELECT ID, NOM, VILLE, NBPIECES, SURFACE FROM FICHES WHERE NBPIECES >= :piecesMin AND NBPIECES <= :piecesMax AND SURFACE >= :tailleMin AND SURFACE <= :tailleMax');
} else {
    $req = $db->prepare('SELECT ID, NOM, VILLE, NBPIECES, SURFACE FROM FICHES WHERE NBPIECES >= :piecesMin AND NBPIECES <= :piecesMax AND SURFACE >= :tailleMin AND SURFACE <= :tailleMax AND VILLE = :ville');
    $req->bindParam(':ville', $_REQUEST['ville'], PDO::PARAM_STR);
}

$max = 999;

if($_REQUEST['piecesMax'] == 0){
  $req->bindParam(':piecesMax', $max, PDO::PARAM_INT);
} else {
  $req->bindParam(':piecesMax', $_REQUEST['piecesMax'], PDO::PARAM_INT);
}


if($_REQUEST['tailleMax'] == 0){
  $req->bindParam(':tailleMax', $max, PDO::PARAM_INT);
} else {
  $req->bindParam(':tailleMax', $_REQUEST['tailleMax'], PDO::PARAM_INT);
}

$req->bindParam(':tailleMin', $_REQUEST['tailleMin'], PDO::PARAM_INT);
$req->bindParam(':piecesMin', $_REQUEST['piecesMin'], PDO::PARAM_INT);

if($req->execute() !== TRUE){
  echo "Erreur récupération des fiches";
} else{
  $result = $req->fetchAll(PDO::FETCH_ASSOC);
	echo (json_encode($result));
}
?>
