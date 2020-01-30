const Iota = require('@iota/core');
const Converter = require('@iota/converter');
const Mam = require('@iota/mam')
const fs = require ('fs');
const csv = require ('fast-csv');





// Connessione al nodo
const iota = Iota.composeAPI({
  provider: 'https://nodes.devnet.iota.org:443'
});


//inizializzo il canale mam, il seed è preso casuale
let mamState = Mam.init(iota.provider);

const depth = 3;
const minimumWeightMagnitude = 9;


  const publish = (json, tag) => {


    const mamMessage = prepareMAMMessage(json, tag);
    const transfers = mamMessage.transfers;
    iota
      .prepareTransfers(mamState.seed, transfers)
      .then(trytes => {
        return iota.sendTrytes(trytes, depth, minimumWeightMagnitude);
      })
      .then(bundle => {  
        console.log("root del messaggio MAM: " + mamMessage.info.root);
        console.log("root del messaggio MAM: " + mamMessage.info.tag);
        
        csv
        .writeToStream(fs.createWriteStream("test.csv", 
            {flags: 'a'}),                  
        [
          [mamMessage.info.root, mamMessage.info.tag],
          [""]
        ]
          , {headers:false} )
        /*console.log("hash della transazione: " + bundle[0].hash);
        console.log("hash della transazione: " + bundle[1].hash);
        console.log("hash della transazione: " + bundle[2].hash);*/
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
    return {
      transfers : [{
            address: message.address,
            value: 0,
            message: message.payload,
            tag: tag
            }], 
            info : { root : message.root, 
            tag : jsonTag} 
          };
  };



const init = (n) => {
    //cerco di pubblicare 20 messaggi mam sullo stesso canale
    for (var i = 0; i <20; i++){
      //prendo un tag casuale dal set di keyword
      var randomTag = getRandomTag();
      //creo una transazione mam json d'esempio 
      var json = createJson(i, n, randomTag);
      //pubblico la transazione sul tangle
      publish(json, randomTag);
  }
}

const createJson = (i, n, tag) => {
  return [
    { "mam trans Num:" : i,
      "channel number" : n,
      "tag" : tag 
    }
  ]
}
  

const getRandomTag = () => {
var keySet = ["a","b", "c", "d", "e", "f", "g", "h"];
//rendo key set in ordine casuale
for(let i = keySet.length - 1; i > 0; i--){
  const j = Math.floor(Math.random() * i)
  const temp = keySet[i]
  keySet[i] = keySet[j]
  keySet[j] = temp
}

var tag = "";
for (var i = 0; i < (Math.floor(Math.random()*keySet.length)+1); i++){
  tag = tag+(keySet.pop());
}
return tag;
}

//console.log(getRandomTag());
init(10);