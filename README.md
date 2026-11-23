# Grimório de Hogwarts

Aplicativo Android desenvolvido para a disciplina DS151 - Desenvolvimento para Dispositivos Móveis.
Permite buscar personagens e criaturas do universo Harry Potter usando a Hogwarts API.

## Integrantes

- Danniel Eduardo Dorox - GRR20243721
- Gabriel Silva Costa - GRR20242064
- Murilo da Silva Santos - GRR20240992
- Reinaldo Antônio Castellano Neto - GRR20241499

## Funcionalidades

- Busca de Personagem: mostra o nome completo, a espécie e a casa do personagem.
- Busca de Criatura: mostra a imagem e o nome da criatura.

## Tecnologias

- Kotlin
- minSdk 29
- Retrofit + Gson
- Picasso
- Corrotinas

## API utilizada

Hogwarts API: `https://hogwarts-api.com/api/`

- `characters?search=` para a busca de personagens
- `creatures?search=` para a busca de criaturas

## Como executar

1. Clone o repositório:
   ```
   git clone https://github.com/gabrielcosta07/App3---Hogwarts-.git
   ```
2. Abra o projeto no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Execute o app em um emulador ou dispositivo com Android 10 (API 29) ou superior.
