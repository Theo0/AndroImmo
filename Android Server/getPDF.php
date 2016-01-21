
<?php
require('fpdf.php');
require_once 'connectDB.php';

$req = $db->prepare("SELECT * FROM FICHES WHERE ID ='".$_GET['id']."'");
if($req->execute() !== TRUE){
  echo "Erreur lors de la création du PDF. Merci de réessayer plus tard.";
} else{
  $result = $req->fetchAll(PDO::FETCH_ASSOC);
  $_SESSION['TITRE'] = $result[0]['NOM'];

class PDF extends FPDF
{
// En-tête
function Header()
{
    // Police Arial gras 15
    $this->SetFont('Arial','B',15);
    // Décalage à droite
    $this->Cell(80);
    // Titre
    $this->Cell(30,10, $_SESSION['TITRE'],0,0,'C');
    // Saut de ligne
    $this->Ln(20);
}

// Pied de page
function Footer()
{
    // Positionnement à 1,5 cm du bas
    $this->SetY(-15);
    // Police Arial italique 8
    $this->SetFont('Arial','I',8);
    // Numéro de page
    $this->Cell(0,10,'Page '.$this->PageNo().'/{nb}',0,0,'C');
}
}

// Instanciation de la classe dérivée
$pdf = new PDF();
$pdf->AliasNbPages();
$pdf->AddPage();
$pdf->SetFont('Helvetica','',12);

//REMPLISSAGE DU PDF
$pdf->Cell(20, 10, 'Surface : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['SURFACE']." m2", 0, 1);

$pdf->Cell(40, 10, 'Nombre de pièces : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['NBPIECES'], 0, 1);

$pdf->Cell(45, 10, 'Nombre de chambres : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['NBCHAMBRES'], 0, 1);

$pdf->Cell(55, 10, 'Nombre de salles de bains : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['NBSDB'], 0, 1);

$pdf->Cell(33, 10, 'Nombre de WC : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['NBWC'], 0, 1);

$pdf->Cell(65, 10, 'Nombre de balcon ou terrasses : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['NBBALCON'], 0, 1);

$pdf->Cell(17, 10, 'Etages : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['ETAGES'], 0, 1);

$pdf->Cell(12, 10, 'Ville : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['VILLE'], 0, 1);

$pdf->Cell(25, 10, 'Exposition : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['EXPO'], 0, 1);

$pdf->Cell(55, 10, 'Montant de la taxe fonciere : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['TAXE'], 0, 1);

$pdf->Cell(22, 10, 'Charges : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['COPRO'], 0, 1);

$pdf->Cell(40, 10, 'A propos du bien : ', 0, 0);
$pdf->Cell(0, 10, $result[0]['NOTES'], 0, 1);

if(isset($result[0]['IMG1'])){
  $pdf->AddPage();
  $pdf->Cell( 0, 0, $pdf->Image($result[0]['IMG1'], $pdf->GetX(), $pdf->GetY(), 180,0), 0, 1, 'L', false );
}
if(isset($result[0]['IMG2'])){
  $pdf->AddPage();
  $pdf->Cell( 0, 0, $pdf->Image($result[0]['IMG2'], $pdf->GetX(), $pdf->GetY(), 180,0), 0, 1, 'L', false );
}
if(isset($result[0]['IMG3'])){
  $pdf->AddPage();
  $pdf->Cell( 0, 0, $pdf->Image($result[0]['IMG3'], $pdf->GetX(), $pdf->GetY(), 180,0), 0, 1, 'L', false );
}

$pdf->Output();
}
?>
