const Iota = require('@iota/core');
const Converter = require('@iota/converter');
const Mam = require('@iota/mam')
const generate = require('iota-generate-seed');



// Connessione al nodo
const iota = Iota.composeAPI({
  provider: 'https://nodes.devnet.iota.org:443'
});


let mamState = Mam.init(iota.provider);

const seed = generate();

  const publish = (json, tag) => {

    const depth = 3;
    const minimumWeightMagnitude = 9;

    const transfers = prepareMAMMessage(json, tag);

    iota
      .prepareTransfers(seed, transfers)
      .then(trytes => {
        return iota.sendTrytes(trytes, depth, minimumWeightMagnitude);
      })
      .then(bundle => {
        console.log("hash della transazione: " + bundle[0].hash);
        console.log("hash della transazione: " + bundle[1].hash);
        console.log("hash della transazione: " + bundle[2].hash);
      })
      .catch(err => {
        console.error(err)
      });
  }


  const prepareMAMMessage = (json, jsonTag) => {  
    const trytes = Converter.asciiToTrytes(JSON.stringify(json));
    const tag = Converter.asciiToTrytes(JSON.stringify(jsonTag));
    const message = Mam.create(mamState, trytes);
    mamState = message.state;
    console.log("root del messaggio MAM: " + message.root);
    console.log("address del messaggio: " + message.address);
    return [
      {
        address: message.address,
        value: 0,
        message: message.payload,
        tag: tag
      }
    ];
  };
  

const getRandomTag = () => {
var tag = ["temperature","weather", "pollution", "science", "industry", "sport", "living", "traffic"];
return tag[Math.floor(Math.random()*tag.length)];
}




const json = {
  "name" : "prova mam channel"
};

publish(json, getRandomTag());