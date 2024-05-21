package com.packt.domain.user

class GetUserData() {
    //We are using here a dummy object as this implementation is out of the scope of this project in the book
    fun getData(): UserData {
        return UserData(
            id = "1",
            name = "John Doe",
            avatar = ""
        )
    }
}