const fs = require('fs');
const path = require('path');
const xpath = require('xpath');
const xml2js = require('xml2js');
const axios = require('axios');
const { v4: uuidv4 } = require('uuid');

const diretorioBase = './';

//npm install xpath axios uuid

async function processarArquivo(arquivo) {
  const xml = fs.readFileSync(arquivo, 'utf-8');

  let nome = '';
  let ie = '';
  let cMunNode = '';
  let xNomeMun = '';
  let cnpj = '';


  xml2js.parseString(xml, (err, result) => {
    if (err) {
      console.error('Erro ao processar o XML:', err);
      return;
    }

    emitente = result.NFCom.infNFCom[0].emit[0];
    cnpj = emitente.CNPJ[0];
    nome = emitente.xNome[0];
    ie = emitente.IE[0];
    cMunNode = emitente.enderEmit[0].cMun[0];
    xNomeMun = emitente.enderEmit[0].xMun[0];

    console.log(`CNPJ: ${cnpj}`);
    console.log(`xNome: ${nome}`);
    console.log(`IE: ${ie}`);
    console.log(`cMun: ${cMunNode}`);
    console.log(`xMun: ${xNomeMun}`);

  });


  const cnpjPrimeirosNoveDigitos = cnpj.slice(0, 8);

  const meuObjetoJSON = {
    "requestId": uuidv4(),
    "idEmpresa": 1,
    "razaoSocial": nome,
    "nomeFantasia": nome,
    "nomeExibicao": nome,
    "nomeFantasiaNormalizado": nome,
    "razaoSocialNormalizado": nome,
    "nomeExibicaoNormalizado": nome,
    "filialId": ie.replace(/[^0-9]/g, ''),
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

  try {
    console.log(`Enviando ======> ${arquivo}`);
    const response = await axios.post('http://localhost:7085/request/', meuObjetoJSON, {'Content-Type': 'application/json'});
    console.log(`Enviado: ${arquivo}`);
  } catch (error) {
    console.error(`Erro ao enviar ${arquivo}: ${error}`);
  }

}

async function processarDiretorio(diretorio) {
  const arquivos = fs.readdirSync(diretorio);
  for (const arquivo of arquivos) {
    const caminhoCompleto = path.join(diretorio, arquivo);
    if (fs.statSync(caminhoCompleto).isDirectory()) {
      await processarDiretorio(caminhoCompleto);
    } else {
      if (arquivo.endsWith('.xml')) {
        await processarArquivo(caminhoCompleto);
      }
    }
  }
}

processarDiretorio(diretorioBase)
  .then(() => {
    console.log(`---- Fim do processamento! ----`);
  })
  .catch(error => {
    console.error('Erro ao processar diretório:', error);
  });
