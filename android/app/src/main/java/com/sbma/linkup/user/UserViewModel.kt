package com.sbma.linkup.user

import androidx.lifecycle.ViewModel

class UserViewModel(
    private val repository: IUserRepository,
) : ViewModel() {
    val allItemsStream = repository.getAllItemsStream()
    fun getItemStream(id: String) = repository.getItemStream(id)

}