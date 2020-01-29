//serie di file js in cui passo per passo inizio con IOTA

//nel seguente file vado a leggere sulla rete devNet Tangle la transazione precedentemente fatta

const Iota = require('@iota/core');
const Extract = require('@iota/extract-json');
const Converter = require('@iota/converter');
var readline = require('readline-sync');

// Connect to a node
const iota = Iota.composeAPI({
  provider: 'https://nodes.devnet.thetangle.org:443'
});

// Define the bundle hash for your transaction


  function askToUser() {
    var tag = readline.question("Inserisci qui il tag di ricerca: ");
    tag = Converter.asciiToTrytes(tag);
    console.log(tag)
    query({
      field: tag
    })
    }

// Get the transaction in your bundle
const query = async packet => {
iota.findTransactionObjects({ tags:  [packet.field]})
  .then(bundle => {
    //solo value transazione
    //console.log(JSON.parse(Extract.extractJson(bundle)));
    //tutta la transazione
    console.log(bundle);
  })
  .catch(err => {
    console.error(err);
  });
}


askToUser();