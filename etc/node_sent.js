const fs = require('fs');
const path = require('path');
const xpath = require('xpath');
const dom = require('xmldom').DOMParser;
const axios = require('axios');
const { v4: uuidv4 } = require('uuid');

const diretorioBase = './';

//npm install xpath axios uuid

function processarArquivo(arquivo) {
  const xml = fs.readFileSync(arquivo, 'utf-8');
  const doc = new dom().parseFromString(xml);
  const cnpjNode = xpath.select1('//nfe:CNPJ', doc, true, { nfe: 'http://www.portalfiscal.inf.br/nfcom' });
  const nome = xpath.select1('//nfe:xNome', doc, true, { nfe: 'http://www.portalfiscal.inf.br/nfcom' }).firstChild.data;
  const ie = xpath.select1('//nfe:IE', doc, true, { nfe: 'http://www.portalfiscal.inf.br/nfcom' }).firstChild.data;
  const cMunNode = xpath.select1('//nfe:cMun', doc, true, { nfe: 'http://www.portalfiscal.inf.br/nfcom' }).firstChild.data;
  const xNomeMun = xpath.select1('//nfe:xMun', doc, true, { nfe: 'http://www.portalfiscal.inf.br/nfcom' }).firstChild.data;
  const cnpj = cnpjNode.firstChild.data;

  const cnpjPrimeirosNoveDigitos = cnpj.slice(0, 8);

//  const payload = JSON.stringify({
//    emissor: cnpj,
//    payload: xml,
//    requestid: uuidv4(),
//    uuid: uuidv4()
//  });

  const meuObjetoJSON = {
    "requestId": uuidv4(),
    "idEmpresa": 1,
    "razaoSocial": nome,
    "nomeFantasia": nome,
    "nomeExibicao": nome,
    "nomeFantasiaNormalizado": nome,
    "razaoSocialNormalizado": nome,
    "nomeExibicaoNormalizado": nome,
    "filialId": ie,
    "cnpj": cnpj,
    "filialNomeApresentacao": "Filial 1",
    "filialInscricaoMunicipal": "123456",
    "filialHabilitada": true,
    "idMunicipio": cMunNode,
    "nomeMunicipio": "Cidade Exemplo",
    "siglaUF": "SP",
    "codigoIBGE": cMunNode,
    "nomeMunicipioPesquisa": xNomeMun,
    "cnpjBase": cnpjPrimeirosNoveDigitos,
    "habilitada": true,
    "idConta": cnpjPrimeirosNoveDigitos,
    "nomeConta": "Conta "  + cnpjPrimeirosNoveDigitos,
    "validadeCertificado": "2023-11-03T12:34:56",
    "webhookurl": "http://exemplo.com/webhook",
    "integrationId": uuidv4(),
    "uuid": uuidv4(),
    "requestReceivedDateTime": "2023-11-03T12:34:56",
    "filename": arquivo,
    "contentType": "text/plain",
    "integracao": "XML_SEFAZ",
    "modelo": "NFCOM",
    "documento": "DFE",
    "requestType": "STRING",
    "storageBucket": "bucket-exemplo",
    "storageKey": "chave-exemplo",
    "tipoRequisicao": "STRING",
    "payload": xml
  };

  const payload = JSON.stringify(meuObjetoJSON);

  axios.post('http://localhost:7085/request/', payload)
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
