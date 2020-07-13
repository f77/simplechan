package io.github.f77.simplechan.repositories

import io.github.f77.simplechan.entities.BoardEntity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class DvachRepository : ImageboardRepositoryInterface {
    override fun loadBoards(): List<BoardEntity> {
        // Execute a request just synchronously and throw any exceptions.
        val client = OkHttpClient();

        val request = Request.Builder()
            .url("https://2ch.hk/boards.json")
            .build()

        val response = client.newCall(request).execute()
        return parseBoards(response.body()!!.string())
    }

    private fun parseBoards(rawJson: String): List<BoardEntity> {
        val resultList: MutableList<BoardEntity> = mutableListOf()

        val json = JSONObject(rawJson)
        val boardsArray = json.getJSONArray("boards")

        for (i in 0 until boardsArray.length()) {
            val boardObject: JSONObject = boardsArray.get(i) as JSONObject

            val boardEntity = BoardEntity(boardObject.getString("id"))
                .apply {
                    name = boardObject.getString("name")
                    categoryName = boardObject.getString("category")
                }
            resultList.add(boardEntity)
        }
        return resultList
    }
}
