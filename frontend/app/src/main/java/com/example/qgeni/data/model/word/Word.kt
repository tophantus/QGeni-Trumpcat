package com.example.qgeni.data.model.word

data class Word(
    val id: Int,
    val text: String,
    val pronunciation: String,
    val types: List<WordType>,
    val isFavorite: Boolean = false
)

data class WordType(
    val text: String,
    val meanings: List<WordMeaning>
)

data class WordMeaning(
    val text: String,
    val examples: List<Example>
)

data class Example(
    val src: String,
    val tgt: String
)

data class WordAccessHistory(
    val id: Int,
    val text: String,
    val pronunciation: String,
    val type: String,
    val meaning: String,
    val isFavorite: Boolean = false
)


object WordMockData {
    val words: List<Word> = listOf(
        Word(
            id = 1,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "When in Rome, do as the Romans do",
                                    tgt = "Nhập gia tùy tục"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Word(
            id = 2,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "haha",
                                    tgt = "ba"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Word(
            id = 3,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "haha",
                                    tgt = "ba"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Word(
            id = 4,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "haha",
                                    tgt = "ba"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Word(
            id = 5,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "haha",
                                    tgt = "ba"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Word(
            id = 6,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "haha",
                                    tgt = "ba"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Word(
            id = 7,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "haha",
                                    tgt = "ba"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Word(
            id = 8,
            text = "hello",
            pronunciation = "/haaa/",
            types = listOf(
                WordType(
                    text = "noun",
                    meanings = listOf(
                        WordMeaning(
                            text = "qua tao",
                            examples = listOf(
                                Example(
                                    src = "haha",
                                    tgt = "ba"
                                )
                            )
                        )
                    )
                )
            )
        )
    )

    val wordAccessHistories = listOf(
        WordAccessHistory(
            id = 0,
            text = "She",
            pronunciation = "shi",
            type = "Danh từ",
            meaning = "Cô ấy, chị ấy, em ấy",
            isFavorite = true
        ),
        WordAccessHistory(
            id = 0,
            text = "Sheeeee",
            pronunciation = "shiiiiiii",
            type = "Danh từ",
            meaning = "Cô ấy, chị ấy, em ấy",
            isFavorite = false
        ),
        WordAccessHistory(
            id = 0,
            text = "She",
            pronunciation = "shi",
            type = "Danh từ",
            meaning = "Cô ấy, chị ấy, em ấy",
            isFavorite = true
        )
    )
}
