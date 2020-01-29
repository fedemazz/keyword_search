const Mam = require('@iota/mam')
const { asciiToTrytes, trytesToAscii } = require('@iota/converter')
var readline = require('readline-sync');

//root channel mam
rootMam = readline.question("Inserisci la root: ");

async function initMam() {
  console.log("In ascolto...");
  await Mam.init('https://nodes.devnet.iota.org:443');
}


async function fetch() {
const data = await Mam.fetchSingle(rootMam, 'public', null);
console.log(trytesToAscii(data.payload));
}


initMam()
fetch();

//KDTO9WPPQOMXSFGGYEUVWBHWVFTMFWUP9AIAGKZIUZENXBCUWMQZQFAAKMVLJTQ9VNAZ9QFXIUITDCQWG