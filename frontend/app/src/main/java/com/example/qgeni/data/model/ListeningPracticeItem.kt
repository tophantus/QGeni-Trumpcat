package com.example.qgeni.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.response.ListeningPracticeResponse
import com.example.qgeni.data.repositories.DefaultFileRepository
import java.time.LocalDateTime


data class ListeningPracticeItem(
    override val id: Int,
    override val title: String,
    override val creationDate: LocalDateTime,
    override val questionList: List<ListeningQuestion>
): PracticeItem(id, title, creationDate, questionList) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromResponse(response: ListeningPracticeResponse): ListeningPracticeItem {
            val questionList = response.questionList.map {
                ListeningQuestion(
                    imageList = it.imageList.map { imgUrl ->
                        DefaultFileRepository.getAbsolutePath(imgUrl)
                    },
                    answerIndex = it.answerIndex,
                    mp3File = DefaultFileRepository.getAbsolutePath(it.mp3File),
                    caption = it.caption
                )
            }
            return ListeningPracticeItem(
                id = response.id,
                title = response.title,
                creationDate = LocalDateTime.parse(response.creationDate),
                questionList = questionList
            )
        }
    }
}


/**
 *  @property imageList: danh sách hình ảnh cho 1 câu hỏi
 *  @property answerIndex: vị trí của hình ảnh được mô tả trong imageList
 *  @property mp3File: file mp3 chứa mô tả của hình ảnh
 */
data class ListeningQuestion(
    val imageList: List<String>,
    val answerIndex: Int,
    val mp3File: String,
    val caption: String
)

