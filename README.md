## Endpoints
> Note: Для ознакомления с доступными языками для перевода и их кодами, рекомендуется сначала выполнить GET /translate/languages
----
#### `GET` /translate/languages
----

Возвращает Json фомата:
```json5
{
	{ "languages": [ { "code": "az", "name": "azərbaycan" },
                     { "code": "sq", "name": "shqip" },
                        ..... ]}
}
```
**Parameters**:

* `languages` - Список доступных языков и их кодов.
----
#### `POST` /translate
----
Json формат на вход:
```json5
{
	"text": "Пример текста",
	"source_code": "ru",
	"target_code": "en"
}
```
**Parameters**:

* `text` - Строка для перевода
*  `source_code` - Код исходного языка.(не обязательный)
* `target_code` - Код языка перевода.



Возвращает Json фомата:
```json5
{
	"translated_text": "Sample text",
	"source_language": "ru",
	"target_language": "en"
}
```
**Parameters**:

* `translated_text` - Строка с переведёнными словами.
*  `source_language` - Код исходного языка.
* `target_language` - Код языка перевода.




## Technology stack


| Stack |
| ------ |
| Java |
| Spring Boot |
| H2 DB |
| Yandex Translate API |
| Medium |
| Google Analytics |

## Docker

Чтобы запустить приложение, нужно перейти в корень проекта и выполнить следующие команды:
```sh
docker compose build --no-cache
docker compose up
```
После чего приложение запуститься на 7777 порте. Если этот порт занят, его можно изменить на любой другой в `.env` изменив значение переменной `APP_PORT`

## Testing
Примеры запросов через curl:
`POST /translate`
```sh
curl --request POST \
  --header "Date: $(date +%T)" \
  --header "Content-Type: application/json" \
  --data '{
    "text": "За окном стояла прекрасная погода: светило солнце, на небе отсутствовал даже намек на облачность",
    "source_code": "ru",
    "target_code": "en"
}' \
  "localhost:8080/api/v1/translate"
```
Ответ:
```json5
{
    "translated_text": "Behind window stood beautiful weather the luminary the sun on sky was absent even hint on cloud cover",
    "source_language": "ru",
    "target_language": "en"
}
```
> Note: `--header "Date: $(date +%T)"` - нужен для сохранения времени обращения в бд.
----
## DB Schema
<img width="553" alt="image" src="https://user-images.githubusercontent.com/79803012/227827371-0f374ebc-fa54-431b-ad25-e43686208783.png">
