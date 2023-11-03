# Caminho da pasta que contém os arquivos zip
$dir = "c:\tmp\xmls"

# Lista todos os arquivos zip na pasta
$files = Get-ChildItem -Path $dir -Filter "*.zip"

# Para cada arquivo zip
foreach ($file in $files) {

    # Obtém o nome do arquivo zip
    $filename = $file.Name

    # Cria um subdiretório com o nome do arquivo zip sem a extensão
    $destinationPath = $dir + "\" + $filename.Substring(0, $filename.Length - 4)

    # Tenta descompactar o arquivo zip para o subdiretório
    try {
        Expand-Archive -Path $file.FullName -DestinationPath $destinationPath
    }
    # Se ocorrer um erro, registra o erro e continua para o próximo arquivo
    catch {
        Write-Error "Erro ao descompactar $filename"
        Write-Error $_.Exception.Message
        Write-Error $_.Exception.GetType().FullName
    }
}
