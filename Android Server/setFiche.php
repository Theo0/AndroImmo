<?php
error_reporting(E_ALL);
ini_set('display_errors', 'On');
require_once 'connectDB.php';
if( isset($_REQUEST["json"]) ) {
     $data = json_decode($_REQUEST["json"], true);
     $stmt = $db->prepare("INSERT INTO FICHES (NOM, SURFACE, NBPIECES, NBCHAMBRES, NBSDB, NBWC, NBBALCON, ETAGES, ADR, VILLE, EXPO, TAXE, COPRO, NOTES, LAT, LON, IMG1, IMG2, IMG3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
     $stmt->bindParam(1, $data["NOM"]);
     $stmt->bindParam(2, $data["SURFACE"]);
     $stmt->bindParam(3, $data["NBPIECES"]);
     $stmt->bindParam(4, $data["NBCHAMBRES"]);
     $stmt->bindParam(5, $data["NBSDB"]);
     $stmt->bindParam(6, $data["NBWC"]);
     $stmt->bindParam(7, $data["NBBALCON"]);
     $stmt->bindParam(8, $data["ETAGES"]);
     $stmt->bindParam(9, $data["ADR"]);
     $stmt->bindParam(10, $data["VILLE"]);
     $stmt->bindParam(11, $data["EXPO"]);
     $stmt->bindParam(12, $data["TAXE"]);
     $stmt->bindParam(13, $data["COPRO"]);
     $stmt->bindParam(14, $data["NOTES"]);
     $stmt->bindParam(15, $data["LAT"]);
     $stmt->bindParam(16, $data["LON"]);
     $stmt->bindParam(17, $data["IMG1"]);
     $stmt->bindParam(18, $data["IMG2"]);
     $stmt->bindParam(19, $data["IMG3"]);


     if($stmt->execute() == TRUE){
       echo "OK";
     } else {
       echo "REQUETE PAS DU TOUT OK";
     }

     //19
     /*$req = $db->prepare("UPDATE FICHES SET ID=22 WHERE ID=1");
     if($req->execute() !== TRUE){
       echo "Erreur récupération des fiches";
     }else{
     	echo $json;
    }*/
}
else {
  echo 0;
}
