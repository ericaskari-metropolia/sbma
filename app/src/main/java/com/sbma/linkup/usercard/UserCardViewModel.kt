package com.sbma.linkup.usercard

import androidx.lifecycle.ViewModel
import java.util.UUID

class UserCardViewModel(
    private val repository: IUserCardRepository,
) : ViewModel() {
    fun allItemsStream(userId: UUID) = repository.getUserItemsStream(userId)
    fun getItemStream(id: UUID) = repository.getItemStream(id)

}