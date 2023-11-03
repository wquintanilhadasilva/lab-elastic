#!/bin/bash

for arquivo in *; do
    if [[ -f "$arquivo" && "$arquivo" =~ \.zip$ ]]; then
        nome_pasta=$(basename "$arquivo" .zip)
        
        if [ -d "$nome_pasta" ]; then
            echo "A pasta $nome_pasta já existe. Pulando para o próximo arquivo."
            continue
        fi
        
        mkdir "$nome_pasta"

        tamanho=$(unzip -l "$arquivo" | awk 'NR==4{print $1}')
        echo "Descompactando $arquivo..."

        unzip -q "$arquivo" | pv -lep -s $tamanho | tar -xf - -C "$nome_pasta"
        
        if [ $? -eq 0 ]; then
            echo "Arquivo $arquivo descompactado com sucesso."
        else
            echo "Erro ao descompactar o arquivo $arquivo."
        fi
    fi
done
