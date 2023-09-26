package com.sbma.linkup.userconnection

import androidx.lifecycle.ViewModel
import java.util.UUID

class UserConnectionViewModel(
    private val repository: IUserConnectionRepository,
) : ViewModel() {
    fun allItemsStream(userId: UUID) = repository.getUserItemsStream(userId)
    fun getItemStream(id: String) = repository.getItemStream(id)

}