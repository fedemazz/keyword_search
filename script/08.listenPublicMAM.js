//il seguente codice permette di rimanere in ascolto su un canale MAM partendo da un root
//il root è l'identificatore del MAM ed ogni volta che viene trovato un nuovo mam, la root cambia 


const Mam = require('@iota/mam')
const { asciiToTrytes, trytesToAscii } = require('@iota/converter')

//root channel mam
root = 'CPQKYUN9GDIHNJPYVSMDXIKWJBEJQRAUMDJNLWAO9BJAFVLPVVCYMZEOKRYUWCARLYEOHEPKISZPFYJOM'

async function initMam() {
  console.log("In ascolto...");
  await Mam.init('https://nodes.devnet.iota.org:443');
}

//controllo ogni 5 secondi se sono presenti nuovi messaggi nel canale
async function checkMam() {

  // The showData callback will be called in order for each message found
  const data = await Mam.fetch(root, 'public', null, showData);
  //la nuova root, cioè l'indirizzo del prossimo messaggio è contenuto nel ritorno della funzione sopra (Mam.fetch)
  root = data.nextRoot;
  //richiamo la funzione ogni 5 secondi
  console.log("In ascolto...");
  setTimeout(checkMam, 5000);
}

//funzione per visualizzare i dati a schermo
const showData = raw => {
    const data = trytesToAscii(raw)
    console.log(data);
  }

//chiamo init e check
initMam()
checkMam()