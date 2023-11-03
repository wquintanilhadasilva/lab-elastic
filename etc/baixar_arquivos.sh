#!/bin/bash

# Lista de CNPJs
CNPJ_LIST=(
  # Adicione outros CNPJs aqui se necessário
)

# Loop através da lista de CNPJs
for CNPJ in "${CNPJ_LIST[@]}"
do
  # Executa o comando curl com o CNPJ atual
  curl "http://localhost:8080/baixador?qtd=10000&emi=${CNPJ}" \
    -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7" \
    -H "Accept-Language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7" \
    -H "Authorization: Basic xxxxxxxx" \
    -H "Connection: keep-alive" \
    -H "Referer: http://geradorweb.oobj.com.br/gerador-web/" \
    --compressed \
    --insecure \
    -o "${CNPJ}.zip" # Salva o arquivo com o nome do CNPJ
done
