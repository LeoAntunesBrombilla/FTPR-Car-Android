# Guia de Configuração do Projeto Android - Car API Test

Este guia fornece instruções passo a passo para configurar e testar o projeto Android que integra
Firebase Authentication, Google Maps e API REST de carros.

## 📋 Pré-requisitos

- Android Studio instalado
- Conta Google para acessar Firebase Console e Google Cloud Console
- JDK 8 ou superior
- Dispositivo Android ou emulador para testes

## 🔧 Configuração Inicial

### 1. Clone o Projeto

### 2. Configuração do Firebase

#### 2.1. Criar Projeto no Firebase Console

1. Acesse [Firebase Console](https://console.firebase.google.com/)
2. Clique em "Adicionar projeto" ou "Create a project"
3. Digite o nome do projeto (ex: "Car API Test")
4. Configure o Google Analytics (opcional)
5. Clique em "Criar projeto"

#### 2.2. Configurar Firebase Authentication

1. No Firebase Console, vá para **Authentication** > **Get started**
2. Na aba **Sign-in method**, habilite:
    - **Phone** (Autenticação por telefone)
    - **Google** (opcional, se escolher esta opção)

#### 2.3. Configurar Phone Authentication para Testes

1. Em **Authentication** > **Sign-in method** > **Phone**
2. Role para baixo até **Phone numbers for testing**
3. Adicione o número: `+5511912345678`
4. Código de verificação: `123456`
5. Clique em **Save**

#### 2.4. Configurar Firebase Storage

1. No Firebase Console, vá para **Storage** > **Get started**
2. Escolha **Start in test mode** (para desenvolvimento)
3. Selecione uma localização (ex: us-central1)

#### 2.5. Adicionar App Android ao Firebase

1. No Firebase Console, clique no ícone Android
2. **Package name**: `com.example.myapitest`
3. **App nickname**: "Car API Test" (opcional)
4. **SHA-1**: Execute no terminal do projeto:
   ```bash
   ./gradlew signingReport
   ```
   Copie o SHA-1 de **debug**
5. Clique em **Register app**
6. **IMPORTANTE**: Baixe o arquivo `google-services.json`
7. Coloque o arquivo em: `app/google-services.json`

### 3. Configuração do Google Maps API

#### 3.1. Ativar Google Maps API

1. Acesse [Google Cloud Console](https://console.cloud.google.com/)
2. Selecione o mesmo projeto criado no Firebase
3. Vá para **APIs & Services** > **Library**
4. Procure por "Maps SDK for Android"
5. Clique em **Enable**

#### 3.2. Criar API Key

1. Em **APIs & Services** > **Credentials**
2. Clique em **+ CREATE CREDENTIALS** > **API key**
3. Copie a API key gerada
4. Clique em **RESTRICT KEY** para configurar restrições
5. Em **Application restrictions**, selecione **Android apps**
6. Adicione:
    - **Package name**: `com.example.myapitest`
    - **SHA-1**: o mesmo usado no Firebase
7. Em **API restrictions**, selecione **Maps SDK for Android**
8. Clique em **Save**

#### 3.3. Configurar API Key no Projeto

Abra o arquivo `app/src/main/AndroidManifest.xml` e substitua `TROCAR_AQUI` pela sua API key:

```xml

<meta-data android:name="com.google.android.geo.API_KEY" android:value="SUA_API_KEY_AQUI" />
```

No arquivo `app/src/main/java/com/example/myapitest/data/network/ApiService.kt`, configure a URL
base:

```kotlin
private const val BASE_URL = "http://10.0.2.2:3000/" // Para emulador
// ou
private const val BASE_URL = "http://SEU_IP:3000/" // Para dispositivo real
```

## 🚀 Como Testar o Projeto

### 1. Verificar Arquivos de Configuração

Certifique-se de que os seguintes arquivos estão configurados:

- ✅ `app/google-services.json` (baixado do Firebase)
- ✅ `app/src/main/AndroidManifest.xml` (com API key do Google Maps)
- ✅ URL da API REST configurada no código

### 2. Build e Execução

1. Abra o projeto no Android Studio
2. Aguarde a sincronização do Gradle
3. Execute no emulador ou dispositivo:
   ```bash
   ./gradlew installDebug
   ```

### 3. Testando as Funcionalidades

#### 3.1. Teste de Login com Telefone

1. Na tela de login, digite: `+55 11 91234-5678`
2. Clique em "Enviar código"
3. Digite o código: `123456`
4. Deve fazer login com sucesso

#### 3.2. Teste da API de Carros

1. Após fazer login, navegue para a lista de carros
2. Teste as operações CRUD:
    - **Create**: Adicionar novo carro
    - **Read**: Visualizar lista e detalhes
    - **Update**: Editar informações do carro
    - **Delete**: Remover carro

#### 3.3. Teste do Google Maps

1. Na tela de detalhes do carro, verifique se o mapa carrega
2. Na tela de adicionar/editar carro, teste o seletor de localização

### 4. Upload de Imagens para Firebase Storage

1. Na tela de adicionar carro, selecione uma imagem
2. A imagem deve ser enviada para Firebase Storage
3. A URL da imagem deve ser salva no banco de dados

## 🔍 Solução de Problemas Comuns

### Problema: App não compila

**Solução**: Verifique se o `google-services.json` está na pasta `app/`

### Problema: Mapa não carrega

**Solução**:

- Verifique se a API key está correta no `AndroidManifest.xml`
- Certifique-se de que "Maps SDK for Android" está habilitado

### Problema: Login não funciona

**Solução**:

- Verifique se o número de teste está configurado no Firebase
- Confirme se o SHA-1 está correto no Firebase Console

### Problema: API não conecta

**Solução**:

- Para emulador: use `http://10.0.2.2:PORT`
- Para dispositivo: use o IP real da máquina
- Verifique se a API está rodando

### Problema: Upload de imagem falha

**Solução**:

- Verifique permissões de storage no `AndroidManifest.xml`
- Confirme se Firebase Storage está configurado

## 📱 Estruturas de Dados

### Car Model

```json
{
  "id": "string",
  "imageUrl": "https://firebase-storage-url",
  "year": "2020/2020",
  "name": "Nome do Carro",
  "licence": "ABC-1234",
  "place": {
    "lat": -23.5505,
    "long": -46.6333
  }
}
```

### Números de Teste Firebase

- **Telefone**: `+55 11 91234-5678`
- **Código**: `123456`