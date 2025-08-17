package com.example.qgeni.data.model.mascot

import androidx.annotation.RawRes
import com.example.qgeni.R

data class Mascot(
    val name: String,
    @RawRes
    val idleLottie: Int,
    @RawRes
    val sadLottie: Int,
    @RawRes
    val angryLottie: Int,
    @RawRes
    val happyLottie: Int,
    @RawRes
    val surpriseLottie: Int,
    @RawRes
    val curiousLottie: Int
)

object MascotRepository {
    val mascot = Mascot(
        name = "Bam",
        idleLottie = R.raw.bam_chill,
        angryLottie = R.raw.bam_angry,
        sadLottie = R.raw.bam_curious,
        curiousLottie = R.raw.bam_smile,
        surpriseLottie = R.raw.bam_surprise,
        happyLottie = R.raw.bam_happy
    )

}