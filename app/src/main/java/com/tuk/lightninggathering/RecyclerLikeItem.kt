package com.tuk.lightninggathering

class RecyclerLikeItem(
    var title: String, var date: String, //현재 참여 인원
    var current_peoplenum: Int, total_peoplenum: Int, place: String
) {
    var total_peoplenum //총 모집인원
            : Int
    var place: String

    init {
        current_peoplenum = current_peoplenum
        this.total_peoplenum = total_peoplenum
        this.place = place
    }
}
