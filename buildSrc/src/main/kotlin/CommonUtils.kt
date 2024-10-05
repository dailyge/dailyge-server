import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

private const val COOKIE_AUTH = "cookieAuth"

fun addAuthOption(content: String): String {
    val gson = Gson()
    val jsonObject = gson.fromJson(content, JsonObject::class.java)

    val securitySchemes = JsonObject().apply {
        add(
            COOKIE_AUTH,
            JsonObject().apply {
                addProperty("type", "apiKey")
                addProperty("in", "cookie")
                addProperty("name", "Access-Token")
            }
        )
    }

    jsonObject.getAsJsonObject("components")
        .add("securitySchemes", securitySchemes)
    jsonObject.getAsJsonObject("paths").entrySet().forEach { path ->
        path.value.asJsonObject.entrySet()
            .forEach { operation ->
                val securityRequirement = JsonArray().apply {
                    add(
                        JsonObject().apply {
                            add(COOKIE_AUTH, JsonArray())
                        }
                    )
                }
                operation.value.asJsonObject
                    .add("security", securityRequirement)
            }
    }

    val globalSecurity = JsonArray().apply {
        add(
            JsonObject().apply {
                add(COOKIE_AUTH, JsonArray())
            }
        )
    }
    jsonObject.add("security", globalSecurity)
    return gson.toJson(jsonObject)
}
