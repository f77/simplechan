package io.github.f77.simplechan.repositories

import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.entities.ThreadEntity
import io.github.f77.simplechan.entities.post.PostEntity
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class DvachRepository : AbstractRepository() {
    override val name: String = "Dvach"
    override val defaultDomains: List<String> = listOf("2ch.hk", "2ch.pm")
    override val domain: String = defaultDomains[0]

    override fun loadBoards(): List<BoardEntity> {
        // Execute a request just synchronously and throw any exceptions.
        val request = Request.Builder()
            .url("$schema$domain/boards.json")
            .build()

        val response = httpClient.newCall(request).execute()
        return parseBoards(response.body()!!.string())
    }

    override fun loadThreads(board: BoardEntity, pageNumbers: List<Int>): List<ThreadEntity> {
        val pages = if (pageNumbers.isEmpty()) listOf(1) else pageNumbers

        val result: MutableList<ThreadEntity> = mutableListOf()
        for (i in pages.indices) {
            val subResult = loadSingleThreadsPage(board, i)
            result.addAll(subResult)
        }

        return result
    }

    private fun loadSingleThreadsPage(board: BoardEntity, pageNumber: Int): List<ThreadEntity> {
        val request = Request.Builder()
            .url("$schema$domain/" + board.id + "/" + pageNumber + ".json")
            .build()

        val response = httpClient.newCall(request).execute()
        return parseThreads(response.body()!!.string(), board)
    }

    private fun parseThreads(rawJson: String, board: BoardEntity): List<ThreadEntity> {
        val resultList: MutableList<ThreadEntity> = mutableListOf()

        val json = JSONObject(rawJson)
        val threadsArray = json.getJSONArray("threads")

        for (i in 0 until threadsArray.length()) {
            val threadObject: JSONObject = threadsArray.get(i) as JSONObject

            val allPosts = parsePosts(threadObject.getJSONArray("posts"))
            val opPostEntity = allPosts[0]

            val threadEntity = ThreadEntity(
                threadObject.getString("thread_num"),
                board,
                opPostEntity,
                allPosts.subList(1, allPosts.size - 1)
            )
            resultList.add(threadEntity)
        }

        return resultList
    }

    private fun parsePosts(postsArray: JSONArray): List<PostEntity> {
        val resultList: MutableList<PostEntity> = mutableListOf()
        for (i in 0 until postsArray.length()) {
            val postObject: JSONObject = postsArray.get(i) as JSONObject
            val postEntity = PostEntity(postObject.getString("num")).apply {
                timestamp = postObject.getInt("timestamp")
                comment = postObject.getString("comment")
            }

            resultList.add(postEntity)
        }

        return resultList
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
