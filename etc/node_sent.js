const fs = require('fs');
const path = require('path');
const xpath = require('xpath');
const dom = require('xmldom').DOMParser;
const axios = require('axios');
const { v4: uuidv4 } = require('uuid');

const diretorioBase = '/xmls';

//npm install xpath axios uuid

function processarArquivo(arquivo) {
  const xml = fs.readFileSync(arquivo, 'utf-8');
  const doc = new dom().parseFromString(xml);
  const cnpjNode = xpath.select1('//nfe:CNPJ', doc, true, { nfe: 'http://www.portalfiscal.inf.br/nfcom' });
  const cnpj = cnpjNode.firstChild.data;

  const payload = JSON.stringify({
    emissor: cnpj,
    payload: xml,
    requestid: uuidv4(),
    uuid: uuidv4()
  });

  axios.post('http://localhost:8079/enviar', payload)
    .then((response) => {
      console.log(`Enviado: ${arquivo}`);
    })
    .catch((error) => {
      console.error(`Erro ao enviar ${arquivo}: ${error}`);
    });
}

function processarDiretorio(diretorio) {
  const arquivos = fs.readdirSync(diretorio);
  arquivos.forEach((arquivo) => {
    const caminhoCompleto = path.join(diretorio, arquivo);
    if (fs.statSync(caminhoCompleto).isDirectory()) {
      processarDiretorio(caminhoCompleto);
    } else {
      if (arquivo.endsWith('.xml')) {
        processarArquivo(caminhoCompleto);
      }
    }
  });
}

processarDiretorio(diretorioBase);
